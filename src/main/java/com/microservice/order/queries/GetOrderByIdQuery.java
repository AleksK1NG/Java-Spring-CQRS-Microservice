package com.microservice.order.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOrderByIdQuery {
    private String id;
}
