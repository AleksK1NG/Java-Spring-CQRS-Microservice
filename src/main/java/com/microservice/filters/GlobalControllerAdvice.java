package com.microservice.filters;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.shared.exceptions.InternalServerErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor

@Order(2)
public class GlobalControllerAdvice {
    private final Tracer tracer;


    @ExceptionHandler(RuntimeException.class)
    @NewSpan(name = "handleRuntimeException")
    public ResponseEntity<InternalServerErrorResponse> handleRuntimeException(OrderNotFoundException ex, WebRequest request) {
        final var response = new InternalServerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now().toString());
        log.error("OrderNotFoundException response: {} ", response);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.error(ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @NewSpan(name = "handleInvalidArgument")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        final Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}
