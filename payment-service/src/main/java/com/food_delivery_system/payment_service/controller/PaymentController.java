package com.food_delivery_system.payment_service.controller;

import com.food_delivery_system.http.payment.CreatePaymentRequest;
import com.food_delivery_system.http.payment.CreatePaymentResponse;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;
import com.food_delivery_system.payment_service.mapper.PaymentEntityMapper;
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
    private final PaymentEntityMapper paymentEntityMapper;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentEntityMapper paymentEntityMapper) {
        this.paymentService = paymentService;
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @PostMapping
    public CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Received request: paymentRequest={}", request);

        PaymentEntity savedPayment = paymentService.createPayment(request);
        return paymentEntityMapper.toCreatePaymentResponse(savedPayment);
    }
}
