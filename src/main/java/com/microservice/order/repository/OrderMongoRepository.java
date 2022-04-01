package com.microservice.order.repository;

import com.microservice.order.domain.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
}
