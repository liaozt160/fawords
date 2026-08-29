package com.example.demo.dto;

import com.example.demo.config.SecurityConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DtoAndSecurityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSignupRequestValidationValid() {
        SignupRequest request = new SignupRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid SignupRequest should have no violations");
    }

    @Test
    public void testSignupRequestValidationInvalid() {
        SignupRequest request = new SignupRequest();
        request.setUsername("");
        request.setEmail("invalid-email");
        request.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid SignupRequest should have violations");
    }

    @Test
    public void testSigninRequestValidation() {
        SigninRequest request = new SigninRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        Set<ConstraintViolation<SigninRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testAuthResponse() {
        AuthResponse response = new AuthResponse("token123", "testuser", "Success");
        assertEquals("token123", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testPasswordEncoder() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        String encoded = encoder.encode("myPassword");
        assertTrue(encoder.matches("myPassword", encoded));
    }
}