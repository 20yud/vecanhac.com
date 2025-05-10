package com.vecanhac.ddd.controller.ticket;

import com.vecanhac.ddd.application.dto.order.CreateOrderRequestDTO;
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


    @GetMapping("/my-tickets")
    public List<MyTicketProjection> getMyTickets(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam(name = "status", defaultValue = "ALL") String status,
            @RequestParam(name = "filter", defaultValue = "upcoming") String filter
    ) {
        return ticketAppService.getMyTickets(userId, status.toUpperCase(), filter.toLowerCase());
    }



    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody CreateOrderRequestDTO request) {
        Long orderId = ticketAppService.createOrder(request);
        return ResponseEntity.ok(orderId);
    }
}