package com.diyetisyen.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void secured_endpoint_should_require_token() throws Exception {

        mockMvc.perform(post("/api/auth/register/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_endpoint_should_be_accessible() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                        {
                          "username": "melike",
                          "password": "123456"
                        }
                        """))
                .andExpect(status().isOk());
    }
}
