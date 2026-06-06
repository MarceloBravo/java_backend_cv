package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.DescripcionPortafolio;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.entities.User;

@DisplayName("Portafolio Entity - Tests Unitarios")
public class PortafolioTest {

    private Portafolio portafolio;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123",
                "Santiago", "español", "1234567890", true, null, new ArrayList<>());
        portafolio = new Portafolio(1L, "Título 1", "Imagen 1", "Video 1", "Mouse Move Title 1",
                "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1", new ArrayList<>(), user);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Portafolio con todos los campos")
        void shouldBuildPortafolioWithAllFields() {
            assertNotNull(portafolio);
            assertEquals(1L, portafolio.getId());
            assertEquals("Título 1", portafolio.getTitle());
            assertEquals("Imagen 1", portafolio.getImage());
            assertEquals("Video 1", portafolio.getVideo());
            assertEquals("Mouse Move Title 1", portafolio.getMouseMoveTitle());
            assertEquals("Mouse Move Description 1", portafolio.getMouseMoveDescription());
            assertEquals("Parrafo Inferior 1", portafolio.getParagraph());
            assertEquals("Link 1", portafolio.getLink());
            assertNotNull(portafolio.getDescription());
            assertTrue(portafolio.getDescription().isEmpty());
            assertEquals(user, portafolio.getUser());
        }

        @Test
        @DisplayName("Debe crear un Portafolio vacío con @NoArgsConstructor")
        void shouldCreatePortafolioWithNoArgsConstructor() {
            Portafolio emptyPortafolio = new Portafolio();

            assertNotNull(emptyPortafolio);
            assertNull(emptyPortafolio.getId());
            assertNull(emptyPortafolio.getTitle());
            assertNotNull(emptyPortafolio.getDescription());
            assertTrue(emptyPortafolio.getDescription().isEmpty());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            portafolio.setTitle("Título actualizado");
            portafolio.setImage("Imagen actualizada");
            portafolio.setVideo("Video actualizado");
            portafolio.setMouseMoveTitle("Mouse Title actualizado");
            portafolio.setMouseMoveDescription("Mouse Description actualizado");
            portafolio.setParagraph("Parrafo actualizado");
            portafolio.setLink("Link actualizado");
            portafolio.setUser(user);

            ArrayList<DescripcionPortafolio> descripciones = new ArrayList<>();
            descripciones.add(new DescripcionPortafolio(1L, "Descripción 1", 1, portafolio));
            portafolio.setDescription(descripciones);

            assertEquals("Título actualizado", portafolio.getTitle());
            assertEquals("Imagen actualizada", portafolio.getImage());
            assertEquals("Video actualizado", portafolio.getVideo());
            assertEquals("Mouse Title actualizado", portafolio.getMouseMoveTitle());
            assertEquals("Mouse Description actualizado", portafolio.getMouseMoveDescription());
            assertEquals("Parrafo actualizado", portafolio.getParagraph());
            assertEquals("Link actualizado", portafolio.getLink());
            assertEquals(user, portafolio.getUser());
            assertEquals(1, portafolio.getDescription().size());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1L, portafolio.getId());
            assertEquals("Título 1", portafolio.getTitle());
            assertEquals("Imagen 1", portafolio.getImage());
            assertEquals("Video 1", portafolio.getVideo());
            assertEquals("Mouse Move Title 1", portafolio.getMouseMoveTitle());
            assertEquals("Mouse Move Description 1", portafolio.getMouseMoveDescription());
            assertEquals("Parrafo Inferior 1", portafolio.getParagraph());
            assertEquals("Link 1", portafolio.getLink());
            assertEquals(user, portafolio.getUser());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Portafolio otroPortafolio = new Portafolio(1L, "Otro título", "Otra imagen", "Otro video",
                    "Otro mouse title", "Otra mouse description", "Otro parrafo", "Otro link",
                    new ArrayList<>(), user);
            assertEquals(portafolio, otroPortafolio);
            assertEquals(otroPortafolio, portafolio);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es distinto")
        void shouldNotBeEqualWhenIdIsDifferent() {
            Portafolio otroPortafolio = new Portafolio(2L, "Título 1", "Imagen 1", "Video 1",
                    "Mouse Move Title 1", "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1",
                    new ArrayList<>(), user);
            assertNotEquals(portafolio, otroPortafolio);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertFalse(portafolio.equals(null));
            assertFalse(portafolio.equals("objeto"));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo título si el id es distinto")
        void shouldNotBeEqualWhenOnlyTitleMatches() {
            Portafolio otroPortafolio = new Portafolio(2L, "Título 1", "Otra imagen", "Otro video",
                    "Otro mouse title", "Otra mouse description", "Otro parrafo", "Otro link",
                    new ArrayList<>(), user);
            assertFalse(portafolio.equals(otroPortafolio));
            assertFalse(otroPortafolio.equals(portafolio));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Portafolio otroPortafolio = new Portafolio(null, "Título 1", "Imagen 1", "Video 1",
                    "Mouse Move Title 1", "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1",
                    new ArrayList<>(), user);
            assertFalse(portafolio.equals(otroPortafolio));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Portafolio portafolioSinId = new Portafolio(null, "Título 1", "Imagen 1", "Video 1",
                    "Mouse Move Title 1", "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1",
                    new ArrayList<>(), user);
            assertFalse(portafolioSinId.equals(portafolio));
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertTrue(portafolio.hashCode() != 0);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Portafolio sinId = new Portafolio();
            assertEquals(0, sinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Portafolio otroPortafolio = new Portafolio(1L, "Otro título", "Otra imagen", "Otro video",
                    "Otro mouse title", "Otra mouse description", "Otro parrafo", "Otro link",
                    new ArrayList<>(), user);
            assertEquals(portafolio.hashCode(), otroPortafolio.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Portafolio otroPortafolio = new Portafolio(2L, "Título 1", "Imagen 1", "Video 1",
                    "Mouse Move Title 1", "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1",
                    new ArrayList<>(), user);
            assertNotEquals(portafolio.hashCode(), otroPortafolio.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String expected = "Portafolio{id=1, title='Título 1', image='Imagen 1', video='Video 1', "
                    + "mouseMoveTitle='Mouse Move Title 1', mouseMoveDescription='Mouse Move Description 1', "
                    + "paragraph='Parrafo Inferior 1', link='Link 1'}";
            assertEquals(expected, portafolio.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(portafolio.equals(portafolio));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(portafolio.equals("otro objeto"));
            assertFalse(portafolio.equals(null));
        }
    }
}
