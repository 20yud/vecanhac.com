package com.vecanhac.ddd.controller.event;


import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {


    @Autowired
    private EventAppService eventAppService;

    @GetMapping
    public List<EventEntity> getAllEvents() {
        return eventAppService.getAllEvents();
    }


    @GetMapping("/{id}")
    public Optional<EventEntity> getEventById(@PathVariable Long id) {
        return eventAppService.getEventById(id);
    }

    @GetMapping("/slug/{slug}")
    public Optional<EventEntity> getEventBySlug(@PathVariable String slug) {
        return eventAppService.getEventBySlug(slug);
    }

    @GetMapping("/search")
    public List<EventEntity> searchEvents(@RequestParam(name = "keyword") String keyword) {
        return eventAppService.search(keyword);
    }
}
