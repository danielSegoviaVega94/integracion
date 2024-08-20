package com.ejercicio.integracion.security;
import com.ejercicio.integracion.entity.Telefono;
import com.ejercicio.integracion.entity.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
public class UsuarioPrincipal implements UserDetails {
    private UUID id;
    private String nombre;
    private String correo;
    private String password;
    private List<Telefono> phones;

    public UsuarioPrincipal(UUID id, String nombre, String correo, String password, List<Telefono> phones) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.phones = phones;
    }

    public static UsuarioPrincipal create(Usuario usuario) {
        return new UsuarioPrincipal(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getPassword(),
                usuario.getPhones()
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
