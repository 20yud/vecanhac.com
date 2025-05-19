package com.vecanhac.ddd.domain.event;

import com.vecanhac.ddd.domain.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventRepositoryCustom {


    Page<EventEntity> searchEvents(EventSearchFilter filter, Pageable pageable);

    Page<EventEntity> searchEvents(AdminEventSearchCriteria criteria, Pageable pageable);
}
