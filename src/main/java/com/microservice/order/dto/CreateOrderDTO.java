package com.microservice.order.dto;

import com.microservice.order.domain.OrderStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateOrderDTO(
        @NotBlank @Size(min = 5, max = 250, message = "invalid userEmail length") String userEmail,
        @NotBlank @Size(min = 5, max = 250, message = "invalid userName length") String userName,
        @NotBlank @Size(min = 5, max = 250, message = "invalid deliveryAddress length") String deliveryAddress,
        OrderStatus status,
        LocalDateTime deliveryDate) {
}
