package com.mabc.back_cv.web.config;

import com.mabc.back_cv.web.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Continúa la cadena de filtros cuando no hay encabezado Authorization")
    void continuaCuandoNoHayAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Continúa la cadena de filtros cuando el encabezado Authorization no empieza con Bearer")
    void continuaCuandoAuthorizationNoEmpiezaConBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");
        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Autentica al usuario cuando el token es válido y no existe autenticación previa")
    void autenticaUsuarioCuandoTokenValido() throws ServletException, IOException {
        String token = "token-valido";
        String username = "usuario@example.com";
        UserDetails userDetails = new User(username, "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isAccessTokenValid(token, userDetails)).thenReturn(true);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals(username, authentication.getName());
        assertFalse(authentication.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("No autentica al usuario cuando el token es inválido")
    void noAutenticaCuandoTokenInvalido() throws ServletException, IOException {
        String token = "token-invalido";
        String username = "usuario@example.com";
        UserDetails userDetails = new User(username, "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isAccessTokenValid(token, userDetails)).thenReturn(false);

        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("No intenta autenticar cuando ya existe una autenticación en el contexto")
    void noAutenticaCuandoYaHayAutenticacion() throws ServletException, IOException {
        String token = "token-valido";
        String username = "usuario@example.com";
        Authentication existingAuth = new UsernamePasswordAuthenticationToken("existing", null, List.of());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).isAccessTokenValid(anyString(), any());
        assertSame(existingAuth, SecurityContextHolder.getContext().getAuthentication());
    }
}
