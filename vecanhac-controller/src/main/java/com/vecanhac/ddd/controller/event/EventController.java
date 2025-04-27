package com.vecanhac.ddd.controller.event;


import com.vecanhac.ddd.application.dto.EventDetailDTO;
import com.vecanhac.ddd.application.dto.EventResponseDTO;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventTrendingProjection;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public List<EventEntity> searchEvents(@RequestParam(name = "keyword") String keyword) {
        return eventAppService.search(keyword);
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
