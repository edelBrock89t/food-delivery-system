package com.food_delivery_system.http.order;

public record CreateOrderItemRequest(
        Long itemId,
        Integer quantity,
        String itemName
) {
}

