package com.vecanhac.ddd.infrastructure.config;

import com.vecanhac.ddd.domain.model.enums.TicketStatus;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketStatusScheduler {

    private final TicketRepository ticketRepository;

    // Chạy mỗi 24h (86_400_000 ms)
    @Scheduled(fixedRate = 86_400_000)
    public void updateSoldOutTickets() {
        List<TicketEntity> expiredTickets = ticketRepository.findTicketsToMarkAsSoldOut();
        for (TicketEntity ticket : expiredTickets) {
            ticket.setStatus(TicketStatus.SOLD_OUT);
        }
        ticketRepository.saveAll(expiredTickets);
        log.info("✅ Updated {} tickets to SOLD_OUT", expiredTickets.size());
    }
}