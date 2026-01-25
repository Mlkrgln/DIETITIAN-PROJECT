package com.diyetisyen.integration;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.repository.RoleRepository;
import com.diyetisyen.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = roleRepository.save(
                Role.builder().name("ROLE_ADMIN").build()
        );

        User user = User.builder()
                .username("melike")
                .email("melike@test.com")
                .password(passwordEncoder.encode("123456"))
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(user);
    }

    @Test
    void register_admin_should_be_accessible_without_token() throws Exception {
        mockMvc.perform(post("/api/auth/register/admin")
                        .contentType("application/json")
                        .content("""
                        {
                          "username": "admin",
                          "email": "admin@test.com",
                          "password": "123456"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void login_endpoint_should_be_accessible() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                        {
                          "username": "melike",
                          "password": "123456"
                        }
                        """))
                .andExpect(status().isOk());
    }
}

