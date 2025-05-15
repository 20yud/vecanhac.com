package com.vecanhac.ddd.infrastructure.persistence.repository.event.myevents;

import com.vecanhac.ddd.domain.event.EventEntity;
import com.vecanhac.ddd.domain.event.myevents.MyEventRepositoryCustom;
import com.vecanhac.ddd.domain.event.myevents.MyEventSearchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyEventRepositoryImpl implements MyEventRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<EventEntity> searchMyEvents(MyEventSearchFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = cq.from(EventEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getOrganizerId() != null) {
            predicates.add(cb.equal(root.get("organizerId"), filter.getOrganizerId()));
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String likePattern = "%" + filter.getKeyword().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("title")), likePattern));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getUpcoming() != null) {
            LocalDateTime now = LocalDateTime.now();
            if (filter.getUpcoming()) {
                predicates.add(cb.greaterThan(root.get("startTime"), now));
            } else {
                predicates.add(cb.lessThan(root.get("endTime"), now));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("createdAt")));

        return em.createQuery(cq).getResultList();
    }
}
