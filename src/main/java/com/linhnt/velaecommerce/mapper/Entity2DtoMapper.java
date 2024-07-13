package com.linhnt.velaecommerce.mapper;

import com.linhnt.velaecommerce.dto.reponse.order.OrderResponseDto;
import com.linhnt.velaecommerce.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface Entity2DtoMapper {
    Entity2DtoMapper INSTANCE = Mappers.getMapper(Entity2DtoMapper.class);
    OrderResponseDto responseOrder(OrderEntity entity);
}
