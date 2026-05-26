package com.mabc.back_cv.entities;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.UserPresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity - Tests Unitarios")
class UserTest {

    private Rol rol;
    private User user;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());

        user = User.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .fono("+56912345678")
                .direccion("Av. Siempre Viva 123")
                .ciudad("Santiago")
                .idioma("Español")
                .password("hashedPassword123")
                .activo(true)
                .rol(rol)
                .build();
    }

    // -------------------------------------------------------------------------
    // Builder y constructores
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un User con todos los campos usando @Builder")
        void shouldBuildUserWithAllFields() {
            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("Juan", user.getNombre());
            assertEquals("Pérez", user.getApellido());
            assertEquals("juan.perez@example.com", user.getEmail());
            assertEquals("+56912345678", user.getFono());
            assertEquals("Av. Siempre Viva 123", user.getDireccion());
            assertEquals("Santiago", user.getCiudad());
            assertEquals("Español", user.getIdioma());
            assertEquals("hashedPassword123", user.getPassword());
            assertTrue(user.getActivo());
            assertEquals(rol, user.getRol());
        }

        @Test
        @DisplayName("El campo 'activo' debe ser TRUE por defecto usando @Builder.Default")
        void shouldSetActivoTrueByDefault() {
            User userSinActivo = User.builder()
                    .nombre("Ana")
                    .apellido("García")
                    .email("ana@example.com")
                    .password("pass")
                    .rol(rol)
                    .build();

            assertTrue(userSinActivo.getActivo(),
                    "El campo 'activo' debe valer true por defecto al usar el builder");
        }

        @Test
        @DisplayName("Debe crear un User vacío con @NoArgsConstructor")
        void shouldCreateUserWithNoArgsConstructor() {
            User emptyUser = new User();
            assertNotNull(emptyUser);
            assertNull(emptyUser.getId());
            assertNull(emptyUser.getNombre());
            assertNull(emptyUser.getEmail());
        }

        @Test
        @DisplayName("Debe crear un User con @AllArgsConstructor")
        void shouldCreateUserWithAllArgsConstructor() {
            List<UserPresentation> parrafos = new ArrayList<>();
            User fullUser = new User(2L, "María", "López", "maria@example.com",
                    "555-1234", "Calle 9", "Valparaíso", "Inglés",
                    "secret", false, rol, parrafos);

            assertEquals(2L, fullUser.getId());
            assertEquals("María", fullUser.getNombre());
            assertEquals("López", fullUser.getApellido());
            assertEquals("maria@example.com", fullUser.getEmail());
            assertFalse(fullUser.getActivo());
        }
    }

    // -------------------------------------------------------------------------
    // Getters y Setters (Lombok)
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Getters y Setters")
    class GettersAndSetters {

        @Test
        @DisplayName("Debe actualizar campos mediante setters")
        void shouldUpdateFieldsWithSetters() {
            user.setNombre("Carlos");
            user.setApellido("Soto");
            user.setEmail("carlos.soto@example.com");
            user.setFono("+56998765432");
            user.setDireccion("Nueva Dirección 456");
            user.setCiudad("Concepción");
            user.setIdioma("Francés");
            user.setPassword("newPassword");
            user.setActivo(false);

            assertEquals("Carlos", user.getNombre());
            assertEquals("Soto", user.getApellido());
            assertEquals("carlos.soto@example.com", user.getEmail());
            assertEquals("+56998765432", user.getFono());
            assertEquals("Nueva Dirección 456", user.getDireccion());
            assertEquals("Concepción", user.getCiudad());
            assertEquals("Francés", user.getIdioma());
            assertEquals("newPassword", user.getPassword());
            assertFalse(user.getActivo());
        }

        @Test
        @DisplayName("parrafos debe ser null cuando se construye con @Builder sin @Builder.Default")
        void parrafosShouldBeNullWhenBuiltWithBuilder() {
            // @Builder no usa el inicializador del campo a menos que se anote con
            // @Builder.Default.
            // Este test documenta ese comportamiento conocido de Lombok.
            assertNull(user.getParrafos(),
                    "Sin @Builder.Default, el campo 'parrafos' queda null al usar el builder");
        }

        @Test
        @DisplayName("Debe gestionar la lista de parrafos mediante setter")
        void shouldManageParrafosListViaSetter() {
            List<UserPresentation> parrafos = new ArrayList<>();
            parrafos.add(new UserPresentation());
            user.setParrafos(parrafos);

            assertNotNull(user.getParrafos());
            assertEquals(1, user.getParrafos().size());
        }

        @Test
        @DisplayName("parrafos se inicializa como lista vacía con @NoArgsConstructor")
        void parrafosShouldBeInitializedWithNoArgsConstructor() {
            User emptyUser = new User();
            // Con el constructor vacío el campo usa el inicializador: = new ArrayList<>()
            assertNotNull(emptyUser.getParrafos());
            assertTrue(emptyUser.getParrafos().isEmpty());
        }
    }

    // -------------------------------------------------------------------------
    // UserDetails
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Implementación de UserDetails")
    class UserDetailsImplementation {

        @Test
        @DisplayName("getUsername() debe retornar el email")
        void getUsernameShouldReturnEmail() {
            assertEquals("juan.perez@example.com", user.getUsername());
        }

        @Test
        @DisplayName("getPassword() debe retornar el password")
        void getPasswordShouldReturnPassword() {
            assertEquals("hashedPassword123", user.getPassword());
        }

        @Test
        @DisplayName("getAuthorities() debe retornar la autoridad basada en el nombre del rol")
        void getAuthoritiesShouldReturnRolAuthority() {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            assertNotNull(authorities);
            assertEquals(1, authorities.size());
            assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
        }

        @Test
        @DisplayName("isAccountNonExpired() debe retornar true cuando activo=true")
        void isAccountNonExpiredShouldReturnTrueWhenActivo() {
            assertTrue(user.isAccountNonExpired());
        }

        @Test
        @DisplayName("isAccountNonExpired() debe retornar false cuando activo=false")
        void isAccountNonExpiredShouldReturnFalseWhenInactivo() {
            user.setActivo(false);
            assertFalse(user.isAccountNonExpired());
        }

        @Test
        @DisplayName("isAccountNonLocked() debe retornar true cuando activo=true")
        void isAccountNonLockedShouldReturnTrueWhenActivo() {
            assertTrue(user.isAccountNonLocked());
        }

        @Test
        @DisplayName("isAccountNonLocked() debe retornar false cuando activo=false")
        void isAccountNonLockedShouldReturnFalseWhenInactivo() {
            user.setActivo(false);
            assertFalse(user.isAccountNonLocked());
        }

        @Test
        @DisplayName("isCredentialsNonExpired() debe retornar true cuando activo=true")
        void isCredentialsNonExpiredShouldReturnTrueWhenActivo() {
            assertTrue(user.isCredentialsNonExpired());
        }

        @Test
        @DisplayName("isCredentialsNonExpired() debe retornar false cuando activo=false")
        void isCredentialsNonExpiredShouldReturnFalseWhenInactivo() {
            user.setActivo(false);
            assertFalse(user.isCredentialsNonExpired());
        }

        @Test
        @DisplayName("isEnabled() debe retornar true cuando activo=true")
        void isEnabledShouldReturnTrueWhenActivo() {
            assertTrue(user.isEnabled());
        }

        @Test
        @DisplayName("isEnabled() debe retornar false cuando activo=false")
        void isEnabledShouldReturnFalseWhenInactivo() {
            user.setActivo(false);
            assertFalse(user.isEnabled());
        }

        @Test
        @DisplayName("Todos los métodos de estado deben retornar false cuando activo=null")
        void allStatusMethodsShouldReturnFalseWhenActivoIsNull() {
            user.setActivo(null);
            assertFalse(user.isAccountNonExpired());
            assertFalse(user.isAccountNonLocked());
            assertFalse(user.isCredentialsNonExpired());
            assertFalse(user.isEnabled());
        }
    }

    // -------------------------------------------------------------------------
    // equals() y hashCode()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("equals() y hashCode()")
    class EqualsAndHashCode {

        @Test
        @DisplayName("Dos users con el mismo email deben ser iguales")
        void userWithSameEmailShouldBeEqual() {
            User other = User.builder()
                    .id(99L)
                    .nombre("Otro")
                    .apellido("Apellido")
                    .email("juan.perez@example.com")
                    .password("otraPass")
                    .rol(rol)
                    .build();

            assertEquals(user, other);
        }

        @Test
        @DisplayName("Dos users con distinto email NO deben ser iguales")
        void userWithDifferentEmailShouldNotBeEqual() {
            User other = User.builder()
                    .email("otro@example.com")
                    .password("pass")
                    .rol(rol)
                    .build();

            assertNotEquals(user, other);
        }

        @Test
        @DisplayName("Un user debe ser igual a sí mismo")
        void userShouldEqualItself() {
            assertEquals(user, user);
        }

        @Test
        @DisplayName("Un user no debe ser igual a null")
        void userShouldNotEqualNull() {
            assertFalse(user.equals(null));
        }

        @Test
        @DisplayName("Un user no debe ser igual a un objeto de distinta clase")
        void userShouldNotEqualDifferentClass() {
            assertFalse(user.equals(new Object()));
        }

        @Test
        @DisplayName("hashCode debe ser igual para users con el mismo email")
        void sameEmailShouldProduceSameHashCode() {
            User other = User.builder()
                    .email("juan.perez@example.com")
                    .password("pass")
                    .rol(rol)
                    .build();

            assertEquals(user.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("hashCode debe ser 0 cuando el email es null")
        void hashCodeShouldBeZeroWhenEmailIsNull() {
            User userSinEmail = new User();
            assertEquals(0, userSinEmail.hashCode());
        }

        @Test
        @DisplayName("equals debe retornar false cuando el email del user es null")
        void equalsShouldReturnFalseWhenEmailIsNull() {
            User userSinEmail = new User();
            assertNotEquals(userSinEmail, user);
        }
    }

    // -------------------------------------------------------------------------
    // toString()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("toString()")
    class ToStringMethod {

        @Test
        @DisplayName("toString() debe contener los campos principales")
        void toStringShouldContainMainFields() {
            String result = user.toString();

            assertNotNull(result);
            assertTrue(result.contains("Juan"), "Debe contener el nombre");
            assertTrue(result.contains("Pérez"), "Debe contener el apellido");
            assertTrue(result.contains("juan.perez@example.com"), "Debe contener el email");
            assertTrue(result.contains("Santiago"), "Debe contener la ciudad");
        }

        @Test
        @DisplayName("toString() NO debe exponer el password")
        void toStringShouldNotExposePassword() {
            String result = user.toString();
            assertFalse(result.contains("hashedPassword123"),
                    "El password NO debe aparecer en toString()");
        }
    }
}
