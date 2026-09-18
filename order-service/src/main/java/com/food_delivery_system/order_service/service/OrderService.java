package com.food_delivery_system.order_service.service;

import com.food_delivery_system.http.order.CreateOrderRequestDTO;
import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import com.food_delivery_system.order_service.dto.OrderPaymentRequest;
import com.food_delivery_system.order_service.entity.order.OrderEntity;

public interface OrderService {

    OrderEntity createOrder(CreateOrderRequestDTO createOrderRequest);
    OrderEntity getOrderOrThrow(Long id);
    OrderEntity processPayment(Long orderId, OrderPaymentRequest request);
    void assignDeliveryToOrder(Long orderId, DeliveryAssignedEvent deliveryAssignedEvent);
}
