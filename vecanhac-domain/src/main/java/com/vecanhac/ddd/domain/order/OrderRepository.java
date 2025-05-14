package com.vecanhac.ddd.domain.order;


import com.vecanhac.ddd.domain.model.enums.OrderStatus;
import com.vecanhac.ddd.domain.projection.MyOrderProjection;
import com.vecanhac.ddd.domain.projection.MyTicketProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
        SELECT 
            oi.id AS orderItemId,
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
            oi.id AS orderItemId,
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

    @Query(value = """
    SELECT DISTINCT 
        o.id AS orderId,
        e.title AS eventTitle,
        e.address AS eventAddress,
        e.cover_image_url AS coverImageUrl,
        CONCAT(DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i'), ' đến ', DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i')) AS showingTime,
        o.total_amount AS totalPrice,
        o.status AS orderStatus,
        o.created_at AS createdAt
    FROM orders o
    JOIN order_items oi ON o.id = oi.order_id
    JOIN tickets t ON t.id = oi.ticket_id
    JOIN showings s ON s.id = t.showing_id
    JOIN events e ON e.id = s.event_id
    WHERE o.user_id = :userId
      AND (
        (:upcoming = true AND s.start_time > CURRENT_TIMESTAMP) OR
        (:upcoming = false AND s.start_time <= CURRENT_TIMESTAMP)
      )
    ORDER BY o.created_at DESC
""", nativeQuery = true)
    List<MyOrderProjection> findOrdersByUserAndUpcoming(
            @Param("userId") Long userId,
            @Param("upcoming") boolean upcoming
    );



    @Query("""
    SELECT DISTINCT 
        o.id AS orderId,
        e.title AS eventTitle,
        e.address AS eventAddress,
        e.coverImageUrl AS coverImageUrl,
        CONCAT(CONCAT(FUNCTION('DATE_FORMAT', s.startTime, '%Y-%m-%d %H:%i'), ' đến '), FUNCTION('DATE_FORMAT', s.endTime, '%Y-%m-%d %H:%i')) AS showingTime,
        o.totalAmount AS totalPrice,
        o.status AS orderStatus,
        o.createdAt AS createdAt
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
    List<MyOrderProjection> findOrdersByUserAndStatusAndUpcoming(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("upcoming") boolean upcoming
    );

}