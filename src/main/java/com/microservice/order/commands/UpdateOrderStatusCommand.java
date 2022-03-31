package com.microservice.order.commands;

import com.microservice.order.domain.OrderStatus;
import com.microservice.shared.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UpdateOrderStatusCommand extends BaseCommand {
    private OrderStatus status;
}
