package com.diyetisyen.auth.controller;

import com.diyetisyen.auth.dto.AuthResponse;
import com.diyetisyen.auth.dto.LoginRequest;
import com.diyetisyen.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Security filtrelerini devre dışı bırakır
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // HATANIN ÇÖZÜMÜ: Spring'in aradığı veritabanı fabrikasını sahte olarak oluşturuyoruz
    @MockBean
    private jakarta.persistence.EntityManagerFactory entityManagerFactory;

    // Eğer projenizde JWT kullanıyorsanız, muhtemelen bu bean de hata verecektir:
    // @MockBean
    // private JwtService jwtService;

    @Test
    void login_should_return_200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("melike");
        request.setPassword("123456");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("dummy-token"));

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }
}