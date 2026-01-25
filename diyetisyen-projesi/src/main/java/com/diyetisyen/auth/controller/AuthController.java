package com.diyetisyen.auth.controller;

import com.diyetisyen.auth.dto.AuthResponse;
import com.diyetisyen.auth.dto.LoginRequest;
import com.diyetisyen.auth.dto.RegisterRequest;
import com.diyetisyen.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    // Public register for customers
    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req, "ROLE_CUSTOMER"));
    }

    // Admin-only endpoints to create dietitian/admin should be protected with ROLE_ADMIN in prod
    @PostMapping("/register/dietitian")
    public ResponseEntity<AuthResponse> registerDietitian(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req, "ROLE_DIETITIAN"));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req, "ROLE_ADMIN"));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
