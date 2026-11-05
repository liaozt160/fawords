package com.fawords.controller;

import com.fawords.entity.Role;
import com.fawords.payload.request.ProfileUpdateRequest;
import com.fawords.payload.response.UserProfileResponse;
import com.fawords.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private com.fawords.security.JwtUtils jwtUtils;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "testuser")
    public void testGetProfileSuccess() throws Exception {
        UserProfileResponse profile = new UserProfileResponse(1L, "testuser", "test@example.com", true, "http://avatar.jpg", Role.USER);
        when(userService.getCurrentUserProfile("testuser")).thenReturn(profile);

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testUpdateProfileSuccess() throws Exception {
        String json = "{\"username\":\"newuser\", \"email\":\"new@example.com\", \"newPassword\":\"newpass123\", \"avatarUrl\":\"http://newavatar.jpg\"}";
        UserProfileResponse profile = new UserProfileResponse(1L, "newuser", "new@example.com", false, "http://newavatar.jpg", Role.USER);

        when(userService.updateProfile(eq("testuser"), any(ProfileUpdateRequest.class))).thenReturn(profile);

        mockMvc.perform(put("/api/users/profile")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }
}
