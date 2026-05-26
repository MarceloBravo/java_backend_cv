package com.mabc.back_cv.entities;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.Presentacion;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Presentacion Entity - Tests Unitarios")
public class PresentacionTest {
    
    private Presentacion presentacion;
    private User user;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, rol, new ArrayList<>());
        presentacion = new Presentacion(1L, "Hola, soy Juan Pérez", user);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {
        @Test
        @DisplayName("Debe construir una Presentacion con todos los campos usando @Builder")
        void shouldBuildPresentacionWithAllFields() {
            assertNotNull(presentacion);
            assertEquals(1L, presentacion.getId());
            assertEquals("Hola, soy Juan Pérez", presentacion.getParrafo());
            assertEquals(user, presentacion.getUser()); 
        }   

        @Nested
        @DisplayName("Getters y Setters")
        class GettersAndSetters {
            @Test
            @DisplayName("Debe actualizar campos mediante setters")
            void shouldUpdateFieldsWithSetters() {
                presentacion.setParrafo("Hola, soy Juan Pérez actualizado");
                presentacion.setUser(user);

                assertEquals(1L, presentacion.getId());
                assertEquals("Hola, soy Juan Pérez actualizado", presentacion.getParrafo());
                assertEquals(user, presentacion.getUser());
            }

            @Test
            @DisplayName("Debe obtener campos mediante getters")
            void shouldGetFields() {
                assertEquals(1L, presentacion.getId());
                assertEquals("Hola, soy Juan Pérez", presentacion.getParrafo());
                assertEquals(user, presentacion.getUser());
            }
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToString {
        @Test
        @DisplayName("Debe generar el String")
        void shouldGenerateToString() {
            String expected = "Presentacion{id=1, parrafo='Hola, soy Juan Pérez'}";
            assertEquals(expected, presentacion.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(presentacion.equals(presentacion));
        }

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Presentacion otraPresentacion = new Presentacion(1L, "Hola, soy Juan Pérez", user);
            assertTrue(presentacion.equals(otraPresentacion));
            assertTrue(otraPresentacion.equals(presentacion));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo parrafo si el id es distinto")
        void shouldNotBeEqualWhenOnlyParrafoMatches() {
            Presentacion otraPresentacion = new Presentacion(2L, "Hola, soy Juan Pérez", user);
            assertFalse(presentacion.equals(otraPresentacion));
            assertFalse(otraPresentacion.equals(presentacion));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo user si el id es distinto")
        void shouldNotBeEqualWhenOnlyUserMatches() {
            Presentacion otraPresentacion = new Presentacion(3L, "Hola, soy Juan Pérez", user);
            assertFalse(presentacion.equals(otraPresentacion));
            assertFalse(otraPresentacion.equals(presentacion));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es distinto aunque compartan parrafo")
        void shouldBeDifferentWhenIdDiffers() {
            Presentacion otraPresentacion = new Presentacion(3L, "Hola, soy Juan Pérez", new User(2L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, rol, new ArrayList<>()));
            assertFalse(presentacion.equals(otraPresentacion));
            assertFalse(otraPresentacion.equals(presentacion));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es nulo")
        void shouldBeDifferentWhenIdIsNull() {
            Presentacion otraPresentacion = new Presentacion(null, "Hola, soy Juan Pérez", new User(2L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, rol, new ArrayList<>()));
            assertFalse(presentacion.equals(otraPresentacion));
            assertFalse(otraPresentacion.equals(presentacion));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(presentacion.equals("otro objeto"));
            assertFalse(presentacion.equals(null));
        }

        @Test
        @DisplayName("Debe generar un hascode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertTrue(presentacion.hashCode() != 0);
        }

        @Test
        @DisplayName("Debe generar un hascode igual a 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Presentacion presentacion = new Presentacion(null, "Hola, soy Juan Pérez", user);
            assertEquals(0, presentacion.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hascode igual a otro cuando el id es el mismo")
        void shouldGenerateHashCodeEqualWhenIdIsSame() {
            Presentacion otraPresentacion = new Presentacion(1L, "Hola, soy Juan Pérez", user);
            assertEquals(presentacion.hashCode(), otraPresentacion.hashCode());
        }
    }
}
