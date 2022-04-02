package com.microservice.configuration;

import com.microservice.order.domain.OrderDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MongoConfiguration {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void mongoInit() {
        final var orders = mongoTemplate.getCollection("orders");
        final var emailIndex = mongoTemplate.indexOps(OrderDocument.class).ensureIndex(new Index("userEmail", Sort.Direction.ASC));
        final var indexInfo = mongoTemplate.indexOps(OrderDocument.class).getIndexInfo();
        log.info("MongoDB connected, email index created: {}", indexInfo);
    }
}
