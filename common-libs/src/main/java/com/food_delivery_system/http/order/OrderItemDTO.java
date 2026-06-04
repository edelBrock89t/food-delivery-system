package com.food_delivery_system.http.order;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long id,
        Long itemId,
        Integer quantity,
        BigDecimal priceAtPurchase
) {
}
