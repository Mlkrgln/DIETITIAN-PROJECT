package com.diyetisyen.auth.util;

import com.diyetisyen.auth.service.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class JwtUtilTest {

    @Test
    void should_generate_and_validate_token() {
        // 1️⃣ Arrange
        JwtUtil jwtUtil = new JwtUtil(
                "testSecretKeytestSecretKeytestSecretKeytestSecretKey",
                3600000
        );

        // 2️⃣ Act
        String token = jwtUtil.generateToken(
                "melike",
                Set.of("ROLE_USER")
        );

        // 3️⃣ Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("melike", jwtUtil.extractUsername(token));
    }

    @Test
    void should_fail_when_token_expired() throws InterruptedException {
        JwtUtil jwtUtil = new JwtUtil(
                "testSecretKeytestSecretKeytestSecretKeytestSecretKey",
                1 // 1 ms
        );

        String token = jwtUtil.generateToken("melike", Set.of("ROLE_USER"));

        Thread.sleep(5);

        assertFalse(jwtUtil.validateToken(token));
    }
    //Username gerçekten token’ın içinden mi geliyor?
    //“Token’ın içini gerçekten okuyabiliyor muyuz?”
    @Test
    void should_extract_username_from_token() {
        JwtUtil jwtUtil = new JwtUtil(
                "testSecretKeytestSecretKeytestSecretKeytestSecretKey",
                3600000
        );

        String token = jwtUtil.generateToken(
                "melike",
                Set.of("ROLE_USER")
        );

        String username = jwtUtil.extractUsername(token);

        assertEquals("melike", username);
    }
    //Token bozuksa (random string) ne olur?
    //“Biri sahte token yollarsa?”
    @Test
    void should_fail_when_token_is_invalid() {
        JwtUtil jwtUtil = new JwtUtil(
                "testSecretKeytestSecretKeytestSecretKeytestSecretKey",
                3600000
        );

        String fakeToken = "abc.def.ghi";

        assertFalse(jwtUtil.validateToken(fakeToken));
    }
    //Farklı secret ile token doğrulanamaz mı?
    //“Başka bir servis bu token’ı okuyamaz mı?”
    @Test
    void should_fail_when_secret_is_different() {
        JwtUtil jwtUtil1 = new JwtUtil(
                "secret111111111111111111111111111111",
                3600000
        );

        JwtUtil jwtUtil2 = new JwtUtil(
                "secret222222222222222222222222222222",
                3600000
        );

        String token = jwtUtil1.generateToken(
                "melike",
                Set.of("ROLE_USER")
        );

        assertFalse(jwtUtil2.validateToken(token));
    }

}
