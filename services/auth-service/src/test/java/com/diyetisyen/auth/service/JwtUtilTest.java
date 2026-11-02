package com.diyetisyen.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {
    private static final String SECRET = "unit-test-secret-that-is-long-enough-32-bytes";

    @Test
    void tokenIncludesStableUserIdEmailAndRoleList() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);
        String token = jwtUtil.generateToken("ayse", 42L, "ayse@example.test", Set.of("ROLE_CUSTOMER"));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("ayse", claims.getSubject());
        assertEquals(42, ((Number) claims.get("userId")).longValue());
        assertEquals("ayse@example.test", claims.get("email"));
        assertEquals(List.of("ROLE_CUSTOMER"), claims.get("roles", List.class));
    }
}
