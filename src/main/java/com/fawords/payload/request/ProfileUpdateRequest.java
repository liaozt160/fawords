package com.fawords.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Profile update request payload")
public class ProfileUpdateRequest {

    @Size(min = 3, max = 50)
    @Schema(description = "Updated username", example = "john_updated")
    private String username;

    @Email
    @Schema(description = "Updated email address", example = "john_updated@example.com")
    private String email;

    @Size(min = 6)
    @Schema(description = "New password", example = "newpassword123")
    private String newPassword;

    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    public ProfileUpdateRequest() {
    }

    public ProfileUpdateRequest(String username, String email, String newPassword, String avatarUrl) {
        this.username = username;
        this.email = email;
        this.newPassword = newPassword;
        this.avatarUrl = avatarUrl;
    }

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
