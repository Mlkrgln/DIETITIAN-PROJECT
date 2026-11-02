package com.diyetisyen.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys; // Yeni import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Yeni import
import java.nio.charset.StandardCharsets; // Yeni import
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    private final String secret;
    private final long expMs;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.exp-ms}") long expMs
    ) {
        this.secret = secret;
        this.expMs = expMs;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Set<String> roles) {
        return generateToken(username, null, roles);
    }

    public String generateToken(String username, Long userId, Set<String> roles) {
        return generateToken(username, userId, null, roles);
    }

    public String generateToken(String username, Long userId, String email, Set<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMs);

        var token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp);
        if (userId != null) {
            token.claim("userId", userId);
        }
        if (email != null) {
            token.claim("email", email);
        }
        return token.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
