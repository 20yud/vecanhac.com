package com.vecanhac.ddd.application.service.event;

import com.vecanhac.ddd.application.dto.event.myevents.CreateEventRequestDTO;
import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.myevents.MyEventSearchCriteria;
import com.vecanhac.ddd.application.dto.event.myevents.PatchUpdateEventDTO;
import com.vecanhac.ddd.application.dto.search.EventSearchCriteria;
import com.vecanhac.ddd.application.dto.search.EventSearchResponseDTO;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.projection.EventTrendingProjection;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EventAppService {
    List<EventResponseDTO> getAllEvents();
    Page<EventResponseDTO> findAllEvents(Pageable pageable);
    Optional<EventEntity> getEventBySlug(String slug);
    List<EventTrendingProjection> getTrendingEvents();
    EventDetailDTO getEventDetail(Long id);
    List<EventSearchResponseDTO> searchEvents(EventSearchCriteria criteria, Pageable pageable);
    EventResponseDTO createEvent(CreateEventRequestDTO request);
    List<EventResponseDTO> getMyEvents(Long organizerId, MyEventSearchCriteria criteria);
    EventResponseDTO patchUpdateEvent(Long eventId, PatchUpdateEventDTO request, Long userId);
    void deleteEvent(Long eventId, Long userId);
    EventDetailDTO getMyEventDetail(Long eventId, Long userId);
}
