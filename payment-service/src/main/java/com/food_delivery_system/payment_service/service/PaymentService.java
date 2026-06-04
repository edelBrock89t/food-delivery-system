package com.food_delivery_system.payment_service.service;

import com.food_delivery_system.http.payment.CreatePaymentRequest;
import com.food_delivery_system.payment_service.entity.payment.PaymentEntity;

public interface PaymentService {

    PaymentEntity createPayment(CreatePaymentRequest createPaymentRequest);
}
