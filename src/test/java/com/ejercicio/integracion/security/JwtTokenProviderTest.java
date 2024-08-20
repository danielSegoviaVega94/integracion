package com.ejercicio.integracion.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String jwtSecret = "6dbe8cad2c998c65dcb2d27aace9d2a24066c2b0d9fe9366515006318b2fbf5dd687d0bf0f140dd8434b2c0bcfa7045ebf5ff6bf41509145d06d317a523c8d7bbd93fab1cbcf3266362f39e8fecb54276630e9fbd8abf75afa4636fd70ceada249a53219abd1f297a9e4afb9003056f9e74693c8b47c9e5097c312f30e7dfbba367d9182ce0514f97ca8fff0c7e5e419de2c350dfac06bc1880f51a97a183ddd4a73b7eebf7e9739add27050057d9ea284fa31d00e345f5bb9fd56dafe9ceac64f51f3ceff11bcf814a692e84f3dd4811d1d0cb4fc6a5b7051259e531df265bbd8e3b89370dc26d14ced73f2075f6b3ead7fb7a33bfe72cfdada465a694d22139f592ee4f2e99af5209b5084c6275dbcc336386637cf98ba58be808043922fd8c2dc997c1db8f81a5cda878b72bbd8f202543bad34c1e3494e467d61930181fcf837f19b2111c0cf4bdf386d34fd5a03b5e5d6d812541e4745cf7a08042e8b7485ae0d037dc4fc463fab0971880cb524ea42ed1cfdd1b2a0d0ce26a3c68ec37b2ce0b14a3817a5ee69f233d1a75a21948f81824f82dcc0cd1a372222674cd66e107091c6bc93fa1654fa297ee15f97897743981527a82c94a2f618e5b6f6d28df82adf422cf498887b0da4e4ea3609e22643068cc42fd3544f6b631f89273b522b95d14b38608394cab6191e45c5ad136b6425a809e7e86a3be5ed0769496a9c";
    private final int jwtExpirationInMs = 3600000;
    private final SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider(jwtSecret, jwtExpirationInMs);
    }

    @Test
    public void testGenerateToken() {
        Authentication authentication = mock(Authentication.class);
        UsuarioPrincipal userPrincipal = new UsuarioPrincipal(UUID.randomUUID(), "TestUser", "test@example.com", "password", null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
    }

    @Test
    public void testGetUserIdFromJWT() {
        String token = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        UUID userId = jwtTokenProvider.getUserIdFromJWT(token);
        assertNotNull(userId);
    }

    @Test
    public void testValidateToken() {
        String token = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    public void testValidateInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalidToken"));
    }
}
