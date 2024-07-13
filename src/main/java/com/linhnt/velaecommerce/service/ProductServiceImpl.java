package com.linhnt.velaecommerce.service;

import com.linhnt.velaecommerce.constant.ErrorCode;
import com.linhnt.velaecommerce.dto.request.product.FilterProductDto;
import com.linhnt.velaecommerce.entity.OrderEntity;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;
import com.linhnt.velaecommerce.exception.VeValidateException;
import com.linhnt.velaecommerce.repository.OrderRepository;
import com.linhnt.velaecommerce.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    protected ProductServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public List<ProductEntity> filter(FilterProductDto filterDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        if (Objects.equals(filterDto.getSortType(), "desc")) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(filterDto.getSortField())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(filterDto.getSortField())));
        }

        criteriaQuery.select(root)
                .where(this.buildPredicate(criteriaBuilder, root, filterDto));

        TypedQuery<ProductEntity> query = entityManager.createQuery(criteriaQuery);
        if (filterDto.getLimit() != null && filterDto.getPage() != null) {
            query.setFirstResult((filterDto.getPage() - 1) * filterDto.getLimit());
            query.setMaxResults(filterDto.getLimit());
        }

        return query.getResultList();
    }

    @Override
    public Long filterCount(FilterProductDto filterDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> root = countQuery.from(ProductEntity.class);

        countQuery.select(criteriaBuilder.count(root))
                .where(this.buildPredicate(criteriaBuilder, root, filterDto));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public ProductEntity findById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        Predicate idEqual = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.select(root).where(customCriteriaBuilder.and(root, idEqual));

        List<ProductEntity> findRes = entityManager.createQuery(criteriaQuery)
                .getResultList();

        return CollectionUtils.isEmpty(findRes) ? null : findRes.get(0);
    }

    @Override
    public List<ProductEntity> findAvailableByIds(List<Long> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        Predicate predicate = root.get("id").in(ids);

        criteriaQuery.select(root)
                .where(customCriteriaBuilder.and(root, predicate));
        TypedQuery<ProductEntity> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<ProductEntity> findByIds(List<Long> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);


        Predicate predicate = root.get("id").in(ids);

        criteriaQuery.select(root)
                .where(predicate);

        TypedQuery<ProductEntity> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public List<ProductEntity> saveAll(List<ProductEntity> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public void softDeleteById(Long id) throws VeNotFoundException {
        ProductEntity product = findById(id);
        if (product == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(0, 1);
        List<OrderEntity> results = orderRepository.findByProductId(id, pageable);
        if (!CollectionUtils.isEmpty(results)) {
            throw new VeValidateException(ErrorCode.DELETE_PRODUCT_DENIED_BY_ORDER, null);
        }

        product.setDeleted(true);
        productRepository.save(product);
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<ProductEntity> root, FilterProductDto filterDto) {
        Predicate finalCondition = null;
        if (StringUtils.hasText(filterDto.getKeyword())) {
            Predicate nameLike = criteriaBuilder.like(root.get("name"), "%" + filterDto.getKeyword() + "%");
            Predicate descriptionLike = criteriaBuilder.like(root.get("description"), "%" + filterDto.getKeyword() + "%");
            finalCondition = criteriaBuilder.or(nameLike, descriptionLike);
        }

        return customCriteriaBuilder.and(root, finalCondition);
    }
}
