package com.food_delivery_system.order_service.controller;

import com.food_delivery_system.http.order.CreateOrderRequest;
import com.food_delivery_system.http.order.OrderDTO;
import com.food_delivery_system.order_service.converter.OrderDTOConverter;
import com.food_delivery_system.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderDTOConverter orderDTOConverter;

    @Autowired
    public OrderController(OrderService orderService, OrderDTOConverter orderDTOConverter) {
        this.orderService = orderService;
        this.orderDTOConverter = orderDTOConverter;
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderDTOConverter.convert(orderService.createOrder(createOrderRequest));
    }

    @GetMapping("/{id}")
    public OrderDTO getOneOrder(@PathVariable("id") Long id) {
        return orderDTOConverter.convert(orderService.getOrderOrThrow(id));
    }
}
