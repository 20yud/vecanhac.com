package com.vecanhac.ddd.domain.showing;

import com.vecanhac.ddd.domain.event.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowingRepository extends JpaRepository<ShowingEntity, Long> {

    List<ShowingEntity> findByEvent(EventEntity event);
}
