package com.diyetisyen.integration;

import com.diyetisyen.appointment.mapper.AppointmentMapper;
import com.diyetisyen.appointment.repository.AppointmentRepository;
import com.diyetisyen.auth.service.JwtUtil;
import com.diyetisyen.config.TestSecurityConfig;
import com.diyetisyen.notification.listener.AppointmentNotificationListener;
import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.repository.RoleRepository;
import com.diyetisyen.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentMapper appointmentMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    // ✅ Mocklanan çakışan bean
    @MockBean
    private AppointmentNotificationListener appointmentNotificationListener;

    @BeforeEach
    void setup() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

        User user = User.builder()
                .username("melike")
                .email("melike@test.com")
                .password(passwordEncoder.encode("123456"))
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(user);

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("melike", null, Set.of()));

        when(jwtUtil.generateToken(any(), any()))
                .thenReturn("test-token");
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