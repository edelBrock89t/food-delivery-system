package com.food_delivery_system.order_service.dto;

import com.food_delivery_system.order_service.entity.order.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;

@Builder
public record OrderDTO(
        Long id,
        Long customerId,
        String address,
        BigDecimal totalAmount,
        String courierName,
        Integer etaMinutes,
        OrderStatus orderStatus,
        Set<OrderItemDTO> orderItemEntities
) {
}
