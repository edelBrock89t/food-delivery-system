package com.food_delivery_system.order_service.converter;

import com.food_delivery_system.order_service.dto.OrderItemDTO;
import com.food_delivery_system.order_service.entity.order_item.OrderItemEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDTOConverter implements Converter<OrderItemEntity, OrderItemDTO> {

    @Override
    public OrderItemDTO convert(OrderItemEntity source) {
        return OrderItemDTO.builder()
                .id(source.getId())
                .itemId(source.getItemId())
                .quantity(source.getQuantity())
                .priceAtPurchase(source.getPriceAtPurchase())
                .build();
    }
}
