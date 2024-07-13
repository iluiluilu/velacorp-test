package com.linhnt.velaecommerce.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linhnt.velaecommerce.constant.Constant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    @SequenceGenerator(name = "orders_id_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Constant.OrderStatus status;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetailEntity> orderDetails;

    public BigDecimal getPaymentAmount() {
        if (orderDetails.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            total = total.add(orderDetailEntity.getTotalPrice());
        }

        return total;
    }

    public void setPaymentAmount() {
        if (CollectionUtils.isEmpty(this.orderDetails)) {
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            total = total.add(orderDetailEntity.getTotalPrice());
        }
        this.paymentAmount = total;
    }
}
