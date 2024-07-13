package com.linhnt.velaecommerce.dto.reponse.order;

import com.linhnt.velaecommerce.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;

    private String customerName;

    private String deliveryAddress;

    private String customerEmail;

    private String customerPhoneNumber;

    private Boolean deleted;

    private Date createdAt;

    private Date updatedAt;

    private Long createdBy;

    private Long updatedBy;

    private Constant.OrderStatus status;

    private BigDecimal paymentAmount;

    private List<OrderDetailResponseDto> orderDetails;
}
