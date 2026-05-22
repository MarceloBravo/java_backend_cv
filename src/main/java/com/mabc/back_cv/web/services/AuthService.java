package com.mabc.back_cv.web.services;

import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.UserRepository;
import com.mabc.back_cv.web.repositories.RolRepository;
import org.springframework.security.authentication.AuthenticationManager;
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

    public String register(User request) {
        // Buscamos el rol por defecto en la BD. Si no existe, lanzamos una excepción.
        Rol defaultRol = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: El rol 'ROLE_USER' no está inicializado en la base de datos."));

        // Aprovechando que pusimos @Builder en la entidad User gracias a Lombok:
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(defaultRol) // Asignamos el OBJETO Rol completo
                .build();
        
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return jwtService.generateToken(user);
    }
}