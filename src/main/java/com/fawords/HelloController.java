package com.fawords;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World! This is Spring Boot 4.1.0 running on Java 21 inside Fawords (fawords.com).";
    }

    @GetMapping("/api/about")
    public String about() {
        return "test about";
    }

    @GetMapping("/api/health")
    public Map<String, Object> healthCheck(@RequestParam(required = false, defaultValue = "basic") String mode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("mode", mode);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
