package com.fawords;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Hello & Health", description = "Public greeting and health check endpoints")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "Say hello", description = "Returns a friendly greeting message")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public String sayHello() {
        return "Hello World! This is Spring Boot 4.1.2 running on Java 25 inside Fawords (fawords.com).";
    }

    @GetMapping("/api/about")
    @Operation(summary = "About API", description = "Returns about message")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public String about() {
        return "test about";
    }

    @GetMapping("/api/health")
    @Operation(summary = "Health check", description = "Returns system health status")
    @ApiResponse(responseCode = "200", description = "System is healthy")
    public Map<String, Object> healthCheck(
            @Parameter(description = "Mode of health check", example = "detailed")
            @RequestParam(required = false, defaultValue = "basic") String mode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("mode", mode);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
