package com.example.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySuperSecretKeyForJwtGenerationAndValidationWhichIsVeryLongIndeed1234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000); // 24 hours
    }

    @Test
    void testGenerateToken() {
        String username = "testuser";
        String token = jwtUtils.generateToken(username);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        String username = "testuser";
        String token = jwtUtils.generateToken(username);
        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_validToken() {
        String username = "testuser";
        String token = jwtUtils.generateToken(username);
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void testValidateToken_invalidToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        assertFalse(jwtUtils.validateToken(invalidToken));
    }

    @Test
    void testValidateToken_expiredToken() throws InterruptedException {
        jwtUtils = new JwtUtils(); // Re-initialize to set a short expiration
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySuperSecretKeyForJwtGenerationAndValidationWhichIsVeryLongIndeed1234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1); // 1 millisecond expiration

        String username = "testuser";
        String token = jwtUtils.generateToken(username);

        Thread.sleep(50); // Wait for the token to expire

        assertFalse(jwtUtils.validateToken(token));
    }
}