package com.vecanhac.ddd.application.mapper;

import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.domain.event.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponseDTO toEventResponseDTO(EventEntity event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setSlug(event.getSlug());
        dto.setCoverImageUrl(event.getCoverImageUrl());
        dto.setStartTime(event.getStartTime());
        return dto;
    }
}
