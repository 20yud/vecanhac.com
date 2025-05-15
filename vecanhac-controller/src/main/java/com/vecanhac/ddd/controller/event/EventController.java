package com.vecanhac.ddd.controller.event;


import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.search.EventSearchCriteria;
import com.vecanhac.ddd.application.dto.search.EventSearchResponseDTO;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.projection.EventTrendingProjection;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {


    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private TicketRepository ticketRepository;



    @GetMapping("/trending")
    public List<EventTrendingProjection> getTrendingEvents() {
        return eventAppService.getTrendingEvents();
    }

    @GetMapping("/slug/{slug}")
    public Optional<EventEntity> getEventBySlug(@PathVariable String slug) {
        return eventAppService.getEventBySlug(slug);
    }

    @GetMapping("/search")
    public List<EventSearchResponseDTO> searchEvents(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "freeOnly", required = false) Boolean freeOnly,
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        EventSearchCriteria criteria = new EventSearchCriteria();
        criteria.setKeyword(keyword);
        criteria.setCity(city);
        criteria.setStartDate(startDate != null ? LocalDate.parse(startDate) : null);
        criteria.setEndDate(endDate != null ? LocalDate.parse(endDate) : null);
        criteria.setFreeOnly(freeOnly);
        criteria.setCategoryId(categoryId);

        Pageable pageable = PageRequest.of(page, size);

        return eventAppService.searchEvents(criteria, pageable);
    }

    @GetMapping
    public List<EventResponseDTO> getAllEvents() {
        return eventAppService.getAllEvents();
    }

    @GetMapping("/page")
    public Page<EventResponseDTO> getEvents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return eventAppService.findAllEvents(
                PageRequest.of(page, size, Sort.by("startTime").descending())
        );
    }

    @GetMapping("/{id}")
    public EventDetailDTO getEventDetail(@PathVariable(name = "id") Long id) {
        return eventAppService.getEventDetail(id);
    }



}
