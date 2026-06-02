package com.food_delivery_system.order_service.repository;

import com.food_delivery_system.order_service.entity.order_item.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {
}
