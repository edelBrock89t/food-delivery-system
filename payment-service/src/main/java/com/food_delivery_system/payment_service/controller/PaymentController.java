package com.food_delivery_system.payment_service.controller;

import com.food_delivery_system.payment_service.dto.CreatePaymentRequest;
import com.food_delivery_system.payment_service.dto.CreatePaymentResponse;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import com.food_delivery_system.payment_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Received request: paymentRequest={}", request);

        PaymentEntity savedPayment = paymentService.createPayment(request);
        return CreatePaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .paymentStatus(savedPayment.getPaymentStatus())
                .orderId(savedPayment.getOrderId())
                .paymentMethod(savedPayment.getPaymentMethod())
                .amount(savedPayment.getAmount())
                .build();
    }
}
