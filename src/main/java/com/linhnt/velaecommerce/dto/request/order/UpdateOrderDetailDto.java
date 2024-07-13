package com.linhnt.velaecommerce.dto.request.order;

import com.linhnt.velaecommerce.entity.OrderDetailEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateOrderDetailDto {

    private Long id;

    @NotNull(message = "Product is required")
    private Boolean deleted;

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.0", message = "Payment amount must be positive")
    private BigDecimal price;

    private OrderDetailEntity oldOrderDetail;
}
