package com.food_delivery_system.payment_service.dto;

import com.food_delivery_system.payment_service.entity.payment.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
