package com.microservice.order.dto;

import com.microservice.order.domain.OrderStatus;

import javax.validation.constraints.NotNull;

public record UpdateOrderStatusDTO(@NotNull OrderStatus status) {
}
