package com.diyetisyen.user.repository;

import com.diyetisyen.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_should_return_empty() {
        Optional<User> user = userRepository.findByUsername("nonexistent");
        assertTrue(user.isEmpty());
    }

    @Test
    void findByUsername_should_return_user() {
        User u = new User();
        u.setUsername("testuser");
        u.setEmail("test@example.com");
        u.setPassword("1234");
        userRepository.save(u);

        Optional<User> found = userRepository.findByUsername("testuser");
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }
}