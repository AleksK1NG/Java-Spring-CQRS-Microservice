package com.microservice.order.queries;

import com.microservice.order.domain.OrderDocument;
import com.microservice.order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface QueryHandler {
    OrderResponseDto handle(GetOrderByIdQuery query);
    Page<OrderDocument> handle(GetOrdersByUserEmailQuery query);
    Page<OrderDocument> handle(GetOrdersByStatusQuery query);
}
