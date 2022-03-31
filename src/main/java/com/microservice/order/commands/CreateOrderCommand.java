package com.microservice.order.commands;

import com.microservice.shared.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CreateOrderCommand extends BaseCommand {
    private String email;
    private String deliveryAddress;
    private ZonedDateTime deliveryDate;
}
