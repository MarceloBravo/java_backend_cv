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

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final PermisoPantallaRepository permisoPantallaRepository;

    public DynamicAuthorizationManager(PermisoPantallaRepository permisoPantallaRepository) {
        this.permisoPantallaRepository = permisoPantallaRepository;
    }

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
