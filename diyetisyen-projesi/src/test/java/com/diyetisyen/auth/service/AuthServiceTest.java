package com.diyetisyen.auth.service;

import com.diyetisyen.auth.dto.AuthResponse;
import com.diyetisyen.auth.dto.LoginRequest;
import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.service.RoleService;
import com.diyetisyen.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_should_return_token() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("melike");
        request.setPassword("123456");

        Role role = Role.builder().name("ROLE_USER").build();

        User user = User.builder()
                .username("melike")
                .password("encoded")
                .roles(Set.of(role))
                .build();

        when(userService.findByUsername("melike"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(any(), any()))
                .thenReturn("dummy-token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("dummy-token", response.getToken());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(eq("melike"), any());
    }
}
