package com.mabc.back_cv.web.controllers.auth;

import com.mabc.back_cv.web.dto.RefreshTokenRequest;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mabc.back_cv.web.dto.CredencialesDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;

import java.util.Map;

/**
 * Controlador REST encargado de manejar los endpoints de autenticación y
 * registro de usuarios.
 * Mapea las solicitudes que inician con /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Servicio que contiene la lógica de negocio para la autenticación y gestión de
     * usuarios.
     */
    private final AuthService authService;

    /**
     * Constructor de la clase.
     *
     * @param authService El servicio de autenticación.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user Objeto {@link User} con los datos de registro (email, password,
     *             etc.).
     * @return Una respuesta HTTP que contiene el token de acceso y de refresco
     *         recién generados.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UsuarioDTO user) {
        try {
            return ResponseEntity.ok(authService.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Autentica a un usuario existente utilizando sus credenciales (email y
     * password).
     *
     * @param user Objeto {@link User} con las credenciales de inicio de sesión.
     * @return Una respuesta HTTP que contiene el token de acceso y de refresco
     *         generados.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody CredencialesDTO credencialesDTO) {
        try {
            return ResponseEntity.ok(authService.authenticate(credencialesDTO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Genera un nuevo par de tokens de acceso y refresco a partir de un token de
     * refresco válido.
     *
     * @param request La solicitud {@link RefreshTokenRequest} que incluye el token
     *                de refresco actual.
     * @return Una respuesta HTTP con los nuevos tokens generados.
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(authService.refreshTokens(request.refreshToken()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
