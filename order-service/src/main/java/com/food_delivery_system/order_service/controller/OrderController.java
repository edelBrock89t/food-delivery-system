package com.food_delivery_system.order_service.controller;

import com.food_delivery_system.http.order.CreateOrderRequestDTO;
import com.food_delivery_system.http.order.OrderDTO;
import com.food_delivery_system.order_service.converter.OrderEntityMapper;
import com.food_delivery_system.order_service.dto.OrderPaymentRequest;
import com.food_delivery_system.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderEntityMapper orderEntityMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderEntityMapper orderEntityMapper) {
        this.orderService = orderService;
        this.orderEntityMapper = orderEntityMapper;
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody CreateOrderRequestDTO createOrderRequest) {
        return orderEntityMapper.toOrderDTO(orderService.createOrder(createOrderRequest));
    }

    @GetMapping("/{id}")
    public OrderDTO getOneOrder(@PathVariable("id") Long id) {
        return orderEntityMapper.toOrderDTO(orderService.getOrderOrThrow(id));
    }

    @PostMapping("/{id}/pay")
    public OrderDTO processPayment(@PathVariable("id") Long orderId, @RequestBody OrderPaymentRequest request) {
        return orderEntityMapper.toOrderDTO(orderService.processPayment(orderId, request));
    }
}
