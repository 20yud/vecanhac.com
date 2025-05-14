package com.vecanhac.ddd.domain.ticket;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    // ✅ Cần sửa findByEventId thành query custom
    @Query("""
        SELECT t
        FROM TicketEntity t
        WHERE t.showing.event.id = :eventId
    """)
    List<TicketEntity> findByEventId(@Param("eventId") Long eventId);

    @Query("""
        SELECT MIN(t.price) 
        FROM TicketEntity t 
        WHERE t.showing.event.id = :eventId
    """)
    BigDecimal findMinPriceByEventId(@Param("eventId") Long eventId);


    @Query("SELECT t FROM TicketEntity t " +
            "JOIN t.showing s " +
            "WHERE s.startTime < CURRENT_TIMESTAMP " +
            "AND t.status != 'SOLD_OUT' " +
            "AND (t.quantitySold >= t.quantityTotal OR s.endTime < CURRENT_TIMESTAMP)")
    List<TicketEntity> findTicketsToMarkAsSoldOut();

}
