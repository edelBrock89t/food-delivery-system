package com.food_delivery_system.order_service.converter;

import com.food_delivery_system.http.order.CreateOrderRequest;
import com.food_delivery_system.http.order.OrderDTO;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrderEntityMapper {

    @Mapping(target = "orderItemEntities", source = "items")
    OrderEntity toEntity(CreateOrderRequest createOrderRequest);

    @AfterMapping
    default void linkOrderItemEntities(@MappingTarget OrderEntity orderEntity) {
        orderEntity.getOrderItemEntities().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
    }

    OrderDTO toOrderDTO(OrderEntity orderEntity);
}
