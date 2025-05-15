package com.vecanhac.ddd.domain.event;

import com.vecanhac.ddd.domain.event.myevents.MyEventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyEventRepository extends JpaRepository<EventEntity, Long>, MyEventRepositoryCustom {}
