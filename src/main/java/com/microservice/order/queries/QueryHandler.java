package com.microservice.order.queries;

import com.microservice.order.dto.OrderResponseDto;

public interface QueryHandler {
    OrderResponseDto handle(GetOrderByIdQuery query);
}
