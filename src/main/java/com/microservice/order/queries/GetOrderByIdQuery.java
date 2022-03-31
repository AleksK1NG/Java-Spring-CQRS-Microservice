package com.microservice.order.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class GetOrderByIdQuery {
    @NotBlank
    private String id;
}
