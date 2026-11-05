package com.fawords.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Reset password request payload")
public class ResetPasswordRequest {

    @NotBlank
    @Schema(description = "Password reset token", example = "reset-token-12345")
    private String token;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "New password (at least 6 characters)", example = "newsecret123")
    private String newPassword;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
