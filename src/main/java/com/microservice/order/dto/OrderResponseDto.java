package com.microservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private String id;
    private String userEmail;
    private String userName;
    private String deliveryAddress;
    private String status;
    private ZonedDateTime deliveryDate;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
