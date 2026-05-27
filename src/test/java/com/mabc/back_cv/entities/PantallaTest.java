package com.mabc.back_cv.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.mabc.back_cv.web.entities.Pantalla;

@DisplayName("Pantalla Entity - Tests Unitarios")
public class PantallaTest {
    
    private Pantalla pantalla;

    @BeforeEach
    void setUp() {
        pantalla = new Pantalla(1L, "Usuarios", null, true, true, false, true, true, true);
    }   

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Pantalla con todos los campos")
        void shouldBuildPantallaWithAllFields() {
            Pantalla nuevaPantalla = new Pantalla(2L, "Reportes", null, false, true, true, false, true, true);
            
            assertNotNull(nuevaPantalla);
            assertEquals(2L, nuevaPantalla.getId());
            assertEquals("Reportes", nuevaPantalla.getNombre_pantalla());
            assertFalse(nuevaPantalla.getAccion_crear());
            assertTrue(nuevaPantalla.getAccion_editar());
            assertTrue(nuevaPantalla.getAccion_eliminar());
            assertFalse(nuevaPantalla.getAccion_consultar());
            assertTrue(nuevaPantalla.getListar());
            assertTrue(nuevaPantalla.getActivo());
        }

        @Test
        @DisplayName("Debe crear un Pantalla vacío con @NoArgsConstructor")
        void shouldCreatePantallaWithNoArgsConstructor() {
            Pantalla pantallaVacia = new Pantalla();
            
            assertNotNull(pantallaVacia);
            assertNull(pantallaVacia.getId());
            assertNull(pantallaVacia.getNombre_pantalla());
            assertNull(pantallaVacia.getMenu());
            assertNull(pantallaVacia.getAccion_crear());
            assertNull(pantallaVacia.getAccion_editar());
            assertNull(pantallaVacia.getAccion_eliminar());
            assertNull(pantallaVacia.getAccion_consultar());
            assertNull(pantallaVacia.getListar());
            assertNull(pantallaVacia.getActivo());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            Pantalla testPantalla = new Pantalla();
            
            testPantalla.setId(5L);
            testPantalla.setNombre_pantalla("Productos");
            testPantalla.setAccion_crear(true);
            testPantalla.setAccion_editar(false);
            testPantalla.setAccion_eliminar(true);
            testPantalla.setAccion_consultar(true);
            testPantalla.setListar(true);
            testPantalla.setActivo(false);
            
            assertEquals(5L, testPantalla.getId());
            assertEquals("Productos", testPantalla.getNombre_pantalla());
            assertTrue(testPantalla.getAccion_crear());
            assertFalse(testPantalla.getAccion_editar());
            assertTrue(testPantalla.getAccion_eliminar());
            assertTrue(testPantalla.getAccion_consultar());
            assertTrue(testPantalla.getListar());
            assertFalse(testPantalla.getActivo());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Pantalla pantalla2 = new Pantalla(1L, "Otra", null, false, false, false, false, false, false);
            
            assertEquals(pantalla, pantalla2);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertNotEquals(pantalla, null);
            assertNotEquals(pantalla, "No soy una Pantalla");
            assertNotEquals(pantalla, 123);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Pantalla pantalla2 = new Pantalla(2L, "Usuarios", null, true, true, false, true, true, true);
            
            assertNotEquals(pantalla, pantalla2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Pantalla pantallaConIdNull = new Pantalla(null, "Usuarios", null, true, true, false, true, true, true);
            
            assertNotEquals(pantalla, pantallaConIdNull);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Pantalla pantallaSinId = new Pantalla(null, "Usuarios", null, true, true, false, true, true, true);
            Pantalla pantallaConId = new Pantalla(1L, "Usuarios", null, true, true, false, true, true, true);
            
            assertNotEquals(pantallaSinId, pantallaConId);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            int hashCode = pantalla.hashCode();
            
            assertNotEquals(0, hashCode);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Pantalla pantallaSinId = new Pantalla(null, "Usuarios", null, true, true, false, true, true, true);
            
            assertEquals(0, pantallaSinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Pantalla pantalla2 = new Pantalla(1L, "Otra", null, false, false, false, false, false, false);
            
            assertEquals(pantalla.hashCode(), pantalla2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Pantalla pantalla2 = new Pantalla(2L, "Otra", null, false, false, false, false, false, false);
            
            assertNotEquals(pantalla.hashCode(), pantalla2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String stringPantalla = pantalla.toString();
            
            assertNotNull(stringPantalla);
            assertTrue(stringPantalla.contains("Pantalla{"));
            assertTrue(stringPantalla.contains("id=1"));
            assertTrue(stringPantalla.contains("nombre_pantalla='Usuarios'"));
            assertTrue(stringPantalla.contains("accion_crear=true"));
            assertTrue(stringPantalla.contains("activo=true"));
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(pantalla.equals(pantalla));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(pantalla.equals(new Object()));
        }
    }

}
