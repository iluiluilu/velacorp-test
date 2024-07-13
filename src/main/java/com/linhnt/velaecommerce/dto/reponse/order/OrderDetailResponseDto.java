package com.linhnt.velaecommerce.dto.reponse.order;

import com.linhnt.velaecommerce.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDetailResponseDto {
    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private ProductEntity product;
}
