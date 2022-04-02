package com.microservice.order.queries;

import com.microservice.order.domain.OrderDocument;
import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.order.mappers.OrderMapper;
import com.microservice.order.repository.OrderMongoRepository;
import com.microservice.order.repository.OrderPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderQueryHandler implements QueryHandler {

    private final OrderPostgresRepository postgresRepository;
    private final OrderMongoRepository mongoRepository;

    @Override
    public OrderResponseDto handle(GetOrderByIdQuery query) {
        final var document = mongoRepository.findById(query.id());
        if (document.isPresent()) {
            return OrderMapper.orderResponseDtoFromDocument(document.get());
        }
        final var order = postgresRepository.findById(UUID.fromString(query.id()));
        if (order.isEmpty()) throw new OrderNotFoundException("order not found: " + query.id());

        OrderDocument orderDocument = OrderMapper.orderDocumentFromEntity(order.get());
        mongoRepository.save(orderDocument);
        return OrderMapper.orderResponseDtoFromEntity(order.get());
    }

    @Override
    public Page<OrderResponseDto> handle(GetOrdersByUserEmailQuery query) {
        final var pageRequest = PageRequest.of(query.page(), query.size());
        return mongoRepository.findByUserEmailOrderByDeliveryDate(query.userEmail(), pageRequest).map(OrderMapper::orderResponseDtoFromDocument);
    }

    @Override
    public Page<OrderResponseDto> handle(GetOrdersByStatusQuery query) {
        final var pageRequest = PageRequest.of(query.page(), query.size());
        return mongoRepository.findByStatusOrderByCreatedAt(query.status(), pageRequest).map(OrderMapper::orderResponseDtoFromDocument);
    }
}
