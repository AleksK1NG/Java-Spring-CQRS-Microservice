package com.microservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Spring CQRS Microservice",
        description = "Spring Postgresql MongoDB Kafka CQRS Microservice",
        contact = @Contact(name = "Alexander Bryksin", email = "alexander.bryksin@yandex.ru", url = "https://github.com/AleksK1NG")))
public class MicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }

}
