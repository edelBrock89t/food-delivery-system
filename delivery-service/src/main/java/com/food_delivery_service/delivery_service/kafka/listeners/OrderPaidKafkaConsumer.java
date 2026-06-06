package com.food_delivery_service.delivery_service.kafka.listeners;

import com.food_delivery_service.delivery_service.service.DeliveryService;
import com.food_delivery_system.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderPaidKafkaConsumer {

    private final DeliveryService deliveryService;

    @Autowired
    public OrderPaidKafkaConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = "${order.kafka.topic.order-paid}", groupId = "order-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void orderPaidEventConsumerListener(ConsumerRecord<String, OrderPaidEvent> consumerRecord) {
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.topic());
        log.info("Received OrderPaidEvent from Kafka topic: {}", consumerRecord.value());

        var newDelivery = deliveryService.assignDelivery(consumerRecord.value());
        log.info("Delivery for orderId={} has been successfully assigned, and will be finished in {} minutes", newDelivery.getOrderId(), newDelivery.getEtaMinutes());
        deliveryService.notifyClientAboutAssignedDelivery(newDelivery);
    }
}
