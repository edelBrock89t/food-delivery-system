package com.food_delivery_system.http.order;

import java.util.Set;

public record CreateOrderRequest(
        Long customerId,
        String address,
        Set<CreateOrderItemRequest> items
) {
}
