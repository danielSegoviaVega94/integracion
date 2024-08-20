package com.ejercicio.integracion.security.payload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testLoginRequest() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        assertEquals("test@example.com", loginRequest.getCorreo());
        assertEquals("password", loginRequest.getPassword());
    }
}
