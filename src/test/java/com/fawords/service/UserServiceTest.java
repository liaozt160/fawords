package com.fawords.service;

import com.fawords.entity.Role;
import com.fawords.entity.User;
import com.fawords.payload.request.ProfileUpdateRequest;
import com.fawords.payload.response.UserProfileResponse;
import com.fawords.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrentUserProfileSuccess() {
        User user = new User(1L, "testuser", "test@example.com", "pass", Role.USER, true, "http://avatar.jpg", null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserProfileResponse profile = userService.getCurrentUserProfile("testuser");

        assertNotNull(profile);
        assertEquals("testuser", profile.getUsername());
        assertEquals("test@example.com", profile.getEmail());
        assertTrue(profile.isEmailVerified());
        assertEquals("http://avatar.jpg", profile.getAvatarUrl());
        assertEquals(Role.USER, profile.getRole());
    }

    @Test
    public void testUpdateProfileSuccess() {
        User user = new User(1L, "testuser", "test@example.com", "pass", Role.USER, true, "http://avatar.jpg", null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("newusername")).thenReturn(false);
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass123")).thenReturn("encodedNewPass");

        ProfileUpdateRequest request = new ProfileUpdateRequest("newusername", "newemail@example.com", "newpass123", "http://newavatar.jpg");

        UserProfileResponse response = userService.updateProfile("testuser", request);

        assertNotNull(response);
        assertEquals("newusername", response.getUsername());
        assertEquals("newemail@example.com", response.getEmail());
        assertEquals("http://newavatar.jpg", response.getAvatarUrl());
        assertFalse(response.isEmailVerified()); // email changed, so isEmailVerified set to false
        verify(userRepository).save(user);
    }
}
