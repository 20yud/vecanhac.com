package com.vecanhac.ddd.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long>, EventRepositoryCustom {
    // Tự thêm cái đặc biệt nếu muốn
    Optional<EventEntity> findBySlug(String slug);

//    @Query("SELECT e FROM EventEntity e WHERE e.title LIKE %:keyword%")
//    List<EventEntity> searchByKeyword(String keyword);

}
