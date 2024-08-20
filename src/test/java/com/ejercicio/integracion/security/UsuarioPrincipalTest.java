package com.ejercicio.integracion.security;

import com.ejercicio.integracion.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioPrincipalTest {

    @Test
    public void testCreateUsuarioPrincipal() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("TestUser");
        usuario.setCorreo("test@example.com");
        usuario.setPassword("password");
        usuario.setPhones(new ArrayList<>());

        UsuarioPrincipal usuarioPrincipal = UsuarioPrincipal.create(usuario);

        assertEquals(id, usuarioPrincipal.getId());
        assertEquals("TestUser", usuarioPrincipal.getNombre());
        assertEquals("test@example.com", usuarioPrincipal.getCorreo());
        assertEquals("password", usuarioPrincipal.getPassword());
        assertTrue(usuarioPrincipal.getPhones().isEmpty());
        assertTrue(usuarioPrincipal.isEnabled());
    }

    @Test
    public void testGetAuthorities() {
        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(UUID.randomUUID(), "TestUser", "test@example.com", "password", new ArrayList<>());
        Collection<? extends GrantedAuthority> authorities = usuarioPrincipal.getAuthorities();
        assertTrue(authorities.isEmpty());
    }
}
