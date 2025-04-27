package com.vecanhac.ddd.application.service.event;

import com.vecanhac.ddd.application.dto.EventDetailDTO;
import com.vecanhac.ddd.application.dto.EventResponseDTO;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventTrendingProjection;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EventAppService {
    List<EventResponseDTO> getAllEvents();
    Page<EventResponseDTO> findAllEvents(Pageable pageable);
    Optional<EventEntity> getEventById(Long id);
    Optional<EventEntity> getEventBySlug(String slug);
    List<EventEntity> search(String keyword);
    List<EventTrendingProjection> getTrendingEvents();
    EventDetailDTO getEventDetail(Long id);

}
