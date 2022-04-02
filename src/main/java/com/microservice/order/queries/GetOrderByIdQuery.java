package com.microservice.order.queries;

import javax.validation.constraints.NotBlank;

public record GetOrderByIdQuery(@NotBlank String id) {

}
