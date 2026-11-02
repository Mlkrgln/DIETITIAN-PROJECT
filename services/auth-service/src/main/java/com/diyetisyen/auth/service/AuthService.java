package com.diyetisyen.auth.service;

import com.diyetisyen.auth.dto.AuthResponse;
import com.diyetisyen.auth.dto.LoginRequest;
import com.diyetisyen.auth.dto.RegisterRequest;
import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.service.RoleService;
import com.diyetisyen.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest req) {
        // 1. Kimlik doğrulama
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        // 2. loadUserByUsername yerine doğrudan findByUsername kullanın
        // Böylece casting (ClassCastException) riskini tamamen ortadan kaldırırsınız.
        User user = userService.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 3. Token üretimi
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest req, String requestedRoleName) {

        Role role = roleService.findByName(requestedRoleName)
                .orElseGet(() -> roleService.saveRole(Role.builder().name(requestedRoleName).build()));

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(role))
                .build();

        user = userService.save(user);
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles);
        return new AuthResponse(token);
    }
    // AuthService.java
    public AuthResponse register(RegisterRequest request) {
        // default role = CUSTOMER
        return register(request, "ROLE_CUSTOMER");
    }

}
