package com.microservice.order.delivery.http;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.shared.exceptions.NotFoundExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Optional;


@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class OrderControllerAdvice  {
    private final Tracer tracer;

    @ExceptionHandler(OrderNotFoundException.class)
    @NewSpan(name = "OrderControllerAdvice")
    public ResponseEntity<NotFoundExceptionResponse> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        final var response = NotFoundExceptionResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now().toString())
                .build();
        log.error("OrderNotFoundException response: {} ", response);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.error(ex));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



}
