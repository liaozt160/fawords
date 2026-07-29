package com.fawords;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World! This is Spring Boot 4.1.0 running on Java 21 inside Fawords (fawords.com).";
    }
}
