package com.food_delivery_system.http.order;

public record CreateOrderItemRequestDTO (
        Long itemId,
        Integer quantity,
        String itemName
) {
}

