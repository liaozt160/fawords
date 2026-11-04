package com.fawords.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing JWT token, username, and status message")
public class AuthResponse {
    @Schema(description = "JWT Access Token")
    private String token;
    
    @Schema(description = "Username of the authenticated user")
    private String username;
    
    @Schema(description = "Response status message")
    private String message;

    public AuthResponse(String token, String username, String message) {
        this.token = token;
        this.username = username;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}