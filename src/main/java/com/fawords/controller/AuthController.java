package com.fawords.controller;

import com.fawords.payload.request.SignupRequest;
import com.fawords.payload.request.SigninRequest;
import com.fawords.payload.response.AuthResponse;
import com.fawords.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or error during registration")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated", 
                 content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "500", description = "Authentication failed / Internal server error")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest request) {
        try {
            AuthResponse authResponse = authService.authenticateUser(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}