package com.food_delivery_system.http.order;

import java.util.Set;

public record CreateOrderRequestDTO (
        Long customerId,
        String address,
        Set<CreateOrderItemRequestDTO> items
) {
}
