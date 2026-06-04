package com.food_delivery_system.http.payment;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
