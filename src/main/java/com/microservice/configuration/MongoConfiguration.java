package com.microservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class MongoConfiguration {


    @PostConstruct
    public void mongoInit() {
        log.info("MongoDB connected");
    }
}
