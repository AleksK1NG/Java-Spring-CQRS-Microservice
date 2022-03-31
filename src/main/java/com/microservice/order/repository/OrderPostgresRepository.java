package com.microservice.order.repository;

import com.microservice.order.domain.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderPostgresRepository extends PagingAndSortingRepository<Order, UUID> {

}
