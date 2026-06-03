package com.food_delivery_system.payment_service.controller;

import com.food_delivery_system.payment_service.dto.CreatePaymentRequest;
import com.food_delivery_system.payment_service.dto.CreatePaymentResponse;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import com.food_delivery_system.payment_service.entity.payment.PaymentStatus;
import com.food_delivery_system.payment_service.repository.PaymentJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentJpaRepository paymentJpaRepository;

    @Autowired
    public PaymentController(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @PostMapping
    public CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Received request: paymentRequest={}", request);

        PaymentEntity newPayment = PaymentEntity.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .paymentStatus(PaymentStatus.PAYMENT_CREATED)
                .paymentMethod(request.paymentMethod())
                .build();

        Optional<PaymentEntity> foundPayment = paymentJpaRepository.findByOrderId(request.orderId());
        if(foundPayment.isPresent()) {
            log.info("Payment already exists for orderId={}", request.orderId());
            return CreatePaymentResponse.builder()
                    .paymentId(foundPayment.get().getId())
                    .paymentStatus(foundPayment.get().getPaymentStatus())
                    .orderId(foundPayment.get().getOrderId())
                    .paymentMethod(foundPayment.get().getPaymentMethod())
                    .amount(foundPayment.get().getAmount())
                    .build();
        }

        PaymentEntity saved = paymentJpaRepository.save(newPayment);

        var response = CreatePaymentResponse.builder()
                .paymentId(saved.getId())
                .paymentStatus(saved.getPaymentStatus())
                .orderId(saved.getOrderId())
                .paymentMethod(saved.getPaymentMethod())
                .amount(saved.getAmount())
                .build();
        log.info("New payment has been created: {}", response);
        return response;
    }
}
