package com.diyetisyen.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtil.generateToken(
                "melike",
                Set.of("ROLE_CUSTOMER")
        );

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("melike", jwtUtil.extractUsername(token));
    }
}
