package com.mabc.back_cv.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.mabc.back_cv.web.entities.PermisoPantalla;

@DisplayName("PermisoPantalla Entity - Tests Unitarios")
public class PermisoPantallaTest {
    
    private PermisoPantalla permisoPantalla;

    @BeforeEach
    void setUp() {
        permisoPantalla = new PermisoPantalla(1L, null, null, true, true, false, true, true, true);
    }   

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un PermisoPantalla con todos los campos")
        void shouldBuildPermisoPantallaWithAllFields() {
            PermisoPantalla nuevoPermiso = new PermisoPantalla(2L, null, null, false, true, true, false, true, true);
            
            assertNotNull(nuevoPermiso);
            assertEquals(2L, nuevoPermiso.getId());
            assertFalse(nuevoPermiso.getAccion_crear());
            assertTrue(nuevoPermiso.getAccion_editar());
            assertTrue(nuevoPermiso.getAccion_eliminar());
            assertFalse(nuevoPermiso.getAccion_consultar());
            assertTrue(nuevoPermiso.getListar());
            assertTrue(nuevoPermiso.getActivo());
        }

        @Test
        @DisplayName("Debe crear un PermisoPantalla vacío con @NoArgsConstructor")
        void shouldCreatePermisoPantallaWithNoArgsConstructor() {
            PermisoPantalla permisoVacio = new PermisoPantalla();
            
            assertNotNull(permisoVacio);
            assertNull(permisoVacio.getId());
            assertNull(permisoVacio.getRol());
            assertNull(permisoVacio.getPantalla());
            assertNull(permisoVacio.getAccion_crear());
            assertNull(permisoVacio.getAccion_editar());
            assertNull(permisoVacio.getAccion_eliminar());
            assertNull(permisoVacio.getAccion_consultar());
            assertNull(permisoVacio.getListar());
            assertNull(permisoVacio.getActivo());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            PermisoPantalla testPermiso = new PermisoPantalla();
            
            testPermiso.setId(5L);
            testPermiso.setAccion_crear(true);
            testPermiso.setAccion_editar(false);
            testPermiso.setAccion_eliminar(true);
            testPermiso.setAccion_consultar(true);
            testPermiso.setListar(false);
            testPermiso.setActivo(true);
            
            assertEquals(5L, testPermiso.getId());
            assertTrue(testPermiso.getAccion_crear());
            assertFalse(testPermiso.getAccion_editar());
            assertTrue(testPermiso.getAccion_eliminar());
            assertTrue(testPermiso.getAccion_consultar());
            assertFalse(testPermiso.getListar());
            assertTrue(testPermiso.getActivo());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            PermisoPantalla permiso2 = new PermisoPantalla(1L, null, null, false, false, false, false, false, false);
            
            assertEquals(permisoPantalla, permiso2);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertNotEquals(permisoPantalla, null);
            assertNotEquals(permisoPantalla, "No soy un PermisoPantalla");
            assertNotEquals(permisoPantalla, 123);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            PermisoPantalla permiso2 = new PermisoPantalla(2L, null, null, true, true, false, true, true, true);
            
            assertNotEquals(permisoPantalla, permiso2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            PermisoPantalla permisoConIdNull = new PermisoPantalla(null, null, null, true, true, false, true, true, true);
            
            assertNotEquals(permisoPantalla, permisoConIdNull);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            PermisoPantalla permisoSinId = new PermisoPantalla(null, null, null, true, true, false, true, true, true);
            PermisoPantalla permisoConId = new PermisoPantalla(1L, null, null, true, true, false, true, true, true);
            
            assertNotEquals(permisoSinId, permisoConId);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            int hashCode = permisoPantalla.hashCode();
            
            assertNotEquals(0, hashCode);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            PermisoPantalla permisoSinId = new PermisoPantalla(null, null, null, true, true, false, true, true, true);
            
            assertEquals(0, permisoSinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            PermisoPantalla permiso2 = new PermisoPantalla(1L, null, null, false, false, false, false, false, false);
            
            assertEquals(permisoPantalla.hashCode(), permiso2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            PermisoPantalla permiso2 = new PermisoPantalla(2L, null, null, false, false, false, false, false, false);
            
            assertNotEquals(permisoPantalla.hashCode(), permiso2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String stringPermiso = permisoPantalla.toString();
            
            assertNotNull(stringPermiso);
            assertTrue(stringPermiso.contains("PermisoPantalla{"));
            assertTrue(stringPermiso.contains("id=1"));
            assertTrue(stringPermiso.contains("accion_crear=true"));
            assertTrue(stringPermiso.contains("accion_editar=true"));
            assertTrue(stringPermiso.contains("activo=true"));
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(permisoPantalla.equals(permisoPantalla));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(permisoPantalla.equals(new Object()));
        }
    }

}
