package com.food_delivery_system.order_service.errors;

import com.food_delivery_system.order_service.dto.GenericResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OrderControllerAdvice {

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<GenericResponseDTO<String>> handlePaymentFailed(PaymentFailedException ex) {
        log.warn("Payment failed: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PaymentServiceUnavailableException.class)
    public ResponseEntity<GenericResponseDTO<String>> handlePaymentServiceUnavailable(PaymentServiceUnavailableException ex) {
        log.error("Payment service unavailable", ex);
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<GenericResponseDTO<String>> handleOrderNotFound(OrderNotFoundException ex) {
        log.warn("Order not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OrderIsNotInPendingPaymentStatusException.class)
    public ResponseEntity<GenericResponseDTO<String>> handleOrderNotPendingPayment(OrderIsNotInPendingPaymentStatusException ex) {
        log.warn("Order not in PENDING_PAYMENT status: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<GenericResponseDTO<String>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(GenericResponseDTO.error(message, status));
    }
}
