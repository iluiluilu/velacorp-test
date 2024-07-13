package com.linhnt.velaecommerce.service;

import com.linhnt.velaecommerce.dto.request.product.FilterProductDto;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;

import java.util.List;

public interface ProductService {
    List<ProductEntity> filter(FilterProductDto filterDto);

    Long filterCount(FilterProductDto filterDto);

    ProductEntity findById(Long id);

    List<ProductEntity> findByIds(List<Long> ids);

    List<ProductEntity> findAvailableByIds(List<Long> ids);

    ProductEntity save(ProductEntity product);

    List<ProductEntity> saveAll(List<ProductEntity> products);

    void softDeleteById(Long id) throws VeNotFoundException;
}
