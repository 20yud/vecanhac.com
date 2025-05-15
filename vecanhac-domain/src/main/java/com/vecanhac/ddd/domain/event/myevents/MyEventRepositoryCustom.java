package com.vecanhac.ddd.domain.event.myevents;

import com.vecanhac.ddd.domain.event.EventEntity;

import java.util.List;

public interface MyEventRepositoryCustom {
    List<EventEntity> searchMyEvents(MyEventSearchFilter filter);
}
