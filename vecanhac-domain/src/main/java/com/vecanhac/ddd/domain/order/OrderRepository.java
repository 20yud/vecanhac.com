package com.vecanhac.ddd.domain.order;


import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
            SELECT 
                o.id AS orderId,
                e.title AS eventTitle,
                CONCAT(s.startTime, ' - ', s.endTime) AS showingTime,
                t.name AS ticketName,
                oi.quantity AS quantity,
                oi.quantity * oi.priceEach AS totalPrice,
                o.status AS orderStatus,
                e.coverImageUrl AS coverImageUrl,
                e.address AS eventAddress
            FROM OrderEntity o
            JOIN o.orderItems oi
            JOIN oi.ticket t
            JOIN t.showing s
            JOIN s.event e
            WHERE o.userId = :userId
              AND (
                (:upcoming = true AND s.startTime > CURRENT_TIMESTAMP) OR
                (:upcoming = false AND s.startTime <= CURRENT_TIMESTAMP)
              )
            ORDER BY o.createdAt DESC
            """)
    List<MyTicketProjection> findByUserAndUpcoming(
            @Param("userId") Long userId,
            @Param("upcoming") boolean upcoming
    );


    @Query("""
                SELECT 
                    o.id AS orderId,
                    e.title AS eventTitle,
                    CONCAT(s.startTime, ' - ', s.endTime) AS showingTime,
                    t.name AS ticketName,
                    oi.quantity AS quantity,
                    oi.quantity * oi.priceEach AS totalPrice,
                    o.status AS orderStatus,
                    e.coverImageUrl AS coverImageUrl,
                    e.address AS eventAddress
                FROM OrderEntity o
                JOIN o.orderItems oi
                JOIN oi.ticket t
                JOIN t.showing s
                JOIN s.event e
                WHERE o.userId = :userId
                  AND o.status = :status
                  AND (
                    (:upcoming = true AND s.startTime > CURRENT_TIMESTAMP) OR
                    (:upcoming = false AND s.startTime <= CURRENT_TIMESTAMP)
                  )
                ORDER BY o.createdAt DESC
                """)
    List<MyTicketProjection> findByUserAndStatusAndUpcoming(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("upcoming") boolean upcoming
    );

}