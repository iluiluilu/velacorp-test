package com.linhnt.velaecommerce.controller;

import com.linhnt.velaecommerce.constant.Constant;
import com.linhnt.velaecommerce.constant.ErrorCode;
import com.linhnt.velaecommerce.dto.reponse.CommonResponseDto;
import com.linhnt.velaecommerce.dto.reponse.ListResponseDto;
import com.linhnt.velaecommerce.dto.request.order.*;
import com.linhnt.velaecommerce.entity.OrderDetailEntity;
import com.linhnt.velaecommerce.entity.OrderEntity;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;
import com.linhnt.velaecommerce.exception.VeValidateException;
import com.linhnt.velaecommerce.mapper.Dto2EntityMapper;
import com.linhnt.velaecommerce.mapper.Entity2DtoMapper;
import com.linhnt.velaecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public CommonResponseDto filterOrders(@ModelAttribute FilterOrderDto filterDto) {
        ListResponseDto<OrderEntity> response = new ListResponseDto<>();
        response.setItems(orderService.filter(filterDto));
        response.setTotal(orderService.filterCount(filterDto));
        return CommonResponseDto.builder().data(response).build();
    }

    @GetMapping("/{id}")
    public CommonResponseDto getOrderById(@PathVariable("id") Long id) {
        OrderEntity order = orderService.findById(id);
        if (order == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }
        return CommonResponseDto.builder().data(order).build();
    }

    @PostMapping
    public CommonResponseDto createOrder(@RequestBody @Validated CreateOrderDto orderDto) {
        Map<Long, ProductEntity> updateProducts = orderService.validateCreateOrderProduct(orderDto.getOrderDetails());
        OrderEntity order = Dto2EntityMapper.INSTANCE.createOrder(orderDto);
        order.setStatus(Constant.OrderStatus.NEW);

        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (CreateOrderDetailDto orderDetailDto : orderDto.getOrderDetails()) {
            OrderDetailEntity orderDetailEntity = Dto2EntityMapper.INSTANCE.createOrderDetail(orderDetailDto);
            orderDetailEntity.setProduct(updateProducts.get(orderDetailDto.getProductId()));
            orderDetailEntity.setOrder(order);
            orderDetails.add(orderDetailEntity);
        }
        order.setOrderDetails(orderDetails);
        order.setPaymentAmount();
        OrderEntity createdOrder = orderService.save(order, new ArrayList<>(updateProducts.values()));
        return CommonResponseDto.builder().data(Entity2DtoMapper.INSTANCE.responseOrder(createdOrder)).build();
    }

    @PutMapping("/{id}")
    public CommonResponseDto updateOrder(@PathVariable("id") Long id, @RequestBody @Validated UpdateOrderDto orderDto) {
        OrderEntity order = orderService.findById(id);
        if (order == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }

        if (!order.getStatus().equals(Constant.OrderStatus.NEW)) {
            throw new VeValidateException(ErrorCode.UPDATE_ORDER_DENIED_BY_STATUS, null);
        }

        Map<Long, ProductEntity> updateProducts = orderService.validateUpdateOrderProduct(
                orderDto.getOrderDetails(),
                order.getOrderDetails().stream().toList()
        );

        order = Dto2EntityMapper.INSTANCE.updateOrder(orderDto);
        order.setId(id);
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (UpdateOrderDetailDto orderDetailDto : orderDto.getOrderDetails()) {
            OrderDetailEntity orderDetailEntity = Dto2EntityMapper.INSTANCE.updateOrderDetail(orderDetailDto);
            orderDetailEntity.setProduct(updateProducts.get(orderDetailDto.getProductId()));
            orderDetailEntity.setOrder(order);
            orderDetails.add(orderDetailEntity);
        }

        order.setOrderDetails(orderDetails);
        order.setPaymentAmount();
        return CommonResponseDto.builder().data(orderService.save(order, new ArrayList<>(updateProducts.values()))).build();
    }
}
