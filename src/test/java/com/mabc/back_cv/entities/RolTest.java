package com.mabc.back_cv.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

@DisplayName("Rol Entity - Tests Unitarios")
public class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {
        @Test
        void shouldBuildUserWithAllFields() {
            assertNotNull(rol);
            assertEquals(1L, rol.getId());
            assertEquals("ROLE_USER", rol.getNombre());
            assertTrue(rol.getActivo());
            assertEquals(new ArrayList<User>(), rol.getUsers());
        }

        @Nested
        @DisplayName("Getters y Setters")
        class GettersAndSetters {

            @Test
            @DisplayName("Debe actualizar campos mediante setters")
            void shouldUpdateFieldsWithSetters() {
                rol.setId(2L);
                rol.setNombre("ROLE_ADMIN");
                rol.setActivo(false);

                assertEquals(2L, rol.getId());
                assertEquals("ROLE_ADMIN", rol.getNombre());
                assertFalse(rol.getActivo());
            }

            @Test
            @DisplayName("Debe obtener campos mediante getters")
            void shouldGetFields() {

                assertEquals(1L, rol.getId());
                assertEquals("ROLE_USER", rol.getNombre());
                assertTrue(rol.getActivo());
            }
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToString {
        @Test
        @DisplayName("Debe generar el String")
        void shouldGenerateToString() {
            String expected = "Rol{id=1, nombre='ROLE_USER', activo=true}";
            assertEquals(expected, rol.toString());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsAndHashCode {
        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(rol.equals(rol));
        }

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo nombre no nulo")
        void shouldBeEqualWhenSameNonNullNombre() {
            Rol otroRolMismoNombre = new Rol(2L, "ROLE_USER", false, new ArrayList<>());

            assertTrue(rol.equals(otroRolMismoNombre));
            assertTrue(otroRolMismoNombre.equals(rol));
        }

        @Test
        @DisplayName("Debe comparar roles por nombre")
        void compareRolesByNameAndBeEquals() {
            Rol rol2 = new Rol(2L, "ROLE_ADMIN", true, new ArrayList<>());
            Rol rolConNombreNull = new Rol(3L, null, true, new ArrayList<>());
            Rol otroRolConNombreNull = new Rol(4L, null, true, new ArrayList<>());

            assertFalse(rol.equals(rol2));
            assertFalse(rol.equals(null));
            assertFalse(rol.equals(2L));
            assertFalse(rol.equals(rolConNombreNull));
            assertFalse(rolConNombreNull.equals(rol));
            assertFalse(rolConNombreNull.equals(otroRolConNombreNull));
        }

        @Test
        @DisplayName("Debe comparar hashCode por nombre")
        void compareRolesByNameHashCode() {
            Rol mismoNombre = new Rol(2L, "ROLE_USER", false, new ArrayList<>());
            Rol distintoNombre = new Rol(3L, "ROLE_ADMIN", true, new ArrayList<>());
            Rol nombreNull = new Rol(4L, null, true, new ArrayList<>());

            assertEquals(rol.hashCode(), mismoNombre.hashCode());
            assertNotEquals(rol.hashCode(), distintoNombre.hashCode());
            assertEquals(0, nombreNull.hashCode());
        }

        @Test
        @DisplayName("Debe generar string de la clase")
        void generateToString() {
            Rol rol2 = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
            Rol rol3 = new Rol(3L, "ROLE_ADMIN", true, new ArrayList<>());

            assertEquals(rol.toString(), rol2.toString());
            assertNotEquals(rol.toString(), rol3.toString());
            assertNotEquals(rol.toString(), 2L);
        }
    }
}
