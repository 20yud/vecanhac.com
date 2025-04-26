package com.vecanhac.ddd.application.service.event.impl;

import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EventAppServiceImpl implements EventAppService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
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
}
