package com.vecanhac.ddd.controller.ticket;

import com.vecanhac.ddd.application.dto.ticket.MyTicketDTO;
import com.vecanhac.ddd.application.service.ticket.TicketAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

    private final TicketAppService TicketAppService;

    @GetMapping("/my-tickets")
    public ResponseEntity<List<MyTicketDTO>> getMyTickets(@AuthenticationPrincipal(expression = "id") Long userId) {
        return ResponseEntity.ok(TicketAppService.getMyTickets(userId));
    }
}