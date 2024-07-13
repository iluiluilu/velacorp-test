package com.linhnt.velaecommerce.dto.request.order;

import com.linhnt.velaecommerce.constant.Constant;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UpdateOrderDto {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "Customer phone number is required")
    private String customerPhoneNumber;

    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotNull(message = "Status is required")
    private Constant.OrderStatus status;

    @NotEmpty(message = "Order detail must not be empty")
    private List<UpdateOrderDetailDto> orderDetails;

    @AssertTrue(message = "The order must contains at least one product")
    public boolean isExistProduct() {
        List<UpdateOrderDetailDto> orderDetails = this.orderDetails.stream()
                .filter(s -> s.getDeleted() == null || !s.getDeleted())
                .toList();

        return !orderDetails.isEmpty();
    }

    @AssertTrue(message = "The order contains duplicate products")
    public boolean isDuplicateProduct() {
        List<UpdateOrderDetailDto> orderDetailDtos = this.orderDetails.stream()
                .filter(s -> s.getDeleted() == null || !s.getDeleted())
                .toList();
        Set<Long> productIds = orderDetailDtos.stream().map(UpdateOrderDetailDto::getProductId).collect(Collectors.toSet());
        return productIds.size() == orderDetailDtos.size();
    }
}
