package com.food_delivery_system.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private HttpStatus statusCode;

    public static <T> GenericResponseDTO<T> success(T data) {
        return success(data, "SUCCESS");
    }

    public static <T> GenericResponseDTO<T> success(T data, String message) {
        return GenericResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GenericResponseDTO<T> error(String message, HttpStatus statusCode) {
        return GenericResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .build();
    }
}
