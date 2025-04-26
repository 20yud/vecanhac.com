package com.vecanhac.ddd.infrastructure.persistence.repository.event;

import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EventEntity> searchByKeyword(String keyword) {
        String jpql = "SELECT e FROM EventEntity e WHERE e.title LIKE :keyword";
        TypedQuery<EventEntity> query = entityManager.createQuery(jpql, EventEntity.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
}