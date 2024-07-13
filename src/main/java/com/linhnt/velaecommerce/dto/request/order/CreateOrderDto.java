package com.linhnt.velaecommerce.dto.request.order;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateOrderDto {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "Customer phone number is required")
    private String customerPhoneNumber;

    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotEmpty(message = "The order must contains at least one product")
    private List<CreateOrderDetailDto> orderDetails;

    @AssertTrue(message = "The order contains duplicate products")
    public boolean isDuplicateProduct() {
        Set<Long> productIds = orderDetails.stream().map(CreateOrderDetailDto::getProductId).collect(Collectors.toSet());
        return productIds.size() == orderDetails.size();
    }
}
