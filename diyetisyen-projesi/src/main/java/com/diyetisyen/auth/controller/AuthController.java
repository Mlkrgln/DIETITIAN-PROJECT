package com.diyetisyen.auth.controller;


import com.diyetisyen.auth.model.Role;
import com.diyetisyen.auth.model.request.AuthRequest;
import com.diyetisyen.auth.model.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diyetisyen.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.COSTUMER));
    }

    @PostMapping("/register/dietitian")
    public ResponseEntity<AuthResponse> registerDietitian(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.DIETITIAN));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.ADMIN));
    }
}
