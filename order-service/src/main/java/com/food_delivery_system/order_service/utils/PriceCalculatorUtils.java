package com.food_delivery_system.order_service.utils;

import com.food_delivery_system.order_service.entity.order.OrderEntity;
import com.food_delivery_system.order_service.entity.order_item.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PriceCalculatorUtils {

    public static void calculatePricingForOrder(OrderEntity orderEntity) {

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(OrderItemEntity item : orderEntity.getOrderItemEntities()) {
            double randomPrice = ThreadLocalRandom.current().nextDouble(100, 5000);
            item.setPriceAtPurchase(BigDecimal.valueOf(randomPrice));

            totalPrice = item.getPriceAtPurchase()
                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    .add(totalPrice);
        }

        orderEntity.setTotalAmount(totalPrice);
    }
}
