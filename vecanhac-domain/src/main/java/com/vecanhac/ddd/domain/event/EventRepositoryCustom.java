package com.vecanhac.ddd.domain.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventRepositoryCustom {


    Page<EventEntity> searchEvents(EventSearchFilter filter, Pageable pageable);
}
