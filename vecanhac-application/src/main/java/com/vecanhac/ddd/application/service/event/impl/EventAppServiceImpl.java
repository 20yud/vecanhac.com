package com.vecanhac.ddd.application.service.event.impl;

import com.vecanhac.ddd.application.dto.event.CreateEventRequestDTO;
import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.search.EventSearchCriteria;
import com.vecanhac.ddd.application.dto.search.EventSearchResponseDTO;
import com.vecanhac.ddd.application.dto.showing.CreateShowingDTO;
import com.vecanhac.ddd.application.dto.ticket.CreateTicketDTO;
import com.vecanhac.ddd.application.exception.BadRequestException;
import com.vecanhac.ddd.application.mapper.TicketMapper;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.application.service.ticket.impl.TicketAppServiceImpl;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.event.EventSearchFilter;
import com.vecanhac.ddd.domain.projection.EventTrendingProjection;
import com.vecanhac.ddd.domain.model.enums.EventStatus;
import com.vecanhac.ddd.domain.model.enums.TicketStatus;
import com.vecanhac.ddd.domain.showing.ShowingEntity;
import com.vecanhac.ddd.domain.showing.ShowingRepository;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EventAppServiceImpl implements EventAppService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowingRepository showingRepository;

    private static final Logger log = LoggerFactory.getLogger(EventAppServiceImpl.class);


    @Override
    public List<EventResponseDTO> getAllEvents() {
        List<EventEntity> events = eventRepository.findByStatus(EventStatus.PUBLISHED);
        return mapToResponseDTOs(events);
    }

    @Override
    public Page<EventResponseDTO> findAllEvents(Pageable pageable) {
        Page<EventEntity> events = eventRepository.findByStatus(EventStatus.PUBLISHED, pageable);
        return events.map(this::mapToResponseDTO);
    }


    @Override
    public Optional<EventEntity> getEventBySlug(String slug) {
        return Optional.empty();
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
        dto.setOrgName(event.getOrganizerName());
        dto.setOrgImageUrl(event.getOrganizerLogoUrl());

        var minPrice = ticketRepository.findMinPriceByEventId(id);
        dto.setMinTicketPrice(minPrice != null ? minPrice.doubleValue() : null);

        // ✅ Thêm đoạn này để lấy danh sách vé
        var tickets = ticketRepository.findByEventId(id)
                .stream()
                .map(TicketMapper::toDTO)
                .toList();
        dto.setTickets(tickets); // đảm bảo EventDetailDTO có field `tickets`

        return dto;
    }

    @Override
    public List<EventSearchResponseDTO> searchEvents(EventSearchCriteria criteria, Pageable pageable) {
        EventSearchFilter filter = new EventSearchFilter();
        filter.setKeyword(criteria.getKeyword());
        filter.setCity(criteria.getCity());
        filter.setStartDate(criteria.getStartDate());
        filter.setEndDate(criteria.getEndDate());
        filter.setFreeOnly(criteria.getFreeOnly());
        filter.setCategoryId(criteria.getCategoryId());

        Page<EventEntity> page = eventRepository.searchEvents(filter, pageable);

        return page.map(this::mapToSearchResponseDTO).getContent();
    }

    private EventSearchResponseDTO mapToSearchResponseDTO(EventEntity event) {
        EventSearchResponseDTO dto = new EventSearchResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setSlug(event.getSlug());
        dto.setCoverImageUrl(event.getCoverImageUrl());
        dto.setStartTime(event.getStartTime());
        dto.setVenue(event.getVenue());
        var minPrice = ticketRepository.findMinPriceByEventId(event.getId());
        if (minPrice != null) {
            dto.setMinTicketPrice(minPrice.doubleValue());
        } else {
            dto.setMinTicketPrice(null); // nếu không có vé
        }
        return dto;
    }

    @Transactional
    @Override
    public EventResponseDTO createEvent(CreateEventRequestDTO request) {
        EventEntity event = new EventEntity();
        event.setTitle(request.getTitle());
        if (eventRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug đã tồn tại.");
        }
        event.setSlug(request.getSlug());
        event.setDescription(request.getDescription());
        event.setCoverImageUrl(request.getCoverImageUrl());
        event.setOrganizerName(request.getOrganizerName());
        event.setOrganizerLogoUrl(request.getOrganizerLogoUrl());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setVenue(request.getVenue());
        event.setAddress(request.getAddress());
        event.setLocationId(request.getLocationId());
        event.setCreatedAt(LocalDateTime.now());
        event.setStatus(EventStatus.PENDING);
        event.setOrganizerId(request.getOrganizerId()); // 👈 Đã gán từ controller: user.getId()

        EventEntity savedEvent = eventRepository.save(event);

        for (CreateShowingDTO showingReq : request.getShowings()) {
            ShowingEntity showing = new ShowingEntity();
            showing.setEvent(savedEvent);
            showing.setStartTime(showingReq.getStartTime());
            showing.setEndTime(showingReq.getEndTime());
            showing.setSeatMapId(showingReq.getSeatMapId());
            showing.setIsEnabledQueue(showingReq.getIsEnabledQueue() != null && showingReq.getIsEnabledQueue());

            ShowingEntity savedShowing = showingRepository.save(showing);

            for (CreateTicketDTO ticketReq : showingReq.getTickets()) {
                TicketEntity ticket = new TicketEntity();
                ticket.setShowing(savedShowing);
                ticket.setName(ticketReq.getName());
                ticket.setPrice(ticketReq.getPrice());
                ticket.setOriginalPrice(ticketReq.getOriginalPrice());
                ticket.setQuantityTotal(ticketReq.getQuantityTotal());
                ticket.setQuantitySold(0);
                ticket.setSaleStart(ticketReq.getSaleStart());
                ticket.setSaleEnd(ticketReq.getSaleEnd());
                ticket.setColor(ticketReq.getColor());
                ticket.setImageUrl(ticketReq.getImageUrl());
                ticket.setStatus(TicketStatus.AVAILABLE);

                ticketRepository.save(ticket);
            }
        }
        log.info("Creating event by user {}: {}", request.getOrganizerId(), request.getTitle());

        return mapToResponseDTO(savedEvent);
    }


}
