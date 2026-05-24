package com.mabc.back_cv.web.config;

import com.mabc.back_cv.web.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad principal de Spring Security.
 * Define la cadena de filtros de seguridad, el servicio de carga de usuarios,
 * el proveedor de autenticación y el codificador de contraseñas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Repositorio para realizar consultas a la tabla de usuarios.
     */
    private final UserRepository userRepository;

    /**
     * Constructor de la clase.
     *
     * @param userRepository El repositorio de usuarios.
     */
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Define y configura la cadena de filtros de seguridad (Security Filter Chain).
     * Configura el manejo de CSRF (deshabilitado), las políticas de sesión (STATELESS),
     * las reglas de autorización (autenticación dinámica para endpoints protegidos y
     * acceso público para autenticación) y registra el filtro JWT.
     *
     * @param http                        Configurador de seguridad HTTP.
     * @param jwtAuthFilter               Filtro de autenticación basado en JWT.
     * @param dynamicAuthorizationManager Manejador de autorización dinámica.
     * @return La cadena de filtros de seguridad construida.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter,
            DynamicAuthorizationManager dynamicAuthorizationManager) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF ya que usamos JWT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Rutas públicas (Login/Registro)
                .anyRequest().access(dynamicAuthorizationManager) // Autorización dinámica por DB
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin estado (sin HTTP Session)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Inyectamos nuestro filtro

        return http.build();
    }

    /**
     * Define el servicio para cargar los detalles del usuario a partir de su nombre de usuario (email).
     *
     * @return Implementación personalizada de {@link UserDetailsService}.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * Configura el proveedor de autenticación predeterminado usando {@link DaoAuthenticationProvider}.
     *
     * @return El proveedor de autenticación configurado con el servicio de usuario y el codificador de contraseñas.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expone el {@link AuthenticationManager} predeterminado de Spring Security.
     *
     * @param config Configuración de autenticación de Spring.
     * @return El gestor de autenticación.
     * @throws Exception Si ocurre un error al obtener el gestor.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el bean para el codificador de contraseñas utilizando {@link BCryptPasswordEncoder}.
     *
     * @return El codificador de contraseñas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación robusta para contraseñas
    }
}