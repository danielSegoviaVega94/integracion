package com.ejercicio.integracion.security;

import com.ejercicio.integracion.entity.Usuario;
import com.ejercicio.integracion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));
        return UsuarioPrincipal.create(usuario);
    }

    @Transactional
    public UserDetails loadUserById(UUID id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado con el id: " + id)
        );

        return UsuarioPrincipal.create(usuario);
    }
}

