package com.fawords.service;

import com.fawords.payload.response.AuthResponse;
import com.fawords.payload.request.*;
import com.fawords.entity.Role;
import com.fawords.entity.User;
import com.fawords.repository.UserRepository;
import com.fawords.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // Simulation of token stores
    private final Map<String, String> verificationTokens = new HashMap<>();
    private final Map<String, String> resetPasswordTokens = new HashMap<>();

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String registerUser(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // Default role
        user.setEmailVerified(false);

        userRepository.save(user);

        // Simulation of email verification token generation
        String token = UUID.randomUUID().toString();
        verificationTokens.put(token, user.getUsername());
        
        return "User registered successfully! Verification token (simulation): " + token;
    }

    public AuthResponse authenticateUser(SigninRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(request.getUsername());

        return new AuthResponse(jwt, request.getUsername(), "User authenticated successfully!");
    }

    public String verifyEmail(String token) {
        String username = verificationTokens.get(token);
        if (username == null) {
            throw new RuntimeException("Error: Invalid verification token!");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        user.setEmailVerified(true);
        userRepository.save(user);
        verificationTokens.remove(token);

        return "Email verified successfully!";
    }

    public String initiateForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: Email not found!"));

        String token = UUID.randomUUID().toString();
        resetPasswordTokens.put(token, user.getEmail());

        return "Password reset initiated! Reset token (simulation): " + token;
    }

    public String resetPassword(String token, String newPassword) {
        String email = resetPasswordTokens.get(token);
        if (email == null) {
            throw new RuntimeException("Error: Invalid or expired reset token!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetPasswordTokens.remove(token);

        return "Password reset successfully!";
    }

    public AuthResponse googleLogin(GoogleLoginRequest request) {
        // Simulation of Google OAuth2 verification
        // In reality, we would verify the idToken with Google API
        
        Optional<User> userOptional = userRepository.findByOauth2Id(request.getIdToken());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            // Check if user with same email exists
            userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                user = userOptional.get();
                user.setOauth2Id(request.getIdToken());
            } else {
                // Register new user
                user = new User();
                user.setUsername(request.getName().toLowerCase().replace(" ", "_") + "_" + UUID.randomUUID().toString().substring(0, 5));
                user.setEmail(request.getEmail());
                user.setOauth2Id(request.getIdToken());
                user.setAvatarUrl(request.getAvatarUrl());
                user.setRole(Role.USER);
                user.setEmailVerified(true);
                // No password for OAuth2 users
            }
            userRepository.save(user);
        }

        String jwt = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(jwt, user.getUsername(), "Google login successful!");
    }
}