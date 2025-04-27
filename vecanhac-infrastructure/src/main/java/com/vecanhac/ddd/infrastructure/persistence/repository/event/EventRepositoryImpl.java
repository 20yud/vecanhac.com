package com.vecanhac.ddd.infrastructure.persistence.repository.event;

import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.EventRepositoryCustom;
import com.vecanhac.ddd.domain.event.EventSearchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<EventEntity> searchEvents(EventSearchFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);
        Root<EventEntity> event = cq.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(event.get("status"), "PUBLISHED"));

        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            Predicate titlePredicate = cb.like(event.get("title"), "%" + filter.getKeyword() + "%");
            Predicate venuePredicate = cb.like(event.get("venue"), "%" + filter.getKeyword() + "%");
            predicates.add(cb.or(titlePredicate, venuePredicate));
        }

        if (filter.getCity() != null && !filter.getCity().isBlank()) {
            predicates.add(cb.like(event.get("address"), "%" + filter.getCity() + "%"));
        }

        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("startTime"), filter.getStartDate().atStartOfDay()));
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(
                    event.get("startTime"),
                    filter.getStartDate().atStartOfDay(),
                    filter.getEndDate().atTime(23, 59, 59)
            ));
        }

        if (Boolean.TRUE.equals(filter.getFreeOnly())) {
            predicates.add(cb.equal(event.get("minTicketPrice"), 0));
        }

        if (filter.getCategoryId() != null) {
            predicates.add(cb.equal(event.get("categoryId"), filter.getCategoryId()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(event.get("startTime")));

        TypedQuery<EventEntity> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<EventEntity> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, resultList.size());
    }
}