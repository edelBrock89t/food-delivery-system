package com.food_delivery_system.order_service.config;

import com.food_delivery_system.kafka.OrderPaidEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, OrderPaidEvent> producerFactory(JsonMapper jsonMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        JacksonJsonSerializer<OrderPaidEvent> serializer = new JacksonJsonSerializer<>(jsonMapper);
        serializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, OrderPaidEvent> kafkaTemplate(ProducerFactory<String, OrderPaidEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
