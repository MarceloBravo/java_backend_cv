package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.ContenidoCurso;

@DisplayName("ContenidoCurso Entity - Tests Unitarios")
public class ContenidoCursoTest {

    private ContenidoCurso contenidoCurso;
    
    @BeforeEach
    void setUp() {
        contenidoCurso = new ContenidoCurso(1L, "Título 1", "Descripción 1", true);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un ContenidoCurso con todos los campos")
        void shouldBuildContenidoCursoWithAllFields() {
            assertNotNull(contenidoCurso);
            assertEquals(1L, contenidoCurso.getId());
            assertEquals("Título 1", contenidoCurso.getTitle());    
            assertEquals("Descripción 1", contenidoCurso.getDescription());
            assertEquals(true, contenidoCurso.getActivo());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            contenidoCurso.setTitle("Título 2");
            contenidoCurso.setDescription("Descripción 2");
            contenidoCurso.setActivo(false);

            assertEquals("Título 2", contenidoCurso.getTitle());
            assertEquals("Descripción 2", contenidoCurso.getDescription());
            assertEquals(false, contenidoCurso.getActivo());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1L, contenidoCurso.getId());
            assertEquals(contenidoCurso.getTitle(), "Título 1");
            assertEquals(contenidoCurso.getDescription(), "Descripción 1");
            assertTrue(contenidoCurso.getActivo());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHasdhcode {
        
        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            ContenidoCurso otroContenido = new ContenidoCurso(1L, "Otro", "Otro", false);
            assertEquals(contenidoCurso, otroContenido);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es distinto")
        void shouldNotBeEqualWhenIdIsDifferent() {
            ContenidoCurso otroContenido = new ContenidoCurso(2L, "Título 1", "Descripción 1", true);
            assertNotEquals(contenidoCurso, otroContenido);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertFalse(contenidoCurso.equals(null));
            assertFalse(contenidoCurso.equals("objeto"));
        }
        
        @Test
        @DisplayName("No debe ser igual solo por tener la misma posicion si el id es distinto")
        void shouldNotBeEqualWhenOnlyPosicionMatches() {
            ContenidoCurso otroContenido = new ContenidoCurso(2L, "Título 1", "Descripción 1", true);
            assertFalse(contenidoCurso.equals(otroContenido));
            assertFalse(otroContenido.equals(contenidoCurso));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo parrafo si el id es distinto")  
        void shouldNotBeEqualsWhenSameUser(){
            ContenidoCurso otroContenido = new ContenidoCurso(2L, "Título 1", "Descripción 1", true);
            assertFalse(contenidoCurso.equals(otroContenido));
            assertFalse(otroContenido.equals(contenidoCurso));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es nulo")
        void shouldBeDifferentWhenIdIsNull(){
            ContenidoCurso otroContenido = new ContenidoCurso(null, "Título 1", "Descripción 1", true);
            assertFalse(contenidoCurso.equals(otroContenido));
            assertFalse(otroContenido.equals(contenidoCurso));
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertTrue(contenidoCurso.hashCode() != 0);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            ContenidoCurso sinId = new ContenidoCurso();
            assertEquals(0, sinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            ContenidoCurso otroContenido = new ContenidoCurso(1L, "Otro", "Otro", false);
            assertEquals(contenidoCurso.hashCode(), otroContenido.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString(){
            String expected = "ContenidoCurso{id=1, title='Título 1', description='Descripción 1', activo=true}";
            assertEquals(expected, contenidoCurso.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(contenidoCurso.equals(contenidoCurso));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(contenidoCurso.equals("otro objeto"));
            assertFalse(contenidoCurso.equals(null));
        }

    }

}
