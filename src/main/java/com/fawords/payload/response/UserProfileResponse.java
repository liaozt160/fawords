package com.fawords.payload.response;

import com.fawords.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User profile response payload")
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private boolean isEmailVerified;
    private String avatarUrl;
    private Role role;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, String username, String email, boolean isEmailVerified, String avatarUrl, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
