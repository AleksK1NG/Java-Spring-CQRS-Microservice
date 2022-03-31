package com.microservice.order.queries;

import com.microservice.order.dto.OrderResponseDto;

import java.util.Optional;

public interface QueryHandler {
    Optional<OrderResponseDto> handle(GetOrderByIdQuery query);
}
