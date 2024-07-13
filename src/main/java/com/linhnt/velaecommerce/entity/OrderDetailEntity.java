package com.linhnt.velaecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_details")
public class OrderDetailEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_details_id_seq")
    @SequenceGenerator(name = "order_details_id_seq", sequenceName = "order_details_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

    public BigDecimal getTotalPrice() {
        if (this.totalPrice == null) {
            this.totalPrice = product.getPrice().multiply(new BigDecimal(quantity));
        }
        return this.totalPrice;
    }
}
