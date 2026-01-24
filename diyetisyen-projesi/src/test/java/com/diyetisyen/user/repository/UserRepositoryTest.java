package com.diyetisyen.user.repository;

import com.diyetisyen.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_should_return_user() {
        User user = User.builder()
                .username("melike")
                .email("melike@test.com")
                .password("123")
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("melike");

        assertTrue(found.isPresent());
        assertEquals("melike", found.get().getUsername());
    }

    @Test
    void findByUsername_should_return_empty() {
        Optional<User> found = userRepository.findByUsername("none");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByEmail_should_return_user() {
        User user = User.builder()
                .username("melike")
                .email("melike@test.com")
                .password("123")
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("melike@test.com");

        assertTrue(found.isPresent());
    }
}
