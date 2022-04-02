package com.microservice.filters;

import com.microservice.order.exceptions.OrderNotFoundException;
import com.microservice.shared.exceptions.InternalServerErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor

@Order(2)
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    private final Tracer tracer;


    @ExceptionHandler(RuntimeException.class)
    @NewSpan(name = "handleRuntimeException")
    public ResponseEntity<InternalServerErrorResponse> handleRuntimeException(OrderNotFoundException ex, WebRequest request) {
        final var response = new InternalServerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now().toString());
        log.error("OrderNotFoundException response: {} ", response);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.error(ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @Override
    @NewSpan(name = "handleMethodArgumentNotValid")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());


        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        Optional.ofNullable(tracer.currentSpan()).map(span -> span.error(ex));

        return new ResponseEntity<>(body, status);
    }
}
