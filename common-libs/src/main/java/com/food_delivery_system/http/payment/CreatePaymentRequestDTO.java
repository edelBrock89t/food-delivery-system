package com.food_delivery_system.http.payment;

import java.math.BigDecimal;

public record CreatePaymentRequestDTO (
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
