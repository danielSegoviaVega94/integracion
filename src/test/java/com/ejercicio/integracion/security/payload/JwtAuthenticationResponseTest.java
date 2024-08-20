package com.ejercicio.integracion.security.payload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationResponseTest {

    @Test
    public void testJwtAuthenticationResponse() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("testToken");
        assertEquals("testToken", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
    }
}