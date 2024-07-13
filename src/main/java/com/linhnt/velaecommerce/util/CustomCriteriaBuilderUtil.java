package com.linhnt.velaecommerce.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CustomCriteriaBuilderUtil {
    private final CriteriaBuilder criteriaBuilder;

    public CustomCriteriaBuilderUtil(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    public Predicate buildDeletedCondition(Root<?> root) {
        return criteriaBuilder.equal(root.get("deleted"), false);
    }

    public Predicate and(Root<?> root, Predicate predicate) {
        Predicate deleted = buildDeletedCondition(root);
        if (predicate != null) {
            return criteriaBuilder.and(deleted, predicate);
        }
        return criteriaBuilder.and(deleted);
    }
}
