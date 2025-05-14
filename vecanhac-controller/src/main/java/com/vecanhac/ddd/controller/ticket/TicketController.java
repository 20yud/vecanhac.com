package com.vecanhac.ddd.controller.ticket;

import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
import com.vecanhac.ddd.application.dto.order.MyOrderDTO;
import com.vecanhac.ddd.application.dto.order.OrderItemInOrderDTO;
import com.vecanhac.ddd.application.service.ticket.TicketAppService;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;
import com.vecanhac.ddd.domain.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

    private final TicketAppService ticketAppService;


    //My order API
    @GetMapping("/my-orders")
    public List<MyOrderDTO> getMyOrders(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(name = "status", defaultValue = "ALL") String status,
            @RequestParam(name = "filter", defaultValue = "upcoming") String filter
    ) {
        return ticketAppService.getMyOrders(user.getId(), status.toUpperCase(), filter.toLowerCase());
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemInOrderDTO>> getOrderItems(
            @PathVariable("orderId")  Long orderId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        List<OrderItemInOrderDTO> items = ticketAppService.getOrderItemsByOrder(orderId, user.getId());
        return ResponseEntity.ok(items);
    }




    //Create Order API
    @PostMapping
    public ResponseEntity<Long> createOrder(
            @RequestBody CreateOrderRequestDTO request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        request.setUserId(user.getId());
        return ResponseEntity.ok(ticketAppService.createOrder(request));
    }

    //Cancel Order API
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        ticketAppService.cancelOrder(orderId, user.getId());
        return ResponseEntity.ok().build();
    }
    //Cancel Each Tickets In Order API
    @PutMapping("/order-items/{itemId}/cancel")
    public ResponseEntity<Void> cancelOrderItem(
            @PathVariable("itemId") Long itemId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        ticketAppService.cancelOrderItem(itemId, user.getId());
        return ResponseEntity.ok().build();
    }



}