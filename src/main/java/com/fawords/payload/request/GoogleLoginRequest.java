package com.fawords.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Google OAuth2 login request payload")
public class GoogleLoginRequest {

    @NotBlank
    @Schema(description = "Google ID or token", example = "google-oauth2-id-12345")
    private String idToken;

    @Email
    @Schema(description = "Google email address", example = "john.google@example.com")
    private String email;

    @Schema(description = "Google user full name or username", example = "John Google")
    private String name;

    @Schema(description = "Google avatar URL", example = "https://lh3.googleusercontent.com/a/default-avatar")
    private String avatarUrl;

    public GoogleLoginRequest() {
    }

    public GoogleLoginRequest(String idToken, String email, String name, String avatarUrl) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
