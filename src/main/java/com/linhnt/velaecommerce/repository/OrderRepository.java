package com.linhnt.velaecommerce.repository;

import com.linhnt.velaecommerce.entity.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o JOIN o.orderDetails od  WHERE od.product.id = :productId AND od.deleted = false AND o.deleted = false")
    List<OrderEntity> findByProductId(@Param("productId") Long productId, Pageable pageable);
}
