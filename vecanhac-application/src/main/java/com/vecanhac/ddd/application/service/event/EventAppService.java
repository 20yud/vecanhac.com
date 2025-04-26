package com.vecanhac.ddd.application.service.event;

import com.vecanhac.ddd.domain.event.EventEntity;

import java.util.List;
import java.util.Optional;

public interface EventAppService {


    List<EventEntity> getAllEvents();

    Optional<EventEntity> getEventById(Long id);

    Optional<EventEntity> getEventBySlug(String slug);

    List<EventEntity> search(String keyword);
}
