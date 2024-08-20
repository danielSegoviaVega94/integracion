package com.ejercicio.integracion.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class JwtAuthenticationEntryPointTest {

    @Test
    public void testCommence() throws Exception {
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        entryPoint.commence(request, response, null);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado: No autorizado");
    }
}
