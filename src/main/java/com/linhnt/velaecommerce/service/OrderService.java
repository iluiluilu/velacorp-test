package com.linhnt.velaecommerce.service;

import com.linhnt.velaecommerce.dto.request.order.CreateOrderDetailDto;
import com.linhnt.velaecommerce.dto.request.order.FilterOrderDto;
import com.linhnt.velaecommerce.dto.request.order.UpdateOrderDetailDto;
import com.linhnt.velaecommerce.entity.OrderDetailEntity;
import com.linhnt.velaecommerce.entity.OrderEntity;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderEntity> filter(FilterOrderDto filterDto);

    Long filterCount(FilterOrderDto filterDto);

    OrderEntity findById(Long id);

    OrderEntity save(OrderEntity order, List<ProductEntity> products);

    OrderEntity saveOrder(OrderEntity order, List<ProductEntity> products);

    void softDeleteById(Long id) throws VeNotFoundException;

    Map<Long, ProductEntity> validateCreateOrderProduct(List<CreateOrderDetailDto> orderDetailDtos);

    Map<Long, ProductEntity> validateUpdateOrderProduct(List<UpdateOrderDetailDto> updateOrderDetailDtos, List<OrderDetailEntity> orderDetailEntities);
}
