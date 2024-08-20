package com.ejercicio.integracion.controller;

import com.ejercicio.integracion.dto.UsuarioDTO;
import com.ejercicio.integracion.entity.Telefono;
import com.ejercicio.integracion.entity.Usuario;
import com.ejercicio.integracion.mapper.UsuarioMapper;
import com.ejercicio.integracion.repository.UsuarioRepository;
import com.ejercicio.integracion.security.JwtTokenProvider;
import com.ejercicio.integracion.security.payload.JwtAuthenticationResponse;
import com.ejercicio.integracion.security.payload.LoginRequest;
import com.ejercicio.integracion.security.payload.SignUpRequest;
import com.ejercicio.integracion.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ValidationUtils validationUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, ValidationUtils validationUtils) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.validationUtils = validationUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);

            Usuario usuario = usuarioRepository.findByCorreo(loginRequest.getCorreo()).orElseThrow(() ->
                    new IllegalArgumentException("Usuario no encontrado"));
            usuario.setLastLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (Exception ex) {
            logger.error("Error durante la autenticación", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No se pudo autenticar el usuario.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (!validationUtils.isValidEmail(signUpRequest.getCorreo())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El formato del correo es inválido"));
        }

        if (!validationUtils.isValidPassword(signUpRequest.getPassword())) {
            String passwordRequirements = "La contraseña debe tener al menos 8 caracteres, " +
                    "incluyendo una letra mayúscula, una letra minúscula, " +
                    "un número, y un carácter especial (@#$%^&+=).";
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El formato de la contraseña es inválido. " + passwordRequirements));
        }

        if (usuarioRepository.existsByCorreo(signUpRequest.getCorreo())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Error: El correo ya está en uso!"));
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(signUpRequest.getNombre());
            usuario.setCorreo(signUpRequest.getCorreo());
            usuario.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            usuario.setCreated(LocalDateTime.now());
            usuario.setModified(LocalDateTime.now());
            usuario.setLastLogin(LocalDateTime.now());
            usuario.setActive(true);

            List<Telefono> telefonos = signUpRequest.getPhones().stream()
                    .map(phone -> {
                        Telefono telefono = new Telefono();
                        telefono.setNumber(phone.getNumber());
                        telefono.setCitycode(phone.getCitycode());
                        telefono.setContrycode(phone.getContrycode());
                        telefono.setUsuario(usuario);
                        return telefono;
                    })
                    .collect(Collectors.toList());
            usuario.setPhones(telefonos);

            String token = UUID.randomUUID().toString();
            usuario.setToken(token);

            Usuario savedUsuario = usuarioRepository.save(usuario);

            UsuarioDTO usuarioDTO = UsuarioMapper.INSTANCE.toDto(savedUsuario);

            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception ex) {
            logger.error("Error durante el registro", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error: No se pudo registrar el usuario."));
        }
    }
}
