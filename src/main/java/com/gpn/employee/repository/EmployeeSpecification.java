package com.gpn.employee.repository;

import com.gpn.employee.model.EmployeeEntity;
import com.gpn.employee.model.dto.EmployeeFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EmployeeSpecification {

    public static Specification<EmployeeEntity> filter(final EmployeeFilterRequest request) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (request.firstName() != null) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), likePattern(request.firstName())));
            }
            if (request.lastName() != null) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), likePattern(request.lastName())));
            }
            if (request.phone() != null) {
                predicates.add(cb.like(cb.lower(root.get("phone")), likePattern(request.phone())));
            }
            if (request.email() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), likePattern(request.email())));
            }
            if (request.department() != null) {
                predicates.add(cb.like(cb.lower(root.get("department")), likePattern(request.department())));
            }
            if (request.status() != null) {
                predicates.add(cb.equal(root.get("status"), request.status()));
            }
            if (request.createdFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.createdFrom()));
            }
            if (request.createdTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.createdTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String likePattern(final String value) {
        return "%" + value.toLowerCase() + "%";
    }
}
