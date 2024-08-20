package com.ejercicio.integracion.security.payload;

import com.ejercicio.integracion.entity.Telefono;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String nombre;
    private String correo;
    private String password;
    private List<Telefono> phones;
}
