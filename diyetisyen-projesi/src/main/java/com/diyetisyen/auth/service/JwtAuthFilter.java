package com.diyetisyen.auth.service;

import com.diyetisyen.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final com.diyetisyen.user.service.UserService userService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 1. Authorization header kontrolü
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Token yoksa devam et ve filreden çık
        }

        String token = header.substring(7);

        // 2. Token doğrulama ve kullanıcı çıkarma
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);

            // 3. Kullanıcı oturumu zaten yoksa (önceki filtreler/istekler tarafından ayarlanmadıysa)
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails ud = userService.loadUserByUsername(username);

                // 4. Yeni Authentication nesnesi oluşturma
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. SecurityContext'i güncelleme
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}