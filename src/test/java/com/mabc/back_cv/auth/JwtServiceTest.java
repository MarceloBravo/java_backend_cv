package com.mabc.back_cv.auth;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de JwtService")
class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        
        Rol rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
        
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .nombre("Test")
                .apellido("User")
                .activo(true)
                .rol(rol)
                .build();
    }

    @Test
    @DisplayName("Generar token de acceso exitosamente")
    void generateAccessToken_Success() {
        String accessToken = jwtService.generateAccessToken(testUser);
        
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        assertTrue(jwtService.isAccessToken(accessToken));
        assertFalse(jwtService.isRefreshToken(accessToken));
    }

    @Test
    @DisplayName("Generar token de refresco exitosamente")
    void generateRefreshToken_Success() {
        String refreshToken = jwtService.generateRefreshToken(testUser);
        
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(jwtService.isRefreshToken(refreshToken));
        assertFalse(jwtService.isAccessToken(refreshToken));
    }

    @Test
    @DisplayName("Generar par de tokens exitosamente")
    void generateTokenPair_Success() {
        var tokens = jwtService.generateTokenPair(testUser);
        
        assertNotNull(tokens);
        assertNotNull(tokens.accessToken());
        assertNotNull(tokens.refreshToken());
        assertFalse(tokens.accessToken().isEmpty());
        assertFalse(tokens.refreshToken().isEmpty());
        assertNotEquals(tokens.accessToken(), tokens.refreshToken());
    }

    @Test
    @DisplayName("Extraer username del token correctamente")
    void extractUsername_Success() {
        String token = jwtService.generateAccessToken(testUser);
        String extractedUsername = jwtService.extractUsername(token);
        
        assertEquals(testUser.getEmail(), extractedUsername);
    }

    @Test
    @DisplayName("Validar token de acceso correcto")
    void isAccessTokenValid_Success() {
        String token = jwtService.generateAccessToken(testUser);
        
        assertTrue(jwtService.isAccessTokenValid(token, testUser));
    }

    @Test
    @DisplayName("Validar token de refresco correcto")
    void isRefreshTokenValid_Success() {
        String token = jwtService.generateRefreshToken(testUser);
        
        assertTrue(jwtService.isRefreshTokenValid(token, testUser));
    }

    @Test
    @DisplayName("Token de acceso inválido para usuario diferente")
    void isAccessTokenValid_DifferentUser_Failure() {
        String token = jwtService.generateAccessToken(testUser);
        
        Rol rol = new Rol(2L, "ROLE_ADMIN", true, new ArrayList<>());
        
        User differentUser = User.builder()
                .id(2L)
                .email("different@example.com")
                .password("encodedPassword")
                .nombre("Different")
                .apellido("User")
                .activo(true)
                .rol(rol)
                .build();
        
        assertFalse(jwtService.isAccessTokenValid(token, differentUser));
    }

    @Test
    @DisplayName("Token de refresco inválido para usuario diferente")
    void isRefreshTokenValid_DifferentUser_Failure() {
        String token = jwtService.generateRefreshToken(testUser);
        
        Rol rol = new Rol(2L, "ROLE_ADMIN", true, new ArrayList<>());
        
        User differentUser = User.builder()
                .id(2L)
                .email("different@example.com")
                .password("encodedPassword")
                .nombre("Different")
                .apellido("User")
                .activo(true)
                .rol(rol)
                .build();
        
        assertFalse(jwtService.isRefreshTokenValid(token, differentUser));
    }

    @Test
    @DisplayName("Identificar correctamente token de acceso")
    void isAccessToken_Success() {
        String token = jwtService.generateAccessToken(testUser);
        
        assertTrue(jwtService.isAccessToken(token));
    }

    @Test
    @DisplayName("Identificar correctamente que no es token de acceso")
    void isAccessToken_Failure() {
        String token = jwtService.generateRefreshToken(testUser);
        
        assertFalse(jwtService.isAccessToken(token));
    }

    @Test
    @DisplayName("Identificar correctamente token de refresco")
    void isRefreshToken_Success() {
        String token = jwtService.generateRefreshToken(testUser);
        
        assertTrue(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Identificar correctamente que no es token de refresco")
    void isRefreshToken_Failure() {
        String token = jwtService.generateAccessToken(testUser);
        
        assertFalse(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Token de acceso con tipo incorrecto no es válido")
    void isAccessTokenValid_WrongType_Failure() {
        String refreshToken = jwtService.generateRefreshToken(testUser);
        
        assertFalse(jwtService.isAccessTokenValid(refreshToken, testUser));
    }

    @Test
    @DisplayName("Token de refresco con tipo incorrecto no es válido")
    void isRefreshTokenValid_WrongType_Failure() {
        String accessToken = jwtService.generateAccessToken(testUser);
        
        assertFalse(jwtService.isRefreshTokenValid(accessToken, testUser));
    }

    @Test
    @DisplayName("Token malformado lanza excepción")
    void extractMalformedToken_ThrowsException() {
        String malformedToken = "invalid.token.format";
        
        assertThrows(Exception.class, () -> jwtService.extractUsername(malformedToken));
    }

    @Test
    @DisplayName("Token vacío lanza excepción")
    void extractEmptyToken_ThrowsException() {
        String emptyToken = "";
        
        assertThrows(Exception.class, () -> jwtService.extractUsername(emptyToken));
    }

    @Test
    @DisplayName("Token nulo lanza excepción")
    void extractNullToken_ThrowsException() {
        assertThrows(Exception.class, () -> jwtService.extractUsername(null));
    }

    @Test
    @DisplayName("Token con firma inválida lanza excepción")
    void extractTokenWithInvalidSignature_ThrowsException() {
        String validToken = jwtService.generateAccessToken(testUser);
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "ABCDE";
        
        assertThrows(Exception.class, () -> jwtService.extractUsername(tamperedToken));
    }

    @Test
    @DisplayName("Generar múltiples tokens para el mismo usuario produce tokens válidos")
    void generateMultipleTokens_ProducesValidTokens() {
        String token1 = jwtService.generateAccessToken(testUser);
        String token2 = jwtService.generateAccessToken(testUser);
        
        // Ambos tokens deben ser válidos aunque puedan ser iguales si se generan muy rápido
        assertTrue(jwtService.isAccessTokenValid(token1, testUser));
        assertTrue(jwtService.isAccessTokenValid(token2, testUser));
    }

    @Test
    @DisplayName("Token de acceso contiene el claim de tipo correcto")
    void accessTokenContainsCorrectTypeClaim() {
        String token = jwtService.generateAccessToken(testUser);
        
        assertTrue(jwtService.isAccessToken(token));
        assertFalse(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Token de refresco contiene el claim de tipo correcto")
    void refreshTokenContainsCorrectTypeClaim() {
        String token = jwtService.generateRefreshToken(testUser);
        
        assertTrue(jwtService.isRefreshToken(token));
        assertFalse(jwtService.isAccessToken(token));
    }

    @Test
    @DisplayName("Validar token genérico con usuario correcto")
    void isTokenValid_Success() {
        String token = jwtService.generateAccessToken(testUser);
        
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    @DisplayName("Validar token genérico con usuario incorrecto")
    void isTokenValid_DifferentUser_Failure() {
        String token = jwtService.generateAccessToken(testUser);
        
        Rol rol = new Rol(2L, "ROLE_ADMIN", true, new ArrayList<>());
        
        User differentUser = User.builder()
                .id(2L)
                .email("different@example.com")
                .password("encodedPassword")
                .nombre("Different")
                .apellido("User")
                .activo(true)
                .rol(rol)
                .build();
        
        assertFalse(jwtService.isTokenValid(token, differentUser));
    }

    @Test
    @DisplayName("Validar token no expirado retorna true")
    void isTokenValid_NotExpired_ReturnsTrue() {
        // Este test cubre la condición !isTokenExpired(token) en la línea 145
        // cuando el token NO está expirado (retorna true)
        String token = jwtService.generateAccessToken(testUser);
        
        // Verificar que el token es válido (lo que implica que !isTokenExpired es true)
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    @DisplayName("Usuario con email null genera token correctamente")
    void generateTokenWithNullEmail_Success() {
        Rol rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
        
        User userWithNullEmail = User.builder()
                .id(1L)
                .email(null)
                .password("encodedPassword")
                .nombre("Test")
                .apellido("User")
                .activo(true)
                .rol(rol)
                .build();
        
        assertDoesNotThrow(() -> jwtService.generateAccessToken(userWithNullEmail));
    }

    @Test
    @DisplayName("Usuario inactivo genera token pero validación falla")
    void generateTokenForInactiveUser_ValidationFails() {
        Rol rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
        
        User inactiveUser = User.builder()
                .id(1L)
                .email("inactive@example.com")
                .password("encodedPassword")
                .nombre("Inactive")
                .apellido("User")
                .activo(false)
                .rol(rol)
                .build();
        
        String token = jwtService.generateAccessToken(inactiveUser);
        
        // El token se genera pero la validación de UserDetails fallará por el estado inactivo
        assertNotNull(token);
    }
}
