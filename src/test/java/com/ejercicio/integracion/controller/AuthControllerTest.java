package com.ejercicio.integracion.controller;

import com.ejercicio.integracion.dto.UsuarioDTO;
import com.ejercicio.integracion.entity.Usuario;
import com.ejercicio.integracion.repository.UsuarioRepository;
import com.ejercicio.integracion.security.JwtTokenProvider;
import com.ejercicio.integracion.security.payload.JwtAuthenticationResponse;
import com.ejercicio.integracion.security.payload.LoginRequest;
import com.ejercicio.integracion.security.payload.SignUpRequest;
import com.ejercicio.integracion.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mockedJwtToken");
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JwtAuthenticationResponse responseBody = (JwtAuthenticationResponse) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("mockedJwtToken", responseBody.getAccessToken());
    }

    @Test
    public void testRegisterUser_Success() {
        SignUpRequest signUpRequest = new SignUpRequest("Test User", "test@example.com", "password", new ArrayList<>());
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setNombre("Test User");

        when(validationUtils.isValidEmail(anyString())).thenReturn(true);
        when(validationUtils.isValidPassword(anyString())).thenReturn(true);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UsuarioDTO responseBody = (UsuarioDTO) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("test@example.com", responseBody.getEmail());
        assertEquals("Test User", responseBody.getName());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        SignUpRequest signUpRequest = new SignUpRequest("Test User", "test@example.com", "password", new ArrayList<>());

        when(validationUtils.isValidEmail(anyString())).thenReturn(true);
        when(validationUtils.isValidPassword(anyString())).thenReturn(true);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Map.of("mensaje", "Error: El correo ya está en uso!"), responseEntity.getBody());
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        SignUpRequest signUpRequest = new SignUpRequest("Test User", "invalid-email", "password", new ArrayList<>());

        when(validationUtils.isValidEmail(anyString())).thenReturn(false);

        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Map.of("mensaje", "El formato del correo es inválido"), responseEntity.getBody());
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("Test User", "test@example.com", "invalid-password", new ArrayList<>());

        when(validationUtils.isValidEmail(anyString())).thenReturn(true);
        when(validationUtils.isValidPassword(anyString())).thenReturn(false);

        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        String expectedMessage = "El formato de la contraseña es inválido. La contraseña debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una letra minúscula, un número, y un carácter especial (@#$%^&+=).";
        assertEquals(Map.of("mensaje", expectedMessage), responseEntity.getBody());
    }
}
