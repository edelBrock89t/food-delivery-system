package com.food_delivery_system.http.payment;

import java.math.BigDecimal;

public record CreatePaymentResponseDTO (
        Long paymentId,
        PaymentStatus paymentStatus,
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
