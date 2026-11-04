package com.fawords.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Sign-in request payload")
public class SigninRequest {
    @NotBlank
    @Schema(description = "Username for login", example = "john_doe")
    private String username;

    @NotBlank
    @Schema(description = "User password", example = "secret123")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}