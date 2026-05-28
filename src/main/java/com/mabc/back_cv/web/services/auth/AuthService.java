package com.mabc.back_cv.web.services.auth;

import com.mabc.back_cv.web.dto.AuthTokens;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.repositories.UserRepository;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la lógica de negocio asociada a la autenticación de
 * usuarios.
 * Proporciona métodos para registrar nuevos usuarios, autenticar usuarios
 * existentes
 * y renovar los tokens utilizando un token de refresco.
 */
@Service
public class AuthService {

    /**
     * Repositorio para la gestión de usuarios en base de datos.
     */
    private final UserRepository userRepository;

    /**
     * Repositorio para la gestión de roles en base de datos.
     */
    private final RolRepository rolRepository;

    /**
     * Codificador para encriptar y verificar contraseñas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Servicio para la creación y validación de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Gestor de autenticación de Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor de la clase.
     *
     * @param userRepository        Repositorio de usuarios.
     * @param rolRepository         Repositorio de roles.
     * @param passwordEncoder       Codificador de contraseñas.
     * @param jwtService            Servicio JWT.
     * @param authenticationManager Gestor de autenticación.
     */
    public AuthService(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder,
            JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario en el sistema con el rol predeterminado
     * 'ROLE_USER'.
     * Encripta la contraseña antes de guardar el usuario en la base de datos.
     *
     * @param request Objeto {@link User} con la información del registro.
     * @return Un mapa que asocia las claves "accessToken" y "refreshToken" con sus
     *         respectivos valores.
     * @throws RuntimeException Si el rol predeterminado no se encuentra
     *                          inicializado en la base de datos.
     */
    public Map<String, String> register(User request) {
        Rol defaultRol = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException(
                        "Error: El rol 'ROLE_USER' no está inicializado en la base de datos."));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre() != null ? request.getNombre() : "Usuario")
                .apellido(request.getApellido() != null ? request.getApellido() : "Nuevo")
                .fono(request.getFono())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .idioma(request.getIdioma())
                .activo(true)
                .rol(defaultRol)
                .build();
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new RuntimeException("Error: No se pudo crear el usuario.");
        }
        return toTokenResponse(jwtService.generateTokenPair(savedUser));
    }

    /**
     * Autentica a un usuario utilizando sus credenciales de inicio de sesión.
     * Llama al gestor de autenticación de Spring Security y, si tiene éxito, genera
     * un nuevo par de tokens.
     *
     * @param request Objeto {@link User} que contiene el email y la contraseña.
     * @return Un mapa que asocia las claves "accessToken" y "refreshToken" con sus
     *         respectivos valores.
     */
    public Map<String, String> authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return toTokenResponse(jwtService.generateTokenPair(user));
    }

    /**
     * Renueva el par de tokens utilizando un token de refresco válido.
     *
     * @param refreshToken El token de refresco actual.
     * @return Un mapa que asocia las claves "accessToken" y "refreshToken" con sus
     *         respectivos valores.
     * @throws BadCredentialsException Si el token es inválido, ha expirado o el
     *                                 usuario asociado no existe.
     */
    public Map<String, String> refreshTokens(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Refresh token inválido o expirado");
        }

        return toTokenResponse(jwtService.generateTokenPair(user));
    }

    /**
     * Convierte un objeto {@link AuthTokens} en un mapa con formato JSON de
     * respuesta.
     *
     * @param tokens El par de tokens.
     * @return Un mapa que asocia las claves "accessToken" y "refreshToken" con sus
     *         respectivos valores.
     */
    private Map<String, String> toTokenResponse(AuthTokens tokens) {
        return Map.of(
                "accessToken", tokens.accessToken(),
                "refreshToken", tokens.refreshToken());
    }
}
