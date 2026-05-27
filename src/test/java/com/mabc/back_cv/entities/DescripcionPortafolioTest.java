package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.DescripcionPortafolio;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.entities.User;

@DisplayName("DescripcionPortafolio Entity - Tests Unitarios")
public class DescripcionPortafolioTest {
    
    private DescripcionPortafolio descripcionPortafolio;
    private Portafolio portafolio;
    private User user;

    @BeforeEach
    void setUp(){
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, null, null);
        portafolio = new Portafolio(1L, "Título 1", "Imagen 1", "Video 1", "Mouse Move Title 1", "Mouse Move Description 1", "Parrafo Inferior 1", "Link 1", new ArrayList<>(), user);
        descripcionPortafolio = new DescripcionPortafolio(1L, "Parrafo 1", 1, portafolio);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {
        @Test
        @DisplayName("Debe construir un Objeto DescripcionPortafolio con todos sus campos")
        void shuldBuildDescripcionPortafolioWithAllFields() {
            assertNotNull(descripcionPortafolio);
            assertEquals(1L, descripcionPortafolio.getId());
            assertEquals("Parrafo 1", descripcionPortafolio.getParrafo());
            assertEquals(1, descripcionPortafolio.getPosicion());
            assertEquals(portafolio, descripcionPortafolio.getPortafolio());
        }   

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            descripcionPortafolio.setParrafo("Parrafo 2");
            descripcionPortafolio.setPosicion(2);
            descripcionPortafolio.setPortafolio(portafolio);

            assertEquals("Parrafo 2", descripcionPortafolio.getParrafo());
            assertEquals(2, descripcionPortafolio.getPosicion());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1, descripcionPortafolio.getId());
            assertEquals("Parrafo 1", descripcionPortafolio.getParrafo());
            assertEquals(1, descripcionPortafolio.getPosicion());
            assertEquals(descripcionPortafolio.getPortafolio().equals(portafolio), true);
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHasdhcode {
        @Test
        @DisplayName("Debe generar el String")
        void shouldGenerateToString() {
            String expected = "DescripcionPortafolio{id=1, parrafo='Parrafo 1', posicion=1}";
            assertEquals(expected, descripcionPortafolio.toString());
        }

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(1L, "Parrafo 1", 1, portafolio);
            assertTrue(descripcionPortafolio.equals(otraDescripcionPortafolio));
            assertTrue(otraDescripcionPortafolio.equals(descripcionPortafolio));           
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo parrafo si el id es distinto")
        void shouldNotBeEqualWhenOnlyParrafoMatches() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(2L, "Parrafo 1", 1, portafolio);
            assertFalse(descripcionPortafolio.equals(otraDescripcionPortafolio));
            assertFalse(otraDescripcionPortafolio.equals(descripcionPortafolio));           
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo posicion si el id es distinto")
        void shouldNotBeEqualWhenOnlyPosicionMatches() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(2L, "Parrafo 1", 1, portafolio);
            assertFalse(descripcionPortafolio.equals(otraDescripcionPortafolio));
            assertFalse(otraDescripcionPortafolio.equals(descripcionPortafolio));           
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo portafolio si el id es distinto")
        void shouldNotBeEqualWhenOnlyPortafolioMatches() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(2L, "Parrafo 1", 1, portafolio);
            assertFalse(descripcionPortafolio.equals(otraDescripcionPortafolio));
            assertFalse(otraDescripcionPortafolio.equals(descripcionPortafolio));           
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es nulo")
        void shouldBeDifferentWhenIdDiffers() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(null, "Parrafo 1", 1, portafolio);
            assertFalse(descripcionPortafolio.equals(otraDescripcionPortafolio));
            assertFalse(otraDescripcionPortafolio.equals(descripcionPortafolio));           
        }

        @Test
        @DisplayName("Debe generar un hascode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertTrue(descripcionPortafolio.hashCode() != 0);
        }

        @Test
        @DisplayName("Debe generar un hascode igual a 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(null, "Parrafo 1", 1, portafolio);
            assertEquals(otraDescripcionPortafolio.hashCode(), 0);
        }

        @Test
        @DisplayName("Debe generar un hascode igual a otro cuando el id es el mismo")
        void shouldGenerateHashCodeEqualWhenIdIsSame() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(1L, "Parrafo 1", 1, portafolio);
            assertEquals(descripcionPortafolio.hashCode(), otraDescripcionPortafolio.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hascode distinto a otro cuando el id es distinto")
        void shouldGenerateHashCodeDifferentWhenIdIsDifferent() {
            DescripcionPortafolio otraDescripcionPortafolio = new DescripcionPortafolio(2L, "Parrafo 1", 1, portafolio);
            assertNotEquals(descripcionPortafolio.hashCode(), otraDescripcionPortafolio.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String expected = "DescripcionPortafolio{id=1, parrafo='Parrafo 1', posicion=1}";
            assertEquals(expected, descripcionPortafolio.toString());
        }

        @Test
        @DisplayName("Debe generar un string distinto al del objeto")
        void shouldGenerateDifferentString() {
            String expected = "DescripcionPortafolio{id=2, parrafo='Parrafo 2', posicion=1}";
            assertNotEquals(expected, descripcionPortafolio.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance(){
            assertTrue(descripcionPortafolio.equals(descripcionPortafolio));    
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(descripcionPortafolio.equals("otro objeto"));
            assertFalse(descripcionPortafolio.equals(null));
        }            
    }
}
