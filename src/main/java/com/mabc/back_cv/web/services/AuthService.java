package com.mabc.back_cv.web.services;

import com.mabc.back_cv.web.dto.AuthTokens;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthTokens register(User request) {
        Rol defaultRol = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: El rol 'ROLE_USER' no está inicializado en la base de datos."));

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

        userRepository.save(user);
        return jwtService.generateTokenPair(user);
    }

    public AuthTokens authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return jwtService.generateTokenPair(user);
    }

    public AuthTokens refreshTokens(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Refresh token inválido o expirado");
        }

        return jwtService.generateTokenPair(user);
    }
}
