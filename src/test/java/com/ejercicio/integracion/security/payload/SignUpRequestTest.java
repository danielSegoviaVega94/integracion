package com.ejercicio.integracion.security.payload;

import com.ejercicio.integracion.entity.Telefono;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpRequestTest {

    @Test
    public void testSignUpRequest() {
        List<Telefono> phones = new ArrayList<>();
        SignUpRequest signUpRequest = new SignUpRequest("Test User", "test@example.com", "password", phones);

        assertEquals("Test User", signUpRequest.getNombre());
        assertEquals("test@example.com", signUpRequest.getCorreo());
        assertEquals("password", signUpRequest.getPassword());
        assertEquals(phones, signUpRequest.getPhones());
    }
}
