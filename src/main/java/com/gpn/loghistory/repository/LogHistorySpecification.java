package com.gpn.loghistory.repository;

import com.gpn.loghistory.model.LogHistoryEntity;
import com.gpn.loghistory.model.dto.LogHistoryFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LogHistorySpecification {

    public static Specification<LogHistoryEntity> filter(final LogHistoryFilterRequest request) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (request.userId() != null) {
                predicates.add(cb.equal(root.get("userId"), request.userId()));
            }
            if (request.module() != null) {
                predicates.add(cb.equal(root.get("module"), request.module()));
            }
            if (request.objectId() != null) {
                predicates.add(cb.equal(root.get("objectId"), request.objectId()));
            }
            if (request.action() != null) {
                predicates.add(cb.equal(root.get("action"), request.action()));
            }
            if (request.dateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.dateFrom()));
            }
            if (request.dateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.dateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
