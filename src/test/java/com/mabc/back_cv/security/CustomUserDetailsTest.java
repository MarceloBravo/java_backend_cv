package com.mabc.back_cv.security;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.security.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CustomUserDetails Tests")
class CustomUserDetailsTest {

    private User user;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ROLE_USER", true, null);
        user = User.builder()
            .email("usuario@example.com")
            .password("1234")
            .activo(true)
            .rol(rol)
            .build();
    }

    @Test
    @DisplayName("getUsername and getPassword return the user email and password")
    void shouldReturnUsernameAndPasswordFromUser() {
        
        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getUsername()).isEqualTo("usuario@example.com");
        assertThat(details.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("getAuthorities returns a granted authority when role is present")
    void shouldReturnAuthorityWhenRoleIsPresent() {
        Rol rol = new Rol(1L, "ROLE_ADMIN", true, null);
        User user = User.builder()
                .email("admin@example.com")
                .password("1234")
                .activo(true)
                .rol(rol)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("getAuthorities returns empty list when role is null")
    void shouldReturnEmptyAuthoritiesWhenRoleIsNull() {
        User user = User.builder()
                .email("sinrol@example.com")
                .password("1234")
                .activo(true)
                .rol(null)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("getAuthorities returns empty list when role name is null")
    void shouldReturnEmptyAuthoritiesWhenRoleNameIsNull() {
        Rol rol = new Rol(1L, null, true, null);
        User user = User.builder()
                .email("sinrolnombre@example.com")
                .password("1234")
                .activo(true)
                .rol(rol)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("account status methods are true when user is active")
    void shouldMarkAccountAsNonExpiredNonLockedCredentialsNonExpiredAndEnabledWhenActive() {
        User user = User.builder()
                .email("activo@example.com")
                .password("1234")
                .activo(true)
                .rol(rol)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("account status methods are false when user is inactive")
    void shouldMarkAccountAsExpiredLockedCredentialsExpiredAndDisabledWhenInactive() {
        User user = User.builder()
                .email("inactivo@example.com")
                .password("1234")
                .activo(false)
                .rol(rol)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.isAccountNonExpired()).isFalse();
        assertThat(details.isAccountNonLocked()).isFalse();
        assertThat(details.isCredentialsNonExpired()).isFalse();
        assertThat(details.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("equals and hashCode depend on the wrapped user")
    void shouldCompareCustomUserDetailsByWrappedUser() {
        User userA = User.builder()
                .email("igual@example.com")
                .password("1234")
                .activo(true)
                .rol(rol)
                .build();

        User userB = User.builder()
                .email("igual@example.com")
                .password("otra")
                .activo(false)
                .rol(rol)
                .build();

        CustomUserDetails detailsA = new CustomUserDetails(userA);
        CustomUserDetails detailsB = new CustomUserDetails(userB);

        assertThat(detailsA).isEqualTo(detailsB);
        assertThat(detailsA.hashCode()).isEqualTo(detailsB.hashCode());
    }

    @Test
    @DisplayName("methods throw NullPointerException when wrapped user is null")
    void shouldThrowWhenUserIsNull() {
        CustomUserDetails details = new CustomUserDetails(null);

        assertThatThrownBy(details::getUsername).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(details::getPassword).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(details::getAuthorities).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(details::isEnabled).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("toString does not throw and returns a non-empty string")
    void shouldHaveToStringThatDoesNotThrow() {

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.toString()).isNotBlank();
    }


    @Test
    @DisplayName("Debe devolver el usuario envuelto")
    void shouldReturnWrappedUser() {

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Debe compaar si el objeto es una instancia de sí mismo")
    void shouldCompareWithItself() {

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details).isEqualTo(details);
    }

     @Test  
    @DisplayName("Debe comparar con null y devolver false")
    void shouldCompareWithNullAndReturnFalse() {    
        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details).isNotEqualTo(null);
    }

     @Test
    @DisplayName("Debe comparar con un objeto de otra clase y devolver false")
    void shouldCompareWithDifferentClassAndReturnFalse() {
        CustomUserDetails details = new CustomUserDetails(user);
        Object other = new Object();

        assertThat(details).isNotEqualTo(other);
    }

     @Test
    @DisplayName("Debe comparar con otro CustomUserDetails con el mismo usuario y devolver true")
    void shouldCompareWithAnotherCustomUserDetailsWithSameUserAndReturnTrue() {
        CustomUserDetails detailsA = new CustomUserDetails(user);
        CustomUserDetails detailsB = new CustomUserDetails(user);

        assertThat(detailsA).isEqualTo(detailsB);
    }
    
     @Test
    @DisplayName("Debe comparar con otro CustomUserDetails con un usuario diferente y devolver false")
    void shouldCompareWithAnotherCustomUserDetailsWithDifferentUserAndReturnFalse() {
        User otherUser = User.builder()
                .email("otro@example.com")
                .password("1234")
                .activo(true)
                .rol(rol)       
                .build();
        CustomUserDetails detailsA = new CustomUserDetails(user);
        CustomUserDetails detailsB = new CustomUserDetails(otherUser);  

        assertThat(detailsA).isNotEqualTo(detailsB);
    }
}
