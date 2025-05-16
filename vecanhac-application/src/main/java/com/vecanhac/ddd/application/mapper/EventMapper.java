package com.vecanhac.ddd.application.mapper;

import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    @Autowired
    private TicketRepository ticketRepository;

    public EventResponseDTO toEventResponseDTO(EventEntity event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setSlug(event.getSlug());
        dto.setCoverImageUrl(event.getCoverImageUrl());
        dto.setStartTime(event.getStartTime());

        var minPrice = ticketRepository.findMinPriceByEventId(event.getId());
        if (minPrice != null) {
            dto.setMinTicketPrice(minPrice.doubleValue());
        }
        return dto;
    }
}
