package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.Tecnologia;
import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

@DisplayName("Tecnologia Entity - Tests Unitarios")
public class TecnologiaTest {

    private Tecnologia tecnologia;

    @BeforeEach
    void setUp() {
        tecnologia = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Tecnologia con todos los campos")
        void shouldBuildTecnologiaWithAllFields() {
            
            assertNotNull(tecnologia);
            assertEquals(1L, tecnologia.getId());
            assertEquals("Java", tecnologia.getName());
            assertEquals(TipoTecnologiaEnum.LENGUAJE, tecnologia.getType());
            assertEquals(null, tecnologia.getPathImage());
            assertEquals(null, tecnologia.getLogoSvg());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            tecnologia.setName("Angular");
            tecnologia.setType(TipoTecnologiaEnum.FRAMEWORK);
            tecnologia.setPathImage("ruta/imagen.jpg");
            tecnologia.setLogoSvg("<svg></svg>");

            assertEquals(tecnologia.getName(), "Angular");
            assertEquals(tecnologia.getType(), TipoTecnologiaEnum.FRAMEWORK);
            assertEquals("ruta/imagen.jpg", tecnologia.getPathImage());
            assertEquals("<svg></svg>", tecnologia.getLogoSvg());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1L, tecnologia.getId());
            assertEquals(tecnologia.getName(), "Java");
            assertEquals(tecnologia.getType(), TipoTecnologiaEnum.LENGUAJE);
            assertEquals(tecnologia.getPathImage(), null);
            assertEquals(tecnologia.getLogoSvg(), null);
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHasdhcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Tecnologia otraTecnologia = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertEquals(tecnologia, otraTecnologia);
            assertEquals(otraTecnologia, tecnologia);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es distinto")
        void shouldNotBeEqualWhenIdIsDifferent() {
            Tecnologia otraTecnologia = new Tecnologia(2L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertNotEquals(tecnologia, otraTecnologia);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertFalse(tecnologia.equals(null));
            assertFalse(tecnologia.equals("objeto"));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyPosicionMatches() {
            Tecnologia otraTecnologia = new Tecnologia(2L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertFalse(tecnologia.equals(otraTecnologia));
            assertFalse(otraTecnologia.equals(tecnologia));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener la misma ruta de imágen si el id es distinto")  
        void shouldNotBeEqualsWhenSameUser(){
            Tecnologia otraTecnologia = new Tecnologia(2L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertFalse(tecnologia.equals(otraTecnologia));
            assertFalse(otraTecnologia.equals(tecnologia));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es nulo")
        void shouldBeDifferentWhenIdIsNull(){
            Tecnologia otraTecnologia = new Tecnologia(null, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertFalse(tecnologia.equals(otraTecnologia));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Tecnologia tecnologiaSinId = new Tecnologia(null, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            Tecnologia tecnologiaConId = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);

            assertFalse(tecnologiaSinId.equals(tecnologiaConId));
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertEquals(tecnologia.hashCode() != 0, true);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Tecnologia sinId = new Tecnologia();
            assertEquals(0, sinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Tecnologia otraTecnologia = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null);
            assertEquals(tecnologia.hashCode(), otraTecnologia.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString(){
            String expected = "Tecnologia{id=1, name='Java', type=LENGUAJE, pathImage='null', logoSvg='null'}";
            assertEquals(expected, tecnologia.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(tecnologia.equals(tecnologia));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(tecnologia.equals("otro objeto"));
            assertFalse(tecnologia.equals(null));
        }

    }

}
