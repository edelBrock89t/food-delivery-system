package com.food_delivery_system.payment_service.service.implementation;

import com.food_delivery_system.payment_service.dto.CreatePaymentRequest;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import com.food_delivery_system.payment_service.entity.payment.PaymentStatus;
import com.food_delivery_system.payment_service.repository.PaymentJpaRepository;
import com.food_delivery_system.payment_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentJpaRepository paymentJpaRepository;

    @Autowired
    public PaymentServiceImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public PaymentEntity createPayment(CreatePaymentRequest request) {

        PaymentEntity newPayment = PaymentEntity.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .paymentStatus(PaymentStatus.PAYMENT_CREATED)
                .paymentMethod(request.paymentMethod())
                .build();

        Optional<PaymentEntity> foundPayment = paymentJpaRepository.findByOrderId(request.orderId());
        if (foundPayment.isPresent()) {
            log.info("Payment already exists for orderId={}", request.orderId());
            return foundPayment.get();
        }

        PaymentEntity savedPayment = paymentJpaRepository.save(newPayment);
        log.info("New payment has been created with id: {}", savedPayment.getId());
        return savedPayment;
    }
}
