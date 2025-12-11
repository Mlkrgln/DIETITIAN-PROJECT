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

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.exp-ms}")
    private long expMs;

    // Secret Key oluşturma metodu (Güncel JJWT 0.12.x syntax'ı için kritik)
    private SecretKey getSigningKey() {
        // Base64 kodlaması yerine, String secret'i byte dizisine çevirir.
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Set<String> roles) {
        String rolesStr = String.join(",", roles);
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesStr)
                .setIssuedAt(now)
                .setExpiration(exp)
                // Güncel syntax: setSigningKey yerine signWith ve SecretKey kullanımı
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // Token'ın süresinin dolup dolmadığını kontrol eder
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            // Loglama yapılabilir, hata durumunda token geçersiz
            return false;
        }
    }

    private Claims getClaims(String token) {
        // Güncel syntax: parserBuilder ve setSigningKey/build kullanımı
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // SecretKey kullan
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}