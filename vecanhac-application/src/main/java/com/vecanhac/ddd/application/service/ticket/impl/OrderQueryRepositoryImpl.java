package com.vecanhac.ddd.application.service.ticket.impl;

import com.vecanhac.ddd.application.dto.ticket.MyTicketDTO;
import com.vecanhac.ddd.application.service.ticket.OrderQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MyTicketDTO> findMyTicketsByUserId(Long userId) {
        return em.createQuery("""
            SELECT new com.vecanhac.ddd.application.dto.ticket.MyTicketDTO(
                o.id,
                e.title,
                CONCAT(
                    FUNCTION('DATE_FORMAT', s.startTime, '%d-%m-%Y %H:%i'), ' - ',
                    FUNCTION('DATE_FORMAT', s.endTime, '%H:%i')
                ),
                t.name,
                oi.quantity,
                (double)(oi.priceEach * oi.quantity),
                o.status
            )
            FROM OrderEntity o
            JOIN o.orderItems oi
            JOIN oi.ticket t
            JOIN t.showing s
            JOIN s.event e
            WHERE o.userId = :userId
            ORDER BY o.createdAt DESC
            """, MyTicketDTO.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}