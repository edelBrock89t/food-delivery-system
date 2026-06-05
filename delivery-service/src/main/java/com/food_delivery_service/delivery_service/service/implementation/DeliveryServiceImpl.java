package com.food_delivery_service.delivery_service.service.implementation;

import com.food_delivery_service.delivery_service.entity.DeliveryEntity;
import com.food_delivery_service.delivery_service.repository.DeliveryJpaRepository;
import com.food_delivery_service.delivery_service.service.DeliveryService;
import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import com.food_delivery_system.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Value("${delivery.kafka.topic.delivery-assigned}")
    private String deliveryAssignedTopic;
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate;

    @Autowired
    public DeliveryServiceImpl(KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate, DeliveryJpaRepository deliveryJpaRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.deliveryJpaRepository = deliveryJpaRepository;
    }

    @Override
    @Transactional
    public DeliveryEntity assignDelivery(OrderPaidEvent orderPaidEvent) {
        Long orderId = orderPaidEvent.orderId();

        Optional<DeliveryEntity> theDelivery = deliveryJpaRepository.findByOrderId(orderId);
        if(theDelivery.isPresent()) {
            log.info("Found order delivery has been already assigned: delivery={}", theDelivery.get());
            return theDelivery.get();
        }

        DeliveryEntity newDelivery = DeliveryEntity.builder()
                .orderId(orderPaidEvent.orderId())
                .courierName("courier-" + ThreadLocalRandom.current().nextInt(30))
                .etaMinutes(ThreadLocalRandom.current().nextInt(10, 45))
                .build();
        deliveryJpaRepository.save(newDelivery);
        return newDelivery;
    }

    @Override
    public void notifyClientAboutAssignedDelivery(DeliveryEntity theDelivery) {
        DeliveryAssignedEvent newEvent = new DeliveryAssignedEvent(
                theDelivery.getOrderId(),
                theDelivery.getCourierName(),
                theDelivery.getEtaMinutes()
        );
        kafkaTemplate.send(deliveryAssignedTopic, newEvent.orderId().toString(), newEvent);
    }
}
