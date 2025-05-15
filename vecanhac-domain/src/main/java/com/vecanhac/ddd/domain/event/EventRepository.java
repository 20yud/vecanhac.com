package com.vecanhac.ddd.domain.event;

import com.vecanhac.ddd.domain.model.enums.EventStatus;
import com.vecanhac.ddd.domain.projection.EventTrendingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long>, EventRepositoryCustom {
    // Tự thêm cái đặc biệt nếu muốn

//    @Query("SELECT e FROM EventEntity e WHERE e.title LIKE %:keyword%")
//    List<EventEntity> searchByKeyword(String keyword);

    @Query("""
    SELECT 
        t.showing.event.id AS id,
        t.showing.event.title AS title,
        t.showing.event.slug AS slug,
        t.showing.event.coverImageUrl AS coverImageUrl,
        MIN(t.price) AS minTicketPrice,
        SUM(t.quantitySold) AS totalSold
    FROM TicketEntity t
    WHERE t.showing.event.startTime > CURRENT_TIMESTAMP
    GROUP BY t.showing.event.id, t.showing.event.title, t.showing.event.slug, t.showing.event.coverImageUrl
    ORDER BY SUM(t.quantitySold) DESC
""")
    List<EventTrendingProjection> findTrendingEvents(Pageable pageable);

    List<EventEntity> findByStatus(EventStatus status);

    Page<EventEntity> findByStatus(EventStatus status, Pageable pageable);

    boolean existsBySlug(String slug); // ✅ Trả về boolean

}
