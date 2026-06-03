package com.food_delivery_system.payment_service.repository;

import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
