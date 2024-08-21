package com.ejercicio.integracion.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UsuarioDTO {
    private UUID id;
    private String name;
    private String email;
    private List<TelefonoDTO> phones;
    private String token;
    private boolean isActive;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
}
