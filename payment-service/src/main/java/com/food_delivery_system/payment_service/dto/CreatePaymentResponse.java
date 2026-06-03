package com.food_delivery_system.payment_service.dto;

import com.food_delivery_system.payment_service.entity.payment.PaymentMethod;
import com.food_delivery_system.payment_service.entity.payment.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePaymentResponse(
        Long paymentId,
        PaymentStatus paymentStatus,
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
