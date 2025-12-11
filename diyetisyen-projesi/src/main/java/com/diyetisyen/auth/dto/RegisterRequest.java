package com.diyetisyen.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    // optional: requestedRole (e.g. ROLE_DIETITIAN) — only admins should set this in prod
}
