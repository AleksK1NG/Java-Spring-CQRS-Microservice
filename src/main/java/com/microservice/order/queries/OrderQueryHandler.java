package com.microservice.order.queries;

import com.microservice.order.dto.OrderResponseDto;
import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.mappers.OrderMapper;
import com.microservice.order.repository.OrderMongoRepository;
import com.microservice.order.repository.OrderPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderQueryHandler implements QueryHandler {

    private final OrderPostgresRepository postgresRepository;
    private final OrderMongoRepository mongoRepository;
    private final Tracer tracer;

    @Override
    @NewSpan(name = "(GetOrderByIdQuery)")
    public OrderResponseDto handle(GetOrderByIdQuery query) {
        Optional.ofNullable(tracer.currentSpan()).ifPresent(span -> span.tag("query", query.toString()));

        final var document = mongoRepository.findById(query.id());
        if (document.isPresent()) {
            Optional.ofNullable(tracer.currentSpan()).ifPresent(span -> span.tag("orderDocument", document.toString()));
            return OrderMapper.orderResponseDtoFromDocument(document.get());
        }

        final var order = postgresRepository.findById(UUID.fromString(query.id()));
        if (order.isEmpty()) throw new OrderNotFoundException("order not found: " + query.id());

        final var orderDocument = OrderMapper.orderDocumentFromEntity(order.get());
        mongoRepository.save(orderDocument);

        Optional.ofNullable(tracer.currentSpan()).ifPresent(span -> span.tag("orderDocument", orderDocument.toString()));
        return OrderMapper.orderResponseDtoFromEntity(order.get());
    }

    @Override
    @NewSpan(name = "(GetOrdersByUserEmailQuery)")
    public Page<OrderResponseDto> handle(GetOrdersByUserEmailQuery query) {
        Optional.ofNullable(tracer.currentSpan()).ifPresent(span -> span.tag("query", query.toString()));

        final var pageRequest = PageRequest.of(query.page(), query.size());
        return mongoRepository.findByUserEmailOrderByDeliveryDate(query.userEmail(), pageRequest).map(OrderMapper::orderResponseDtoFromDocument);
    }

    @Override
    @NewSpan(name = "(GetOrdersByStatusQuery)")
    public Page<OrderResponseDto> handle(GetOrdersByStatusQuery query) {
        Optional.ofNullable(tracer.currentSpan()).ifPresent(span -> span.tag("query", query.toString()));

        final var pageRequest = PageRequest.of(query.page(), query.size());
        return mongoRepository.findByStatusOrderByCreatedAt(query.status(), pageRequest).map(OrderMapper::orderResponseDtoFromDocument);
    }
}
