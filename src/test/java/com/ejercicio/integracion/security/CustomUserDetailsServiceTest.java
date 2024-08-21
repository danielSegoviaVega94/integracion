package com.ejercicio.integracion.security;

import com.ejercicio.integracion.entity.Usuario;
import com.ejercicio.integracion.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        customUserDetailsService = new CustomUserDetailsService(usuarioRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("test@example.com"));
    }

    @Test
    public void testLoadUserById() {
        UUID userId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        assertEquals(userId, ((UsuarioPrincipal) userDetails).getId());
    }

    @Test
    public void testLoadUserByIdNotFound() {
        UUID userId = UUID.randomUUID();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserById(userId));
    }
}
