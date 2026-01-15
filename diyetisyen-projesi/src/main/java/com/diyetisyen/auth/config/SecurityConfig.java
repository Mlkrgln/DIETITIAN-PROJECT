package com.diyetisyen.auth.config;

import com.diyetisyen.auth.service.JwtAuthFilter;
import com.diyetisyen.auth.service.JwtEntryPoint;
import com.diyetisyen.auth.service.JwtUtil;
import com.diyetisyen.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("!test")
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // --- WHITELIST: Swagger ve Auth Uç Noktaları ---
    private static final String[] AUTH_WHITELIST = {
            "/api/auth/**",     // Kayıt ve Giriş (Register/Login)
            "/api/roles",       // Rol listeleme (Eğer izinsiz olmalıysa)
            "/v3/api-docs/**",  // OpenAPI 3.0 dokümantasyon uç noktaları
            "/swagger-ui/**",   // Swagger UI arayüzü
            "/swagger-ui.html"  // Ana Swagger sayfası
    };

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final JwtEntryPoint jwtEntryPoint;

    public SecurityConfig(JwtUtil jwtUtil, UserService userService, JwtEntryPoint jwtEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JwtAuthFilter bir bean olmadığı için manuel oluşturulur
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userService);

        http
                // CSRF'yi devre dışı bırak: REST API'ler için gereksizdir.
                .csrf(AbstractHttpConfigurer::disable)

                // Exception Handling (Yetkilendirme hataları için JwtEntryPoint kullanılır)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))

                // Session Management: Durumsuz (Stateless) yap, JWT kullandığın için session tutma
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Yetkilendirme Kuralları
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Tanımlanan yollara herkese izin ver
                        .anyRequest().authenticated() // Diğer tüm isteklere kimlik doğrulaması zorunludur
                );

        // JWT Filtresini, Spring Security'nin varsayılan kullanıcı adı/şifre filtresinden ÖNCE ekle.
        //
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder, şifreleri veritabanına kaydederken şifrelemek için gereklidir.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager, Spring Security'nin kimlik doğrulama sürecini yönetir.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}