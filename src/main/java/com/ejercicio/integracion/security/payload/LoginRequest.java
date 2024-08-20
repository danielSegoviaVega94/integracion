package com.ejercicio.integracion.security.payload;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String correo;
    private String password;
}
