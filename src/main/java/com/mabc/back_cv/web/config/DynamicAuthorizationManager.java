package com.mabc.back_cv.web.config;

import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.PermisoPantallaRepository;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Gestor de autorización dinámica que implementa {@link AuthorizationManager}.
 * Se encarga de verificar si un usuario autenticado tiene permisos para acceder a un recurso (URL)
 * y realizar una acción específica (método HTTP) según la configuración de permisos
 * asociada a su rol en la base de datos.
 */
@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    /**
     * Matcher de rutas utilizado para comparar los patrones de URL guardados en los permisos.
     */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * Repositorio para la consulta de permisos de pantalla.
     */
    private final PermisoPantallaRepository permisoPantallaRepository;

    /**
     * Constructor de la clase.
     *
     * @param permisoPantallaRepository El repositorio de permisos de pantalla.
     */
    public DynamicAuthorizationManager(PermisoPantallaRepository permisoPantallaRepository) {
        this.permisoPantallaRepository = permisoPantallaRepository;
    }

    /**
     * Verifica si el usuario autenticado tiene acceso al contexto de la solicitud HTTP actual.
     * Excluye de la validación a los endpoints públicos de autenticación (ej. /api/auth/*)
     * y comprueba los permisos del rol del usuario para el método HTTP y la URI solicitados.
     *
     * @param authentication Proveedor del objeto {@link Authentication} del usuario actual.
     * @param context El contexto de autorización de la solicitud HTTP.
     * @return Una decisión de autorización {@link AuthorizationDecision} que indica si se permite o no el acceso.
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        HttpServletRequest request = context.getRequest();
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/api/auth/")) {
            return new AuthorizationDecision(true);
        }

        Long roleId = extractRoleId(auth);
        if (roleId == null) {
            return new AuthorizationDecision(false);
        }

        Optional<PermisoPantalla> permiso = findMatchingPermission(roleId, requestUri);

        boolean granted = permiso
                .map(p -> hasPermissionForMethod(request.getMethod(), p, requestUri))
                .orElse(false);

        return new AuthorizationDecision(granted);
    }

    private Long extractRoleId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user && user.getRol() != null) {
            return user.getRol().getId();
        }

        return null;
    }

    private Optional<PermisoPantalla> findMatchingPermission(Long roleId, String requestUri) {
        String ruta = requestUri.endsWith("/") ? requestUri.substring(0, requestUri.length() - 1) : requestUri;
        List<PermisoPantalla> permisos = permisoPantallaRepository.findAllActiveByRolIdAndMenuUrl(roleId, ruta);
        return permisos.stream()
                .filter(pp -> pp.getPantalla() != null)
                .filter(pp -> PATH_MATCHER.match(pp.getPantalla().getMenu().getUrl(), ruta))
                .findFirst();
    }

    private boolean hasPermissionForMethod(String method, PermisoPantalla permiso, String requestUri) {
        if (requestUri.endsWith("/")) {
            requestUri = requestUri.substring(0, requestUri.length() - 1);
        }
        return switch (method) {
            case "POST" -> Boolean.TRUE.equals(permiso.getAccion_crear());
            case "PUT", "PATCH" -> Boolean.TRUE.equals(permiso.getAccion_editar());
            case "DELETE" -> Boolean.TRUE.equals(permiso.getAccion_eliminar());
            case "GET" -> isDetailRequest(requestUri)
                    ? Boolean.TRUE.equals(permiso.getAccion_consultar())
                    : Boolean.TRUE.equals(permiso.getListar());
            default -> false;
        };
    }

    private boolean isDetailRequest(String requestUri) {
        return requestUri.matches(".*/\\d+$");
    }
}
