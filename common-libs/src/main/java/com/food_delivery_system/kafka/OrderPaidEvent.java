package com.food_delivery_system.kafka;

import com.food_delivery_system.http.payment.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderPaidEvent(
        Long orderId,
        Long paymentId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime paidDateTime
) {
}
