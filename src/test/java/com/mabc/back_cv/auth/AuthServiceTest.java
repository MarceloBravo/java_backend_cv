package com.mabc.back_cv.auth;

import com.mabc.back_cv.web.dto.CredencialesDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.repositories.UserRepository;
import com.mabc.back_cv.web.services.auth.AuthService;
import com.mabc.back_cv.web.services.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de AuthService")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Rol testRol;
    private UsuarioDTO testRegisterRequest;
    private CredencialesDTO testCredentials;
    private com.mabc.back_cv.web.dto.AuthTokens testTokens;

    @BeforeEach
    void setUp() {
        testRol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("rawPassword")
                .nombre("Test")
                .apellido("User")
                .activo(true)
                .rol(testRol)
                .build();

        testRegisterRequest = new UsuarioDTO();
        testRegisterRequest.setEmail("test@example.com");
        testRegisterRequest.setPassword("rawPassword");
        testRegisterRequest.setNombre("Test");
        testRegisterRequest.setApellido("User");
        testRegisterRequest.setActivo(true);
        testRegisterRequest.setRol(testRol);

        testCredentials = new CredencialesDTO("test@example.com", "rawPassword");

        testTokens = new com.mabc.back_cv.web.dto.AuthTokens(
                "testAccessToken",
                "testRefreshToken"
        );
    }

    @Test
    @DisplayName("Registrar usuario exitosamente")
    void register_Success() {
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.register(testRegisterRequest);

        assertNotNull(result);
        assertEquals("testAccessToken", result.get("accessToken"));
        assertEquals("testRefreshToken", result.get("refreshToken"));

        verify(rolRepository, times(1)).findByNombre("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateTokenPair(testUser);
    }

    @Test
    @DisplayName("Registrar usuario cuando rol ROLE_USER no existe lanza excepción")
    void register_RolNotFound_ThrowsException() {
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(testRegisterRequest);
        });

        assertEquals("Error: El rol 'ROLE_USER' no está inicializado en la base de datos.", exception.getMessage());
        verify(rolRepository, times(1)).findByNombre("ROLE_USER");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registrar usuario con valores opcionales nulos usa valores por defecto")
    void register_WithNullOptionalFields_UsesDefaults() {
        UsuarioDTO userWithNulls = new UsuarioDTO();
        userWithNulls.setEmail("test@example.com");
        userWithNulls.setPassword("rawPassword");
        userWithNulls.setNombre(null);
        userWithNulls.setApellido(null);
        userWithNulls.setRol(testRol);

        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.register(userWithNulls);

        assertNotNull(result);
        verify(userRepository, times(1)).save(argThat(user ->
                "Usuario".equals(user.getNombre()) && "Nuevo".equals(user.getApellido())
        ));
    }

    @Test
    @DisplayName("Registrar usuario con valores opcionales proporcionados usa esos valores")
    void register_WithOptionalFields_UsesProvidedValues() {
        UsuarioDTO userWithValues = new UsuarioDTO();
        userWithValues.setEmail("test@example.com");
        userWithValues.setPassword("rawPassword");
        userWithValues.setNombre("Juan");
        userWithValues.setApellido("Perez");
        userWithValues.setFono("123456789");
        userWithValues.setDireccion("Calle 123");
        userWithValues.setCiudad("Madrid");
        userWithValues.setIdioma("es");
        userWithValues.setRol(testRol);

        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.register(userWithValues);

        assertNotNull(result);
        verify(userRepository, times(1)).save(argThat(user ->
                "Juan".equals(user.getNombre()) &&
                        "Perez".equals(user.getApellido()) &&
                        "123456789".equals(user.getFono()) &&
                        "Calle 123".equals(user.getDireccion()) &&
                        "Madrid".equals(user.getCiudad()) &&
                        "es".equals(user.getIdioma())
        ));
    }

    @Test
    @DisplayName("Autenticar usuario exitosamente")
    void authenticate_Success() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.authenticate(testCredentials);

        assertNotNull(result);
        assertEquals("testAccessToken", result.get("accessToken"));
        assertEquals("testRefreshToken", result.get("refreshToken"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Autenticar con credenciales inválidas lanza excepción")
    void authenticate_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticate(testCredentials);
        });

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Autenticar con usuario no encontrado lanza excepción")
    void authenticate_UserNotFound_ThrowsException() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            authService.authenticate(testCredentials);
        });

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, never()).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Refrescar tokens exitosamente")
    void refreshTokens_Success() {
        String refreshToken = "validRefreshToken";
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isRefreshTokenValid(refreshToken, testUser)).thenReturn(true);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.refreshTokens(refreshToken);

        assertNotNull(result);
        assertEquals("testAccessToken", result.get("accessToken"));
        assertEquals("testRefreshToken", result.get("refreshToken"));

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).isRefreshTokenValid(refreshToken, testUser);
        verify(jwtService, times(1)).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Refrescar tokens con usuario no encontrado lanza excepción")
    void refreshTokens_UserNotFound_ThrowsException() {
        String refreshToken = "validRefreshToken";
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.refreshTokens(refreshToken);
        });

        assertEquals("Refresh token inválido", exception.getMessage());
        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, never()).isRefreshTokenValid(anyString(), any(User.class));
    }

    @Test
    @DisplayName("Refrescar tokens con token inválido lanza excepción")
    void refreshTokens_InvalidToken_ThrowsException() {
        String refreshToken = "invalidRefreshToken";
        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isRefreshTokenValid(refreshToken, testUser)).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.refreshTokens(refreshToken);
        });

        assertEquals("Refresh token inválido o expirado", exception.getMessage());
        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).isRefreshTokenValid(refreshToken, testUser);
        verify(jwtService, never()).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Refrescar tokens con token expirado lanza excepción")
    void refreshTokens_ExpiredToken_ThrowsException() {
        String expiredToken = "expiredRefreshToken";
        when(jwtService.extractUsername(expiredToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isRefreshTokenValid(expiredToken, testUser)).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.refreshTokens(expiredToken);
        });

        assertEquals("Refresh token inválido o expirado", exception.getMessage());
        verify(jwtService, times(1)).extractUsername(expiredToken);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).isRefreshTokenValid(expiredToken, testUser);
    }

    @Test
    @DisplayName("Refrescar tokens con token de acceso en lugar de refresco lanza excepción")
    void refreshTokens_AccessTokenInsteadOfRefresh_ThrowsException() {
        String accessToken = "accessToken";
        when(jwtService.extractUsername(accessToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isRefreshTokenValid(accessToken, testUser)).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.refreshTokens(accessToken);
        });

        assertEquals("Refresh token inválido o expirado", exception.getMessage());
    }

    @Test
    @DisplayName("Registrar usuario con email null")
    void register_WithNullEmail_Success() {
        UsuarioDTO userWithNullEmail = new UsuarioDTO();
        userWithNullEmail.setEmail(null);
        userWithNullEmail.setPassword("rawPassword");

        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.register(userWithNullEmail);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Registrar usuario con password null")
    void register_WithNullPassword_Success() {
        UsuarioDTO userWithNullPassword = new UsuarioDTO();
        userWithNullPassword.setEmail("test@example.com");
        userWithNullPassword.setPassword(null);

        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(null)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.register(userWithNullPassword);

        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(null);
    }

    @Test
    @DisplayName("Autenticar usuario con email null")
    void authenticate_WithNullEmail_Success() {
        CredencialesDTO credentialsWithNullEmail = new CredencialesDTO(null, "rawPassword");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(null)).thenReturn(Optional.of(testUser));
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        Map<String, String> result = authService.authenticate(credentialsWithNullEmail);

        assertNotNull(result);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Refrescar tokens con token null lanza excepción")
    void refreshTokens_WithNullToken_ThrowsException() {
        assertThrows(Exception.class, () -> {
            authService.refreshTokens(null);
        });
    }

    @Test
    @DisplayName("Refrescar tokens con token vacío lanza excepción")
    void refreshTokens_WithEmptyToken_ThrowsException() {
        assertThrows(Exception.class, () -> {
            authService.refreshTokens("");
        });
    }

    @Test
    @DisplayName("Registrar usuario verifica que se establece activo=true")
    void register_SetsActiveToTrue() {
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        authService.register(testRegisterRequest);

        verify(userRepository, times(1)).save(argThat(user ->
                Boolean.TRUE.equals(user.getActivo())
        ));
    }

    @Test
    @DisplayName("Registrar usuario asigna el rol correctamente")
    void register_AssignsRoleCorrectly() {
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(testTokens);

        authService.register(testRegisterRequest);

        verify(userRepository, times(1)).save(argThat(user ->
                user.getRol() != null && "ROLE_USER".equals(user.getRol().getNombre())
        ));
    }

    @Test
    @DisplayName("Registrar usuario cuando save retorna null lanza excepción")
    void register_SaveReturnsNull_ThrowsException() {
        when(rolRepository.findByNombre("ROLE_USER")).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(testRegisterRequest);
        });

        assertEquals("Error: No se pudo crear el usuario.", exception.getMessage());
        verify(rolRepository, times(1)).findByNombre("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, never()).generateTokenPair(any(User.class));
    }

    @Test
    @DisplayName("Autenticar usuario con password incorrecto lanza excepción")
    void authenticate_WrongPassword_ThrowsException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticate(testCredentials);
        });

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Refrescar tokens genera nuevo par de tokens")
    void refreshTokens_GeneratesNewTokenPair() {
        String refreshToken = "validRefreshToken";
        com.mabc.back_cv.web.dto.AuthTokens newTokens = new com.mabc.back_cv.web.dto.AuthTokens(
                "newAccessToken",
                "newRefreshToken"
        );

        when(jwtService.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isRefreshTokenValid(refreshToken, testUser)).thenReturn(true);
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(newTokens);

        Map<String, String> result = authService.refreshTokens(refreshToken);

        assertEquals("newAccessToken", result.get("accessToken"));
        assertEquals("newRefreshToken", result.get("refreshToken"));
        verify(jwtService, times(1)).generateTokenPair(any(User.class));
    }
}
