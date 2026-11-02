package com.diyetisyen.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Güvenlik şeması adı

        return new OpenAPI()
                // 1. API Bilgileri (Başlık, Versiyon, Açıklama)
                .info(new Info()
                        .title("Diyetisyen Projesi API")
                        .version("1.0")
                        .description("Diyetisyen ve Kullanıcı Yönetim Sistemi REST API Dokümantasyonu."))

                // 2. Server Bilgileri (Uygulamanın çalıştığı adres)
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development Server")
                ))

                // 3. JWT (Bearer Token) Güvenlik Ayarları
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Tüm endpoint'lere güvenlik ekle
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Lütfen JWT erişim token'ınızı girin. Örn: Bearer <token>")));
    }
}