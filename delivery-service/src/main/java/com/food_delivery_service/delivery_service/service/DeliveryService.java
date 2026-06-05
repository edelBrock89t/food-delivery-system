package com.food_delivery_service.delivery_service.service;

import com.food_delivery_service.delivery_service.entity.DeliveryEntity;
import com.food_delivery_system.kafka.OrderPaidEvent;

public interface DeliveryService {

    DeliveryEntity assignDelivery(OrderPaidEvent orderPaidEvent);
    void notifyClientAboutAssignedDelivery(DeliveryEntity theDelivery);
}
