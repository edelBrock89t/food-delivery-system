package com.food_delivery_system.order_service.service;

import com.food_delivery_system.http.order.CreateOrderRequestDTO;
import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import com.food_delivery_system.order_service.dto.OrderPaymentRequest;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import com.food_delivery_system.order_service.errors.OrderIsNotInPendingPaymentStatusException;
import com.food_delivery_system.order_service.errors.OrderNotFoundException;
import com.food_delivery_system.order_service.errors.PaymentFailedException;
import com.food_delivery_system.order_service.errors.PaymentServiceUnavailableException;

public interface OrderService {

    OrderEntity createOrder(CreateOrderRequestDTO createOrderRequest);
    OrderEntity getOrderOrThrow(Long id);
    OrderEntity processPayment(Long orderId, OrderPaymentRequest request) throws OrderNotFoundException, PaymentServiceUnavailableException, PaymentFailedException, OrderIsNotInPendingPaymentStatusException;
    void assignDeliveryToOrder(Long orderId, DeliveryAssignedEvent deliveryAssignedEvent);
}
