package com.food_delivery_system.order_service.dto;

import java.util.Set;

public record CreateOrderRequest(
        Long customerId,
        String address,
        Set<CreateOrderItemRequest> items
) {
}
