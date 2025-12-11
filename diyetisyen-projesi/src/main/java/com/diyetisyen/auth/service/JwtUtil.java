package com.diyetisyen.auth.service;

import com.diyetisyen.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "secretKey123456"; // Prod’da env variable önerilir
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 saat

    public String generateToken(String username,  Set<Role> roles) {
        String roleString = roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(",")); // ROLE_USER,ROLE_ADMIN gibi

        return Jwts.builder()
                .claim("roles", roleString)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
