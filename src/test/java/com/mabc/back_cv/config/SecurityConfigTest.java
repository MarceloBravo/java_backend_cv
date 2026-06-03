package com.mabc.back_cv.config;

import com.mabc.back_cv.web.config.DynamicAuthorizationManager;
import com.mabc.back_cv.web.config.JwtAuthenticationFilter;
import com.mabc.back_cv.web.config.SecurityConfig;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de SecurityConfig")
class SecurityConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private JwtAuthenticationFilter jwtAuthFilter;

    @Mock
    private DynamicAuthorizationManager dynamicAuthorizationManager;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("Carga detalles de usuario cuando existe el email en la base de datos")
    void cargaDetallesDeUsuarioCuandoExisteEmail() {
        User usuario = new User();
        usuario.setEmail("usuario@example.com");
        usuario.setPassword("pass");
        usuario.setActivo(true);
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        usuario.setRol(rol);

        when(userRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        var userDetails = securityConfig.userDetailsService().loadUserByUsername("usuario@example.com");

        assertNotNull(userDetails);
        assertEquals("usuario@example.com", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());
    }

    @Test
    @DisplayName("Lanza UsernameNotFoundException cuando el email no existe")
    void lanzaUsernameNotFoundExceptionCuandoEmailNoExiste() {
        when(userRepository.findByEmail("usuario@example.com")).thenReturn(Optional.empty());

        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> securityConfig.userDetailsService().loadUserByUsername("usuario@example.com"));
    }

    @Test
    @DisplayName("PasswordEncoder utiliza BCrypt y puede verificar contraseñas")
    void passwordEncoderUtilizaBCrypt() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "miPassword123";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$") || encoded.startsWith("$2y$"));
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    @DisplayName("AuthenticationProvider autentica credenciales válidas usando el servicio de usuario")
    void authenticationProviderAutenticaCredencialesValidas() {
        User usuario = new User();
        usuario.setEmail("usuario@example.com");
        usuario.setPassword(securityConfig.passwordEncoder().encode("passwordValido"));
        usuario.setActivo(true);
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        usuario.setRol(rol);

        when(userRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        AuthenticationProvider provider = securityConfig.authenticationProvider();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("usuario@example.com",
                "passwordValido");

        var authentication = provider.authenticate(authRequest);

        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals("usuario@example.com", authentication.getName());
    }

    @Test
    @DisplayName("AuthenticationProvider rechaza credenciales inválidas")
    void authenticationProviderRechazaCredencialesInvalidas() {
        User usuario = new User();
        usuario.setEmail("usuario@example.com");
        usuario.setPassword(securityConfig.passwordEncoder().encode("passwordValido"));
        usuario.setActivo(true);
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        usuario.setRol(rol);

        when(userRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        AuthenticationProvider provider = securityConfig.authenticationProvider();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("usuario@example.com",
                "passwordIncorrecto");

        assertThrows(BadCredentialsException.class, () -> provider.authenticate(authRequest));
    }

    @Test
    @DisplayName("Devuelve AuthenticationManager a partir de AuthenticationConfiguration")
    void devuelveAuthenticationManagerDesdeConfiguration() throws Exception {
        AuthenticationManager manager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(manager);

        assertSame(manager, securityConfig.authenticationManager(authenticationConfiguration));
    }

    @Test
    @DisplayName("Configura correctamente la cadena de filtros de seguridad")
    void configuraSecurityFilterChain() throws Exception {
        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(eq(jwtAuthFilter), eq(UsernamePasswordAuthenticationFilter.class)))
                .thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(filterChain);

        assertSame(filterChain,
                securityConfig.securityFilterChain(httpSecurity, jwtAuthFilter, dynamicAuthorizationManager));

        verify(httpSecurity).csrf(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).authenticationProvider(any());
        verify(httpSecurity).addFilterBefore(eq(jwtAuthFilter), eq(UsernamePasswordAuthenticationFilter.class));
        verify(httpSecurity).build();
    }
}