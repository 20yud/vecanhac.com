package com.vecanhac.ddd.domain.eventcategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventCategoryRepository extends JpaRepository<EventCategoryEntity, Long> {
    void deleteByEventId(Long eventId);
    Optional<EventCategoryEntity> findByEventId(Long eventId);

}