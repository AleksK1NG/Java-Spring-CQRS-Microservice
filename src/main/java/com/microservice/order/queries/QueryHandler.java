package com.microservice.order.queries;

import com.microservice.order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface QueryHandler {
    OrderResponseDto handle(GetOrderByIdQuery query);
    Page<OrderResponseDto> handle(GetOrdersByUserEmailQuery query);
    Page<OrderResponseDto> handle(GetOrdersByStatusQuery query);
}
