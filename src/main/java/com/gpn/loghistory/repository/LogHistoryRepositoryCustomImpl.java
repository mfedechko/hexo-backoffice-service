package com.gpn.loghistory.repository;

import com.gpn.loghistory.model.LogHistoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogHistoryRepositoryCustomImpl implements LogHistoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> findIds(final Specification<LogHistoryEntity> spec, final Sort sort) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<LogHistoryEntity> root = query.from(LogHistoryEntity.class);

        query.select(root.get("id"));
        query.where(spec.toPredicate(root, query, cb));
        query.orderBy(QueryUtils.toOrders(sort, root, cb));

        return entityManager.createQuery(query).getResultList();
    }
}
