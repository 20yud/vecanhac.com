package com.vecanhac.ddd.application.service.admin.impl;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.application.dto.location.LocationDTO;
import com.vecanhac.ddd.application.mapper.CategoryMapper;
import com.vecanhac.ddd.application.mapper.LocationMapper;
import com.vecanhac.ddd.domain.category.CategoryEntity;
import com.vecanhac.ddd.domain.category.CategoryRepository;
import com.vecanhac.ddd.domain.event.AdminEventSearchCriteria;
import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.myevents.PatchUpdateEventDTO;
import com.vecanhac.ddd.application.dto.showing.ShowingDTO;
import com.vecanhac.ddd.application.dto.showing.UpdateShowingDTO;
import com.vecanhac.ddd.application.dto.ticket.TicketDTO;
import com.vecanhac.ddd.application.dto.ticket.UpdateTicketDTO;
import com.vecanhac.ddd.application.exception.BadRequestException;
import com.vecanhac.ddd.application.mapper.TicketMapper;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.eventcategory.EventCategoryEntity;
import com.vecanhac.ddd.domain.eventcategory.EventCategoryRepository;
import com.vecanhac.ddd.domain.location.LocationEntity;
import com.vecanhac.ddd.domain.location.LocationRepository;
import com.vecanhac.ddd.domain.model.enums.EventStatus;
import com.vecanhac.ddd.domain.model.enums.TicketStatus;
import com.vecanhac.ddd.domain.model.enums.UserRole;
import com.vecanhac.ddd.domain.showing.ShowingEntity;
import com.vecanhac.ddd.domain.showing.ShowingRepository;
import com.vecanhac.ddd.domain.ticket.TicketEntity;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import com.vecanhac.ddd.domain.user.UserEntity;
import com.vecanhac.ddd.domain.user.UserRepository;
import com.vecanhac.ddd.application.dto.admin.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.vecanhac.ddd.application.mapper.EventMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAppServiceImpl implements AdminAppService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ShowingRepository showingRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepo;
    private final LocationRepository locationRepo;
    private final EventCategoryRepository eventCategoryRepository;



    @Override
    public List<AdminEventSummaryDTO> getEventsByStatus(String statusString) {
        EventStatus status;
        try {
            status = EventStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái không hợp lệ: " + statusString);
        }

        List<EventEntity> events = eventRepository.findByStatus(status);

        return events.stream()
                .map(event -> AdminEventSummaryDTO.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .venue(event.getVenue())
                        .startTime(event.getStartTime())
                        .endTime(event.getEndTime())
                        .status(event.getStatus())
                        .organizerName(event.getOrganizerName())
                        .build())
                .toList();
    }


    @Transactional
    public void approveEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        event.setStatus(EventStatus.PUBLISHED); // ✅ Published sau khi admin duyệt
    }

    @Transactional
    public void rejectEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));
        event.setStatus(EventStatus.REJECT); // ✅ Trả về nháp nếu bị từ chối
    }



    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new AdminUserDTO(
                        u.getId(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole().name(),
                        u.isVerified()
                ))
                .toList();
    }

    @Override
    public List<AdminUserDTO> searchUsersByRole(String roleStr) {
        try {
            UserRole role = UserRole.valueOf(roleStr.toUpperCase());
            return userRepository.findByRole(role).stream()
                    .map(u -> new AdminUserDTO(
                            u.getId(),
                            u.getEmail(),
                            u.getFullName(),
                            u.getRole().name(),
                            u.isVerified()
                    ))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Vai trò không hợp lệ: " + roleStr);
        }
    }



    @Transactional
    public void updateUserRole(Long userId, String newRoleStr) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        UserRole newRole = UserRole.valueOf(newRoleStr.toUpperCase());

        if (user.getRole() != newRole) {
            user.setRole(newRole);
        }
    }

    @Override
    public List<EventResponseDTO> getAllEventsForAdmin(String statusStr) {
        List<EventEntity> events;

        if (StringUtils.hasText(statusStr) && !statusStr.equalsIgnoreCase("ALL")) {
            try {
                EventStatus status = EventStatus.valueOf(statusStr.toUpperCase());
                events = eventRepository.findByStatus(status);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Trạng thái không hợp lệ: " + statusStr);
            }
        } else {
            events = eventRepository.findAll(); // ✅ lấy tất cả
        }

        return events.stream()
                .map(eventMapper::toEventResponseDTO)
                .toList();
    }



    //Event

    @Override
    public EventDetailDTO getEventDetailAsAdmin(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
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
        dto.setStatus(event.getStatus().name());
        dto.setLocationId(event.getLocationId());
        // Truy xuất category_id từ bảng event_categories
        eventCategoryRepository.findByEventId(eventId)
                .ifPresent(eventCategory -> dto.setCategoryId(eventCategory.getCategoryId()));


        var minPrice = ticketRepository.findMinPriceByEventId(eventId);
        dto.setMinTicketPrice(minPrice != null ? minPrice.doubleValue() : null);

        List<ShowingEntity> showings = showingRepository.findByEvent(event);
        List<ShowingDTO> showingDTOs = showings.stream()
                .map(showing -> {
                    ShowingDTO s = new ShowingDTO();
                    s.setId(showing.getId());
                    s.setStartTime(showing.getStartTime());
                    s.setEndTime(showing.getEndTime());
                    s.setSeatMapId(showing.getSeatMapId());
                    s.setIsEnabledQueue(showing.getIsEnabledQueue());

                    List<TicketDTO> tickets = ticketRepository.findByShowing(showing)
                            .stream().map(TicketMapper::toDTO).toList();
                    s.setTickets(tickets);

                    return s;
                }).toList();

        dto.setShowings(showingDTOs);
        return dto;
    }

    @Transactional
    @Override
    public EventResponseDTO patchUpdateEventAsAdmin(Long eventId, PatchUpdateEventDTO request) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

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

        if (request.getCategoryId() != null) {
            // Xoá tất cả category cũ của sự kiện này
            eventCategoryRepository.deleteByEventId(eventId);

            // Tạo bản ghi mới trong bảng event_categories
            EventCategoryEntity newCategory = new EventCategoryEntity();
            newCategory.setEventId(eventId);
            newCategory.setCategoryId(request.getCategoryId());

            eventCategoryRepository.save(newCategory);
        }


        if (request.getShowings() != null) {
            for (UpdateShowingDTO showingDTO : request.getShowings()) {
                ShowingEntity showing;

                if (showingDTO.getId() == null) {
                    showing = new ShowingEntity();
                    showing.setEvent(event);
                } else {
                    showing = showingRepository.findById(showingDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch diễn ID = " + showingDTO.getId()));

                    if (!showing.getEvent().getId().equals(eventId)) {
                        throw new BadRequestException("Lịch diễn không thuộc sự kiện này");
                    }
                }

                if (showingDTO.getStartTime() != null) showing.setStartTime(showingDTO.getStartTime());
                if (showingDTO.getEndTime() != null) showing.setEndTime(showingDTO.getEndTime());
                if (showingDTO.getSeatMapId() != null) showing.setSeatMapId(showingDTO.getSeatMapId());
                if (showingDTO.getIsEnabledQueue() != null) showing.setIsEnabledQueue(showingDTO.getIsEnabledQueue());

                ShowingEntity savedShowing = showingRepository.save(showing);

                if (showingDTO.getTickets() != null) {
                    for (UpdateTicketDTO ticketDTO : showingDTO.getTickets()) {
                        TicketEntity ticket;
                        if (ticketDTO.getId() == null) {
                            ticket = new TicketEntity();
                            ticket.setShowing(savedShowing);
                            ticket.setStatus(TicketStatus.AVAILABLE);
                            ticket.setQuantitySold(0);
                        } else {
                            ticket = ticketRepository.findById(ticketDTO.getId())
                                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vé ID = " + ticketDTO.getId()));
                            if (!ticket.getShowing().getId().equals(savedShowing.getId())) {
                                throw new BadRequestException("Vé không thuộc lịch diễn này");
                            }
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
                    if (request.getStatus() != null) {
                        try {
                            EventStatus newStatus = EventStatus.valueOf(request.getStatus().toUpperCase());
                            event.setStatus(newStatus);
                        } catch (IllegalArgumentException e) {
                            throw new BadRequestException("Trạng thái không hợp lệ: " + request.getStatus());
                        }
                    }
                }
            }
        }

        EventEntity saved = eventRepository.save(event);
        return eventMapper.toEventResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteEventAsAdmin(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện"));

        if (event.getStatus() == EventStatus.PUBLISHED) {
            throw new BadRequestException("Không thể xóa sự kiện đã được xuất bản");
        }

        List<ShowingEntity> showings = showingRepository.findByEvent(event);
        for (ShowingEntity showing : showings) {
            ticketRepository.deleteAll(ticketRepository.findByShowing(showing));
        }
        showingRepository.deleteAll(showings);

        eventRepository.delete(event);
    }


    @Override
    public List<AdminEventSummaryDTO> searchEvents(AdminEventSearchCriteria criteria) {
        EventStatus status = null;
        if (!"ALL".equalsIgnoreCase(criteria.getStatus())) {
            try {
                status = EventStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Trạng thái không hợp lệ: " + criteria.getStatus());
            }
        }

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by("createdAt").descending());
        Page<EventEntity> events = eventRepository.searchEvents(criteria, pageable);


        return events.stream()
                .map(event -> AdminEventSummaryDTO.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .venue(event.getVenue())
                        .startTime(event.getStartTime())
                        .endTime(event.getEndTime())
                        .status(event.getStatus())
                        .organizerName(event.getOrganizerName())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void changeEventStatus(Long eventId, String newStatus) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy sự kiện với ID: " + eventId));

        EventStatus status;
        try {
            status = EventStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái không hợp lệ: " + newStatus);
        }

        event.setStatus(status);
        eventRepository.save(event);
    }


    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream().map(CategoryMapper::toDTO).toList();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        CategoryEntity entity = CategoryMapper.toEntity(dto);
        return CategoryMapper.toDTO(categoryRepo.save(entity));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepo.findAll().stream().map(LocationMapper::toDTO).toList();
    }

    @Override
    public LocationDTO createLocation(LocationDTO dto) {
        LocationEntity entity = LocationMapper.toEntity(dto);
        return LocationMapper.toDTO(locationRepo.save(entity));
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepo.deleteById(id);
    }

}
