package com.linhnt.velaecommerce.mapper;

import com.linhnt.velaecommerce.dto.request.order.CreateOrderDetailDto;
import com.linhnt.velaecommerce.dto.request.order.CreateOrderDto;
import com.linhnt.velaecommerce.dto.request.order.UpdateOrderDetailDto;
import com.linhnt.velaecommerce.dto.request.order.UpdateOrderDto;
import com.linhnt.velaecommerce.dto.request.product.CreateProductDto;
import com.linhnt.velaecommerce.dto.request.product.UpdateProductDto;
import com.linhnt.velaecommerce.entity.OrderDetailEntity;
import com.linhnt.velaecommerce.entity.OrderEntity;
import com.linhnt.velaecommerce.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface Dto2EntityMapper {
    Dto2EntityMapper INSTANCE = Mappers.getMapper(Dto2EntityMapper.class);

    ProductEntity createProduct(CreateProductDto dto);

    ProductEntity updateProduct(UpdateProductDto dto);

    @Mapping(target = "orderDetails", ignore = true)
    OrderEntity createOrder(CreateOrderDto dto);

    @Mapping(target = "orderDetails", ignore = true)
    OrderEntity updateOrder(UpdateOrderDto dto);

    OrderDetailEntity createOrderDetail(CreateOrderDetailDto dtos);

    OrderDetailEntity updateOrderDetail(UpdateOrderDetailDto dtos);
}
