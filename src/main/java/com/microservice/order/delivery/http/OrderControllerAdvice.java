package com.microservice.order.delivery.http;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.shared.exceptions.NotFoundExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;


@ControllerAdvice
@Slf4j
public class OrderControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<NotFoundExceptionResponse> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        final var response = NotFoundExceptionResponse.builder()
                .message(ex.getMessage())
                .status("404")
                .timestamp(ZonedDateTime.now())
                .build();
        log.error("OrderNotFoundException response: {} ", response);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
