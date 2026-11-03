package com.fawords.controller;

import com.fawords.payload.request.SigninRequest;
import com.fawords.payload.request.SignupRequest;
import com.fawords.service.AuthService;
import com.fawords.payload.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(AuthController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private com.fawords.security.JwtUtils jwtUtils;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;


    @Test
    public void testSignupSuccess() throws Exception {
        String json = "{\"username\":\"testuser\", \"email\":\"test@example.com\", \"password\":\"password123\"}";
        
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testSignupDuplicateUsername() throws Exception {
        String json = "{\"username\":\"testuser\", \"email\":\"test@example.com\", \"password\":\"password123\"}";
        
        when(authService.registerUser(any(SignupRequest.class)))
                .thenThrow(new RuntimeException("Username is already taken"));

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSigninSuccess() throws Exception {
        String json = "{\"username\":\"testuser\", \"password\":\"password123\"}";
        AuthResponse response = new AuthResponse("jwt-token-123", "testuser", "User authenticated successfully!");
        
        when(authService.authenticateUser(any(SigninRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }

    @Test
    public void testSigninInvalidPassword() throws Exception {
        String json = "{\"username\":\"testuser\", \"password\":\"wrongpassword\"}";
        
        when(authService.authenticateUser(any(SigninRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError());
    }
}
