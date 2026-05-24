package com.mabc.back_cv.web.security;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Implementación personalizada de {@link UserDetails} para adaptar la entidad {@link User}
 * al modelo de seguridad de Spring Security.
 */
public class CustomUserDetails implements UserDetails {

    /**
     * La entidad {@link User} de la base de datos asociada a este UserDetails.
     */
    private final User user;

    /**
     * Constructor de la clase.
     *
     * @param user El usuario de la base de datos.
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Obtiene los privilegios o roles concedidos al usuario.
     * En este caso, retorna una lista con el nombre del rol asignado.
     *
     * @return Colección de {@link GrantedAuthority} que representan los roles del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Rol rol = user.getRol();
        if (rol == null || rol.getNombre() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre()));
    }

    /**
     * Obtiene la contraseña encriptada del usuario.
     *
     * @return Contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Obtiene el identificador principal del usuario (en este caso, su dirección de correo electrónico).
     *
     * @return El correo electrónico del usuario.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indica si la cuenta del usuario ha expirado.
     *
     * @return true si la cuenta está activa y no ha expirado, false de lo contrario.
     */
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getActivo());
    }

    /**
     * Indica si el usuario está bloqueado o desbloqueado.
     *
     * @return true si el usuario está activo y no bloqueado, false de lo contrario.
     */
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getActivo());
    }

    /**
     * Indica si las credenciales del usuario (contraseña) han expirado.
     *
     * @return true si las credenciales están vigentes, false de lo contrario.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getActivo());
    }

    /**
     * Indica si el usuario está habilitado.
     *
     * @return true si el usuario está activo y habilitado, false de lo contrario.
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getActivo());
    }

    /**
     * Obtiene la entidad {@link User} original asociada.
     *
     * @return Objeto {@link User} de la base de datos.
     */
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
