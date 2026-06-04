package com.food_delivery_system.order_service.service.implementation;

import com.food_delivery_system.http.order.CreateOrderRequest;
import com.food_delivery_system.http.order.OrderStatus;
import com.food_delivery_system.http.payment.CreatePaymentRequest;
import com.food_delivery_system.http.payment.CreatePaymentResponse;
import com.food_delivery_system.http.payment.PaymentStatus;
import com.food_delivery_system.order_service.dto.OrderPaymentRequest;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import com.food_delivery_system.order_service.entity.order_item.OrderItemEntity;
import com.food_delivery_system.order_service.external.PaymentHttpClient;
import com.food_delivery_system.order_service.repository.OrderJpaRepository;
import com.food_delivery_system.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final PaymentHttpClient paymentHttpClient;

    @Autowired
    public OrderServiceImpl(OrderJpaRepository orderJpaRepository, PaymentHttpClient paymentHttpClient) {
        this.orderJpaRepository = orderJpaRepository;
        this.paymentHttpClient = paymentHttpClient;
    }

    @Override
    @Transactional
    public OrderEntity createOrder(CreateOrderRequest request) {
        log.info("Creating order: request={}", request);

        OrderEntity newOrder = OrderEntity.builder()
                .customerId(request.customerId())
                .address(request.address())
                .totalAmount(BigDecimal.ZERO)
                .courierName(null)
                .etaMinutes(null)
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .orderItemEntities(null)
                .build();

        Set<OrderItemEntity> orderItems = request.items().stream().map(reqItem ->
                OrderItemEntity.builder()
                        .itemId(reqItem.itemId())
                        .itemName(reqItem.itemName())
                        .quantity(reqItem.quantity())
                        .priceAtPurchase(null)
                        .order(newOrder)
                        .build()).collect(Collectors.toSet());
        newOrder.setOrderItemEntities(orderItems);

        calculatePricingForOrder(newOrder);

        return orderJpaRepository.save(newOrder);
    }

    @Override
    public OrderEntity getOrderOrThrow(Long id) {
        log.info("Retrieving order with id: {}", id);

        return orderJpaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id {" + id + "} was not found"));
    }

    private void calculatePricingForOrder(OrderEntity orderEntity) {

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

    @Override
    @Transactional
    public OrderEntity processPayment(Long orderId, OrderPaymentRequest request) {
        log.info("Paying order with id={}, request={}", orderId, request);

        OrderEntity theOrder = getOrderOrThrow(orderId);
        if (!theOrder.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT)) {
            throw new RuntimeException("Order must be in status PENDING_PAYMENT");
        }

        CreatePaymentResponse paymentResponse = paymentHttpClient.createPayment(new CreatePaymentRequest(orderId, request.paymentMethod(), theOrder.getTotalAmount()));
        OrderStatus orderStatus = paymentResponse.paymentStatus().equals(PaymentStatus.PAYMENT_SUCCEEDED) ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED;
        log.info("Payment for orderId {} has been proceeded with status {}", orderId, orderStatus);

        theOrder.setOrderStatus(orderStatus);
        return orderJpaRepository.save(theOrder);
    }
}
