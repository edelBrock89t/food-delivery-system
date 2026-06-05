package com.food_delivery_service.delivery_service.repository;

import com.food_delivery_service.delivery_service.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {
    Optional<DeliveryEntity> findByOrderId(Long orderId);
}
