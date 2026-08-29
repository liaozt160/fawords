package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.fawords.FawordsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FawordsApplication.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        User user = new User(null, "testuser", "test@example.com", "password", Role.USER);
        userRepository.save(user);

        User found = userRepository.findByUsername("testuser").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testExistsByUsername() {
        User user = new User(null, "testuser", "test@example.com", "password", Role.USER);
        userRepository.save(user);

        assertThat(userRepository.existsByUsername("testuser")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    public void testExistsByEmail() {
        User user = new User(null, "testuser", "test@example.com", "password", Role.USER);
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }
}