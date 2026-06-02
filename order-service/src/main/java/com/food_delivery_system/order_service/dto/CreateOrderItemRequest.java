package com.food_delivery_system.order_service.dto;

public record CreateOrderItemRequest(
        Long itemId,
        Integer quantity,
        String itemName
) {
}
