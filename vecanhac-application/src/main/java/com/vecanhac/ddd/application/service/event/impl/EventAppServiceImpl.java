package com.vecanhac.ddd.application.service.event.impl;

import com.vecanhac.ddd.application.dto.event.myevents.CreateEventRequestDTO;
import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.myevents.MyEventSearchCriteria;
import com.vecanhac.ddd.application.dto.event.myevents.PatchUpdateEventDTO;
import com.vecanhac.ddd.application.dto.search.EventSearchCriteria;
import com.vecanhac.ddd.application.dto.search.EventSearchResponseDTO;
import com.vecanhac.ddd.application.dto.showing.CreateShowingDTO;
import com.vecanhac.ddd.application.dto.showing.ShowingDTO;
import com.vecanhac.ddd.application.dto.showing.UpdateShowingDTO;
import com.vecanhac.ddd.application.dto.ticket.CreateTicketDTO;
import com.vecanhac.ddd.application.dto.ticket.TicketDTO;
import com.vecanhac.ddd.application.dto.ticket.UpdateTicketDTO;
import com.vecanhac.ddd.application.exception.BadRequestException;
import com.vecanhac.ddd.application.helper.SlugUtil;
import com.vecanhac.ddd.application.mapper.EventMapper;
import com.vecanhac.ddd.application.mapper.TicketMapper;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.event.EventSearchFilter;
import com.vecanhac.ddd.domain.event.MyEventRepository;
import com.vecanhac.ddd.domain.event.myevents.MyEventSearchFilter;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventAppServiceImpl implements EventAppService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowingRepository showingRepository;

    private static final Logger log = LoggerFactory.getLogger(EventAppServiceImpl.class);

    @Autowired
    private MyEventRepository myEventRepository;

    @Autowired
    private EventMapper eventMapper;

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
        String baseSlug = SlugUtil.slugify(request.getTitle());
        String uniqueSlug = baseSlug;
        int suffix = 1;

        while (eventRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + suffix;
            suffix++;
        }

        event.setSlug(uniqueSlug);

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

    @Override
    public List<EventResponseDTO> getMyEvents(Long organizerId, MyEventSearchCriteria criteria) {
        EventStatus status = null;

        if (StringUtils.hasText(criteria.getStatus())) {
            try {
                status = EventStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException ex) {
                log.warn("Giá trị status không hợp lệ: {}", criteria.getStatus());
            }
        }

        Boolean upcoming = null;
        if ("upcoming".equalsIgnoreCase(criteria.getTime())) {
            upcoming = true;
        } else if ("past".equalsIgnoreCase(criteria.getTime())) {
            upcoming = false;
        }

        MyEventSearchFilter filter = MyEventSearchFilter.builder()
                .organizerId(organizerId)
                .keyword(criteria.getKeyword())
                .status(status)
                .upcoming(upcoming)
                .build();

        List<EventEntity> events = myEventRepository.searchMyEvents(filter);
        return events.stream()
                .map(eventMapper::toEventResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponseDTO patchUpdateEvent(Long eventId, PatchUpdateEventDTO request, Long userId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new BadRequestException("Không có quyền sửa sự kiện này");
        }

        // Chỉ cập nhật trường nào khác null
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getCoverImageUrl() != null) event.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getOrganizerName() != null) event.setOrganizerName(request.getOrganizerName());
        if (request.getOrganizerLogoUrl() != null) event.setOrganizerLogoUrl(request.getOrganizerLogoUrl());
        if (request.getStartTime() != null) event.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) event.setEndTime(request.getEndTime());
        if (request.getVenue() != null) event.setVenue(request.getVenue());
        if (request.getAddress() != null) event.setAddress(request.getAddress());
        if (request.getLocationId() != null) event.setLocationId(request.getLocationId());

        // Cập nhật từng showing
        if (request.getShowings() != null) {
            for (UpdateShowingDTO showingDTO : request.getShowings()) {
                ShowingEntity showing = showingRepository.findById(showingDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch diễn ID = " + showingDTO.getId()));

                if (!showing.getEvent().getId().equals(eventId)) {
                    throw new BadRequestException("Lịch diễn không thuộc sự kiện này");
                }

                if (showingDTO.getStartTime() != null) showing.setStartTime(showingDTO.getStartTime());
                if (showingDTO.getEndTime() != null) showing.setEndTime(showingDTO.getEndTime());
                if (showingDTO.getSeatMapId() != null) showing.setSeatMapId(showingDTO.getSeatMapId());
                if (showingDTO.getIsEnabledQueue() != null) showing.setIsEnabledQueue(showingDTO.getIsEnabledQueue());

                // Cập nhật ticket
                if (showingDTO.getTickets() != null) {
                    for (UpdateTicketDTO ticketDTO : showingDTO.getTickets()) {
                        TicketEntity ticket = ticketRepository.findById(ticketDTO.getId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé ID = " + ticketDTO.getId()));

                        if (!ticket.getShowing().getId().equals(showing.getId())) {
                            throw new BadRequestException("Vé không thuộc lịch diễn này");
                        }

                        if (ticketDTO.getName() != null) ticket.setName(ticketDTO.getName());
                        if (ticketDTO.getPrice() != null) ticket.setPrice(ticketDTO.getPrice());
                        if (ticketDTO.getOriginalPrice() != null) ticket.setOriginalPrice(ticketDTO.getOriginalPrice());
                        if (ticketDTO.getQuantityTotal() != null) ticket.setQuantityTotal(ticketDTO.getQuantityTotal());
                        if (ticketDTO.getSaleStart() != null) ticket.setSaleStart(ticketDTO.getSaleStart());
                        if (ticketDTO.getSaleEnd() != null) ticket.setSaleEnd(ticketDTO.getSaleEnd());
                        if (ticketDTO.getColor() != null) ticket.setColor(ticketDTO.getColor());
                        if (ticketDTO.getImageUrl() != null) ticket.setImageUrl(ticketDTO.getImageUrl());

                        ticketRepository.save(ticket);
                    }
                }

                showingRepository.save(showing);
            }
        }

        EventEntity saved = eventRepository.save(event);
        return mapToResponseDTO(saved);
    }



    @Override
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new BadRequestException("Không có quyền xóa sự kiện này");
        }

        if (event.getStatus() == EventStatus.PUBLISHED) {
            throw new BadRequestException("Không thể xóa sự kiện đã được xuất bản");
        }

        // Xóa showings và tickets liên quan
        List<ShowingEntity> showings = showingRepository.findByEvent(event);
        for (ShowingEntity showing : showings) {
            ticketRepository.deleteAll(ticketRepository.findByShowing(showing));
        }
        showingRepository.deleteAll(showings);

        eventRepository.delete(event);
    }

    @Override
    public EventDetailDTO getMyEventDetail(Long eventId, Long userId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new BadRequestException("Bạn không có quyền xem chi tiết sự kiện này");
        }

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

        var minPrice = ticketRepository.findMinPriceByEventId(eventId);
        dto.setMinTicketPrice(minPrice != null ? minPrice.doubleValue() : null);

        // Lấy showings
        List<ShowingDTO> showingDTOs = showingRepository.findByEvent(event).stream()
                .map(showing -> {
                    ShowingDTO s = new ShowingDTO();
                    s.setId(showing.getId());
                    s.setStartTime(showing.getStartTime());
                    s.setEndTime(showing.getEndTime());
                    s.setSeatMapId(showing.getSeatMapId());
                    s.setIsEnabledQueue(showing.getIsEnabledQueue());

                    List<TicketDTO> tickets = ticketRepository.findByShowing(showing).stream()
                            .map(TicketMapper::toDTO)
                            .toList();
                    s.setTickets(tickets);

                    return s;
                }).toList();

        dto.setShowings(showingDTOs);

        return dto;
    }




}
