package com.vecanhac.ddd.domain.event;

import java.util.List;

public interface EventRepositoryCustom {


    List<EventEntity> searchByKeyword(String keyword);
}
