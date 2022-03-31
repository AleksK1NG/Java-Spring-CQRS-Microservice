package com.microservice.shared.exceptions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotFoundExceptionResponse {
    private String message;
    private ZonedDateTime timestamp;
    private String status;
}
