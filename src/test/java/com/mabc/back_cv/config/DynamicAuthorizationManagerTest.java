package com.mabc.back_cv.config;

import com.mabc.back_cv.web.config.DynamicAuthorizationManager;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.PermisoPantallaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de DynamicAuthorizationManager")
class DynamicAuthorizationManagerTest {

    @Mock
    private PermisoPantallaRepository permisoPantallaRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RequestAuthorizationContext context;

    @InjectMocks
    private DynamicAuthorizationManager dynamicAuthorizationManager;

    private Supplier<Authentication> authenticationSupplier;
    private Authentication authentication;

    @BeforeEach
    void setup() {
        authentication = mock(Authentication.class);
        authenticationSupplier = () -> authentication;
        lenient().when(context.getRequest()).thenReturn(request);
    }

    @Test
    @DisplayName("Permite acceso a endpoints públicos de autenticación")
    void permiteAccesoAEndpointsPublicosDeAutenticacion() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
        verifyNoInteractions(permisoPantallaRepository);
    }

    @Test
    @DisplayName("Deniega acceso cuando el usuario no está autenticado")
    void deniegaAccesoCuandoUsuarioNoEstaAutenticado() {
        when(authentication.isAuthenticated()).thenReturn(false);

        assertFalse(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
        verifyNoInteractions(permisoPantallaRepository);
    }

    @Test
    @DisplayName("Deniega acceso cuando el usuario no tiene rol asignado")
    void deniegaAccesoCuandoUsuarioNoTieneRolAsignado() {
        User user = User.builder().rol(null).build();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/protected");

        assertFalse(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
        verifyNoInteractions(permisoPantallaRepository);
    }

    @Test
    @DisplayName("Deniega acceso cuando no existe un permiso coincidente para la URL")
    void deniegaAccesoCuandoNoExistePermisoCoincidente() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/protected")).thenReturn(List.of());

        assertFalse(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso GET a lista cuando el permiso listar está activo")
    void concedeAccesoGetListaCuandoPermisoListarActivo() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos", false, false, false, false, true);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos");
        when(request.getMethod()).thenReturn("GET");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso GET a detalle cuando el permiso consultar está activo")
    void concedeAccesoGetDetalleCuandoPermisoConsultarActivo() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos/*", false, false, false, true, false);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos/42");
        when(request.getMethod()).thenReturn("GET");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos/42")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Deniega acceso POST cuando no existe permiso crear")
    void deniegaAccesoPostSinPermisoCrear() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos", false, false, false, false, true);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos");
        when(request.getMethod()).thenReturn("POST");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permiso));

        assertFalse(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso POST cuando existe permiso crear")
    void concedeAccesoPostCuandoExistePermisoCrear() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos", true, false, false, false, false);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos");
        when(request.getMethod()).thenReturn("POST");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Deniega acceso para métodos HTTP no soportados")
    void deniegaAccesoParaMetodosHttpNoSoportados() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos", true, true, true, true, true);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos");
        when(request.getMethod()).thenReturn("HEAD");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permiso));

        assertFalse(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso GET a lista cuando la URL termina en slash")
    void concedeAccesoGetListaCuandoUrlTerminaEnSlash() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos", false, false, false, false, true);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos/");
        when(request.getMethod()).thenReturn("GET");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso PUT cuando existe permiso editar")
    void concedeAccesoPutCuandoExistePermisoEditar() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos/*", false, true, false, false, false);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos/42");
        when(request.getMethod()).thenReturn("PUT");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos/42")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Concede acceso DELETE cuando existe permiso eliminar")
    void concedeAccesoDeleteCuandoExistePermisoEliminar() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permiso = permisoDeRuta("/api/recursos/42", false, false, true, false, false);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos/42");
        when(request.getMethod()).thenReturn("DELETE");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos/42")).thenReturn(List.of(permiso));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    @Test
    @DisplayName("Ignora permisos con pantalla nula y aún así concede acceso cuando existe otra coincidencia")
    void concedeAccesoIgnorandoPermisosConPantallaNula() {
        User user = User.builder().rol(new Rol(1L, "ROLE_USER", true, null)).build();
        PermisoPantalla permisoNulo = new PermisoPantalla();
        permisoNulo.setPantalla(null);
        PermisoPantalla permisoValido = permisoDeRuta("/api/recursos", false, false, false, false, true);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/recursos");
        when(request.getMethod()).thenReturn("GET");
        when(permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(1L, "/api/recursos")).thenReturn(List.of(permisoNulo, permisoValido));

        assertTrue(dynamicAuthorizationManager.check(authenticationSupplier, context).isGranted());
    }

    private PermisoPantalla permisoDeRuta(String menuUrl,
                                         Boolean crear,
                                         Boolean editar,
                                         Boolean eliminar,
                                         Boolean consultar,
                                         Boolean listar) {
        Menu menu = new Menu();
        menu.setUrl(menuUrl);
        Pantalla pantalla = new Pantalla();
        pantalla.setMenu(menu);

        PermisoPantalla permiso = new PermisoPantalla();
        permiso.setPantalla(pantalla);
        permiso.setAccion_crear(crear);
        permiso.setAccion_editar(editar);
        permiso.setAccion_eliminar(eliminar);
        permiso.setAccion_consultar(consultar);
        permiso.setListar(listar);
        return permiso;
    }
}
