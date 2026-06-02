package com.food_delivery_system.order_service.converter;

import com.food_delivery_system.order_service.dto.OrderDTO;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDTOConverter implements Converter<OrderEntity, OrderDTO> {

    private final OrderItemDTOConverter orderItemDTOConverter;

    @Autowired
    public OrderDTOConverter(OrderItemDTOConverter orderItemDTOConverter) {
        this.orderItemDTOConverter = orderItemDTOConverter;
    }

    @Override
    public OrderDTO convert(OrderEntity source) {
        return OrderDTO.builder()
                .id(source.getId())
                .customerId(source.getCustomerId())
                .address(source.getAddress())
                .totalAmount(source.getTotalAmount())
                .courierName(source.getCourierName())
                .etaMinutes(source.getEtaMinutes())
                .orderStatus(source.getOrderStatus())
                .orderItemEntities(source.getOrderItemEntities().stream().map(orderItemDTOConverter::convert).collect(Collectors.toSet()))
                .build();
    }
}
