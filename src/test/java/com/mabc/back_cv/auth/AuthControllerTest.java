package com.mabc.back_cv.auth;

import com.mabc.back_cv.web.controllers.auth.AuthController;
import com.mabc.back_cv.web.dto.RefreshTokenRequest;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("AuthController Tests")
class AuthControllerTest{

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Evalúa el método login con credenciales válidas")
    void testLogin_ValidCredentials() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("user@example.com");
        when(user.getPassword()).thenReturn("password");
        when(authService.authenticate(user)).thenReturn(Map.of("accessToken", "token", "refreshToken", "token"));

        ResponseEntity<Map<String, String>> response = authController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().get("accessToken"));
        assertEquals("token", response.getBody().get("refreshToken"));
    }

    @Test
    @DisplayName("Evalúa el método login con credenciales inválidas")
    void testLogin_InvalidCredentials() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("user@example.com");
        when(user.getPassword()).thenReturn("wrong-password");
        when(authService.authenticate(user)).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<Map<String, String>> response = authController.login(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().get("error"));
    }
    /*
    @Test
    @DisplayName("Evalúa el método logout")
    void testLogout(
        @Mock HttpServletRequest request,
        @Mock HttpServletResponse response
    ) {
        // Configurar el comportamiento del servicio de autenticación
        doNothing().when(authService).logout(request, response);
        // Llamar al método a probar
        authController.logout(request, response);
        // Verificar que se haya llamado al servicio de autenticación
        verify(authService, times(1)).logout(request, response);
    } 
    */

   @Test
   @DisplayName("Evalúa el método refreshToken con un token de refresco válido")
   void testRefreshToken_ValidRefreshToken() {
       RefreshTokenRequest refreshTokenRequest = mock(RefreshTokenRequest.class);
       when(refreshTokenRequest.refreshToken()).thenReturn("validRefreshToken");
       when(authService.refreshTokens("validRefreshToken")).thenReturn(Map.of("accessToken", "newAccessToken", "refreshToken", "newRefreshToken"));

       ResponseEntity<Map<String, String>> response = authController.refresh(refreshTokenRequest);

       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals("newAccessToken", response.getBody().get("accessToken"));
       assertEquals("newRefreshToken", response.getBody().get("refreshToken"));
   } 

   @Test
    @DisplayName("Evalúa el método refreshToken con un token de refresco inválido")
    void testRefreshToken_InvalidRefreshToken() {
        RefreshTokenRequest refreshTokenRequest = mock(RefreshTokenRequest.class);
        when(refreshTokenRequest.refreshToken()).thenReturn("invalidRefreshToken");
        when(authService.refreshTokens("invalidRefreshToken")).thenThrow(new RuntimeException("Invalid refresh token"));

        ResponseEntity<Map<String, String>> response = authController.refresh(refreshTokenRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Invalid refresh token", response.getBody().get("error"));
    }

    @Test
    @DisplayName("Testea el método register con datos de usuario válidos")
    void testRegister_ValidUserData() {
        User user = mock(User.class);
        when(authService.register(user)).thenReturn(Map.of("accessToken", "token", "refreshToken", "token"));

        ResponseEntity<Map<String, String>> response = authController.register(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().get("accessToken"));
        assertEquals("token", response.getBody().get("refreshToken"));
    }

    @Test
    @DisplayName("Testea el método register con datos de usuario inválidos")
    void testRegister_InvalidUserData() {
        User user = mock(User.class);
        when(authService.register(user)).thenThrow(new RuntimeException("Invalid user data"));

        ResponseEntity<Map<String, String>> response = authController.register(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Invalid user data", response.getBody().get("error"));
    }


}   