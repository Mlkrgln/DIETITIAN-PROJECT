package com.diyetisyen;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
public class SmokeTest {

    @Test
    void should_pass(){
        assertTrue(true);
    }
}
