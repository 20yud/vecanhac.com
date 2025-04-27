package com.vecanhac.ddd.application.service.event.impl;

import com.vecanhac.ddd.application.dto.EventDetailDTO;
import com.vecanhac.ddd.application.dto.EventResponseDTO;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.event.EventTrendingProjection;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Service
public class EventAppServiceImpl implements EventAppService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<EventResponseDTO> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        return mapToResponseDTOs(events);
    }

    @Override
    public Page<EventResponseDTO> findAllEvents(Pageable pageable) {
        Page<EventEntity> events = eventRepository.findAll(pageable);
        return events.map(this::mapToResponseDTO);
    }

    @Override
    public Optional<EventEntity> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Optional<EventEntity> getEventBySlug(String slug) {
        return eventRepository.findBySlug(slug);
    }

    @Override
    public List<EventEntity> search(String keyword) {
        return eventRepository.searchByKeyword(keyword);
    }

    private List<EventResponseDTO> mapToResponseDTOs(List<EventEntity> events) {
        return events.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private EventResponseDTO mapToResponseDTO(EventEntity event) {
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

    public List<EventTrendingProjection> getTrendingEvents() {
        Pageable pageable = PageRequest.of(0, 10); // lấy 10 bản ghi
        return eventRepository.findTrendingEvents(pageable);
    }

    @Override
    public EventDetailDTO getEventDetail(Long id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        EventDetailDTO dto = new EventDetailDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setSlug(event.getSlug());
        dto.setDescription(event.getDescription());
        dto.setCoverImageUrl(event.getCoverImageUrl());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setVenue(event.getVenue());
        dto.setAndress(event.getAddress());

        var minPrice = ticketRepository.findMinPriceByEventId(id);
        if (minPrice != null) {
            dto.setMinTicketPrice(minPrice.doubleValue());
        } else {
            dto.setMinTicketPrice(null);
        }

        return dto;
    }

}
