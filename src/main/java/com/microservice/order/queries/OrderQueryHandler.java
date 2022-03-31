package com.microservice.order.queries;

import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderQueryHandler implements QueryHandler{

    private final OrderPostgresRepository postgresRepository;

    @Override
    public OrderResponseDto handle(GetOrderByIdQuery query) {
        final var order = postgresRepository.findById(UUID.fromString(query.getId()));
        if (order.isEmpty()) throw new OrderNotFoundException("order not found: " + query.getId());
        return OrderMapper.orderResponseDtoFromEntity(order.get());
    }
}
