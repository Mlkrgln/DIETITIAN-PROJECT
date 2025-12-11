package com.diyetisyen.auth.service;

import com.diyetisyen.auth.model.request.AuthRequest;
import com.diyetisyen.auth.model.response.AuthResponse;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.repository.UserRepository;
import com.diyetisyen.user.model.Role;

import java.util.Set;

public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Şifre yanlış");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }

    public AuthResponse register(AuthRequest request, Role role) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Kullanıcı zaten mevcut");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getUsername() + "@example.com")
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }
}
