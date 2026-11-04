package com.fawords.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Sign-up request payload")
public class SignupRequest {
    @NotBlank
    @Schema(description = "Desired username", example = "john_doe")
    private String username;

    @NotBlank
    @Email
    @Schema(description = "User email address", example = "john@example.com")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "User password (at least 6 characters)", example = "secret123")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}