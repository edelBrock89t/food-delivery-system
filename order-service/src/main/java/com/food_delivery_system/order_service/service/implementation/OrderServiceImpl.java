package com.food_delivery_system.order_service.service.implementation;

import com.food_delivery_system.grpc.order_payment.CreatePaymentRequest;
import com.food_delivery_system.grpc.order_payment.CreatePaymentResponse;
import com.food_delivery_system.grpc.order_payment.PaymentMethod;
import com.food_delivery_system.grpc.order_payment.PaymentStatus;
import com.food_delivery_system.http.order.CreateOrderRequestDTO;
import com.food_delivery_system.http.order.OrderStatus;
import com.food_delivery_system.kafka.DeliveryAssignedEvent;
import com.food_delivery_system.order_service.converter.PaymentMapper;
import com.food_delivery_system.order_service.dto.OrderPaymentRequest;
import com.food_delivery_system.order_service.entity.order.OrderEntity;
import com.food_delivery_system.order_service.entity.order_item.OrderItemEntity;
import com.food_delivery_system.order_service.gRPC.client.PaymentServiceClient;
import com.food_delivery_system.order_service.kafka.producer.OrderKafkaProducer;
import com.food_delivery_system.order_service.repository.OrderJpaRepository;
import com.food_delivery_system.order_service.service.OrderService;
import com.food_delivery_system.order_service.utils.PriceCalculatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderKafkaProducer orderKafkaProducer;
    private final PaymentServiceClient paymentServiceClient;
    private final PaymentMapper paymentMapper;

    @Autowired
    public OrderServiceImpl(OrderJpaRepository orderJpaRepository, OrderKafkaProducer orderKafkaProducer, PaymentServiceClient paymentServiceClient, PaymentMapper paymentMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderKafkaProducer = orderKafkaProducer;
        this.paymentServiceClient = paymentServiceClient;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public OrderEntity createOrder(CreateOrderRequestDTO request) {
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

        PriceCalculatorUtils.calculatePricingForOrder(newOrder);

        return orderJpaRepository.save(newOrder);
    }

    @Override
    public OrderEntity getOrderOrThrow(Long id) {
        log.info("Retrieving order with id: {}", id);

        return orderJpaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id {" + id + "} was not found"));
    }

    @Override
    @Transactional
    public OrderEntity processPayment(Long orderId, OrderPaymentRequest request) {
        log.info("Paying order with id={}, request={}", orderId, request);

        OrderEntity theOrder = getOrderOrThrow(orderId);
        if (!theOrder.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT)) {
            throw new RuntimeException("Order must be in status PENDING_PAYMENT");
        }

        CreatePaymentResponse paymentResponse = paymentServiceClient.createPayment(
                CreatePaymentRequest.newBuilder()
                        .setOrderId(orderId)
                        .setPaymentMethod(PaymentMethod.valueOf(request.paymentMethod().name()))
                        .setAmount(theOrder.getTotalAmount().longValue())
                        .build()
        );
        OrderStatus orderStatus = paymentResponse.getPaymentStatus().equals(PaymentStatus.PAYMENT_SUCCEEDED) ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED;

        theOrder.setOrderStatus(orderStatus);
        OrderEntity saved = orderJpaRepository.save(theOrder);

        orderKafkaProducer.sendOrderPaidEvent(saved, paymentMapper.toDomain(paymentResponse));
        return saved;
    }

    @Override
    @Transactional
    public void assignDeliveryToOrder(Long orderId, DeliveryAssignedEvent event) {
        OrderEntity theOrder = orderJpaRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong. Order with orderId={" + orderId + "} not found"));
        if (theOrder.getOrderStatus().equals(OrderStatus.PENDING_DELIVERY)) {
            log.info("The Order with orderId={} already has assigned delivery", orderId);
            return;
        }
        theOrder.setOrderStatus(OrderStatus.PENDING_DELIVERY);
        theOrder.setCourierName(event.courierName());
        theOrder.setEtaMinutes(event.etaMinutes());

        orderJpaRepository.save(theOrder);
    }
}
