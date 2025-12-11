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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        var user = (User) userService.loadUserByUsername(req.getUsername());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest req, String requestedRoleName) {
        if (userService.existsByUsername(req.getUsername())) throw new RuntimeException("Username exists");
        if (userService.existsByEmail(req.getEmail())) throw new RuntimeException("Email exists");

        Role role = roleService.findByName(requestedRoleName)
                .orElseGet(() -> roleService.saveRole(Role.builder().name(requestedRoleName).build()));

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(role))
                .build();

        userService.save(user);
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return new AuthResponse(token);
    }
}
