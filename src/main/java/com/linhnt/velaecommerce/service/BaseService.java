package com.linhnt.velaecommerce.service;

import com.linhnt.velaecommerce.util.CustomCriteriaBuilderUtil;
import jakarta.persistence.EntityManager;

public abstract class BaseService {
    protected final EntityManager entityManager;
    protected final CustomCriteriaBuilderUtil customCriteriaBuilder;

    protected BaseService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customCriteriaBuilder = new CustomCriteriaBuilderUtil(entityManager.getCriteriaBuilder());
    }
}
