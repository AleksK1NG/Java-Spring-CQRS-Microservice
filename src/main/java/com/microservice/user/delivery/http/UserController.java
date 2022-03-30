package com.microservice.user.delivery.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "/user")
public class UserController {

    @GetMapping
    public ResponseEntity<String> getUser(@RequestParam(name = "name", required = false, defaultValue = "Alex") String name) {
        log.info("(GetUser) name: {}", name);
        return ResponseEntity.ok(  name);
    }
}
