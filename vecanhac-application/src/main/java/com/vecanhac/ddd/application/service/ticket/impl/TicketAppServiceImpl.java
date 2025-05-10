package com.vecanhac.ddd.application.service.ticket.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
import com.vecanhac.ddd.domain.order.OrderEntity;
import com.vecanhac.ddd.domain.orderitem.OrderItemEntity;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;
import com.vecanhac.ddd.application.service.ticket.TicketAppService;
import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import com.vecanhac.ddd.domain.order.OrderRepository;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketAppServiceImpl implements TicketAppService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;


    private static final String QR_BASE_PATH = "uploads/qrcodes/";


    @Override
    public List<MyTicketProjection> getMyTickets(Long userId, String statusParam, String filterParam) {
        boolean upcoming = !"past".equalsIgnoreCase(filterParam);

        return switch (statusParam.toUpperCase()) {
            case "SUCCESS" -> orderRepository.findByUserAndStatusAndUpcoming(userId, OrderStatus.SUCCESS, upcoming);
            case "PENDING" -> orderRepository.findByUserAndStatusAndUpcoming(userId, OrderStatus.PENDING, upcoming);
            case "PAID" -> orderRepository.findByUserAndStatusAndUpcoming(userId, OrderStatus.PAID, upcoming);
            case "CANCELLED" -> orderRepository.findByUserAndStatusAndUpcoming(userId, OrderStatus.CANCELLED, upcoming);
            default -> orderRepository.findByUserAndUpcoming(userId, upcoming); // ALL
        };
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
                String qrUrl = generateQrCodeAndSave(code);

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

        OrderEntity order = OrderEntity.builder()
                .userId(request.getUserId())
                .status(OrderStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .totalAmount(totalAmount)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
        return order.getId();
    }

    private String generateOrderItemCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String generateQrCodeAndSave(String content) {
        try {
            Files.createDirectories(Paths.get(QR_BASE_PATH));

            String filename = content + ".png";
            Path path = Paths.get(QR_BASE_PATH + filename);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 250, 250);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return "/qrcodes/" + filename; // Có thể là URL public nếu deploy
        } catch (IOException | WriterException e) {
            throw new RuntimeException("Lỗi khi tạo QR Code cho " + content, e);
        }
    }

}