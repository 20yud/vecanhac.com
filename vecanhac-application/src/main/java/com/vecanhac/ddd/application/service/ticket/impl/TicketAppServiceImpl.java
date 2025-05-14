package com.vecanhac.ddd.application.service.ticket.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vecanhac.ddd.application.config.AppProperties;
import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
import com.vecanhac.ddd.application.dto.order.MyOrderDTO;
import com.vecanhac.ddd.application.dto.order.OrderItemInOrderDTO;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeEntity;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.order.OrderEntity;
import com.vecanhac.ddd.domain.orderitem.OrderItemEntity;
import com.vecanhac.ddd.domain.orderitem.OrderItemRepository;
import com.vecanhac.ddd.domain.projection.MyOrderProjection;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;
import com.vecanhac.ddd.application.service.ticket.TicketAppService;
import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import com.vecanhac.ddd.domain.order.OrderRepository;
import com.vecanhac.ddd.domain.showing.ShowingEntity;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketAppServiceImpl implements TicketAppService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final AppProperties appProperties;
    private final DiscountCodeRepository discountCodeRepository;
    private final OrderItemRepository orderItemRepository;


    private static final Logger log = LoggerFactory.getLogger(TicketAppServiceImpl.class);


    public List<MyOrderDTO> getMyOrders(Long userId, String status, String filter) {
        boolean isUpcoming = filter.equalsIgnoreCase("upcoming");

        List<MyOrderProjection> projections;

        if (status.equalsIgnoreCase("ALL")) {
            projections = orderRepository.findOrdersByUserAndUpcoming(userId, isUpcoming);
        } else {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            projections = orderRepository.findOrdersByUserAndStatusAndUpcoming(userId, orderStatus, isUpcoming);
        }

        return projections.stream().map(p -> MyOrderDTO.builder()
                .orderId(p.getOrderId())
                .eventTitle(p.getEventTitle())
                .eventAddress(p.getEventAddress())
                .coverImageUrl(p.getCoverImageUrl())
                .showingTime(p.getShowingTime())
                .totalPrice(p.getTotalPrice())
                .orderStatus(p.getOrderStatus())
                .createdAt(p.getCreatedAt())
                .build()
        ).toList();
    }

    public List<OrderItemInOrderDTO> getOrderItemsByOrder(Long orderId, Long userId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (!Objects.equals(order.getUserId(), userId)) {
            throw new AccessDeniedException("Không có quyền truy cập đơn hàng này");
        }

        return order.getOrderItems().stream().map(item -> {
            TicketEntity ticket = item.getTicket();
            ShowingEntity showing = ticket.getShowing();
            EventEntity event = showing.getEvent(); // cần đảm bảo Showing có getEvent()

            return OrderItemInOrderDTO.builder()
                    .orderItemCode(item.getOrderItemCode())
                    .qrCodeUrl(item.getQrCodeUrl())
                    .quantity(item.getQuantity())
                    .priceEach(item.getPriceEach())
                    .isUsed(item.getIsUsed())
                    .isCancelled(item.isCancelled())
                    .ticketName(ticket.getName())
                    .eventTitle(event.getTitle())
                    .eventImage(event.getCoverImageUrl())
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .build();
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Long createOrder(CreateOrderRequestDTO request) {
        List<OrderItemEntity> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderRequestDTO.TicketItem item : request.getTickets()) {
            TicketEntity ticket = ticketRepository.findById(item.getTicketId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            if (ticket.getQuantitySold() + item.getQuantity() > ticket.getQuantityTotal()) {
                throw new RuntimeException("Not enough tickets available for: " + ticket.getName());
            }

            ticket.setQuantitySold(ticket.getQuantitySold() + item.getQuantity());

            for (int i = 0; i < item.getQuantity(); i++) {
                String code = generateOrderItemCode();
                String qrUrl = generateQrCodeAndSave(code, ticket.getId());

                OrderItemEntity orderItem = OrderItemEntity.builder()
                        .ticket(ticket)
                        .priceEach(ticket.getPrice())
                        .quantity(1)
                        .orderItemCode(code)
                        .isUsed(false)
                        .qrCodeUrl(qrUrl)
                        .build();
                orderItems.add(orderItem);
                totalAmount = totalAmount.add(ticket.getPrice());
            }
        }

        // ✅ Tính tổng số vé
        int totalQuantity = request.getTickets().stream()
                .mapToInt(CreateOrderRequestDTO.TicketItem::getQuantity)
                .sum();

        BigDecimal discountAmount = BigDecimal.ZERO;
        String appliedDiscountCode = null;

        // ✅ Nếu có mã giảm giá
        if (request.getDiscountCode() != null && !request.getDiscountCode().isEmpty()) {
            DiscountCodeEntity discount = discountCodeRepository
                    .findByCodeAndIsActiveTrue(request.getDiscountCode())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ hoặc đã hết hạn"));

            if (totalQuantity >= discount.getMinQuantity()) {
                BigDecimal calculated = totalAmount.multiply(BigDecimal.valueOf(discount.getPercentage()))
                        .divide(BigDecimal.valueOf(100));

                if (discount.getMaxDiscountAmount() != null) {
                    calculated = calculated.min(discount.getMaxDiscountAmount());
                }

                discountAmount = calculated;
                appliedDiscountCode = discount.getCode();
            }
        }

        BigDecimal finalAmount = totalAmount.subtract(discountAmount);

        OrderEntity order = OrderEntity.builder()
                .userId(request.getUserId())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalAmount(finalAmount)
                .discountCode(appliedDiscountCode)
                .discountAmount(discountAmount)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
        return order.getId();
    }


    private String generateOrderItemCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String generateQrCodeAndSave(String content, Long ticketId) {
        try {
            String filename = "QR_" + ticketId + "_" + content + ".png";

            Path baseDir = Paths.get(System.getProperty("user.dir"), appProperties.getQrPath());
            Files.createDirectories(baseDir);
            Path path = baseDir.resolve(filename);
            log.info("✅ qrPath config = {}", appProperties.getQrPath());

            log.info("📦 Tạo QR code tại: {}", path.toAbsolutePath());

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 250, 250);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return "/qrcodes/" + filename;
        } catch (IOException | WriterException e) {
            log.error("❌ Lỗi QR: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi tạo QR Code cho " + content, e);
        }
    }


    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền huỷ đơn này");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Đơn hàng đã bị huỷ trước đó");
        }

        int activeTicketCount = 0;

        for (OrderItemEntity item : order.getOrderItems()) {
            if (!item.isCancelled()) {
                item.setCancelled(true);

                TicketEntity ticket = item.getTicket();
                ticket.setQuantitySold(ticket.getQuantitySold() - item.getQuantity());
                ticketRepository.save(ticket); // optional nếu cascade

                activeTicketCount += item.getQuantity();
            }
        }

        // ✅ Nếu toàn bộ vé đều đã bị huỷ thì cập nhật trạng thái đơn hàng
        boolean allCancelled = order.getOrderItems().stream().allMatch(OrderItemEntity::isCancelled);
        if (allCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrderItem(Long orderItemId, Long userId) {
        OrderItemEntity item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Vé không tồn tại"));

        OrderEntity order = item.getOrder();

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền huỷ vé này");
        }

        if (item.isCancelled()) {
            throw new RuntimeException("Vé này đã huỷ rồi");
        }

        // ✅ 1. Hủy vé
        item.setCancelled(true);

        TicketEntity ticket = item.getTicket();
        ticket.setQuantitySold(ticket.getQuantitySold() - item.getQuantity());
        ticketRepository.save(ticket);

        // ✅ 2. Đếm số vé chưa bị huỷ
        List<OrderItemEntity> orderItems = order.getOrderItems();
        long activeCount = orderItems.stream().filter(i -> !i.isCancelled()).count();

        // ✅ 3. Nếu còn < 2 vé => huỷ discount nếu có
        if (order.getDiscountCode() != null && activeCount < 2) {
            order.setDiscountCode(null);
            order.setDiscountAmount(BigDecimal.ZERO);
        }

        // ✅ 4. Nếu tất cả vé đều huỷ => set trạng thái CANCELLED
        boolean allCancelled = orderItems.stream().allMatch(OrderItemEntity::isCancelled);
        if (allCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        // ✅ 5. Cập nhật lại tổng tiền (vì có thể huỷ mã giảm giá)
        BigDecimal newTotal = orderItems.stream()
                .filter(i -> !i.isCancelled())
                .map(OrderItemEntity::getPriceEach)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(order.getDiscountAmount() == null ? BigDecimal.ZERO : order.getDiscountAmount());

        order.setTotalAmount(newTotal.max(BigDecimal.ZERO)); // đảm bảo không âm

        orderRepository.save(order);
    }



}