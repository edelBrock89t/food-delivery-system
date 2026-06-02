package com.food_delivery_system.order_service.service;

import com.food_delivery_system.order_service.dto.CreateOrderRequest;
import com.food_delivery_system.order_service.entity.order.OrderEntity;

public interface OrderService {

    OrderEntity createOrder(CreateOrderRequest createOrderRequest);
    OrderEntity getOrderOrThrow(Long id);
}
