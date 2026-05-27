package com.mabc.back_cv.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.mabc.back_cv.web.entities.Menu;

@DisplayName("Menu Entity - Tests Unitarios")
public class MenuTest {
    
    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu(1L, "Inicio", "/", "home", 1, null, true);
    }   

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Menu con todos los campos")
        void shouldBuildMenuWithAllFields() {
            Menu nuevoMenu = new Menu(2L, "Perfil", "/perfil", "user", 2, 1L, true);
            
            assertNotNull(nuevoMenu);
            assertEquals(2L, nuevoMenu.getId());
            assertEquals("Perfil", nuevoMenu.getNombre());
            assertEquals("/perfil", nuevoMenu.getUrl());
            assertEquals("user", nuevoMenu.getIcono());
            assertEquals(2, nuevoMenu.getOrden());
            assertEquals(1L, nuevoMenu.getMenu_padre_id());
            assertTrue(nuevoMenu.getActivo());
        }

        @Test
        @DisplayName("Debe crear un Menu vacío con @NoArgsConstructor")
        void shouldCreateMenuWithNoArgsConstructor() {
            Menu menuVacio = new Menu();
            
            assertNotNull(menuVacio);
            assertNull(menuVacio.getId());
            assertNull(menuVacio.getNombre());
            assertNull(menuVacio.getUrl());
            assertNull(menuVacio.getIcono());
            assertNull(menuVacio.getOrden());
            assertNull(menuVacio.getMenu_padre_id());
            assertNull(menuVacio.getActivo());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            Menu testMenu = new Menu();
            
            testMenu.setId(5L);
            testMenu.setNombre("Reportes");
            testMenu.setUrl("/reportes");
            testMenu.setIcono("chart");
            testMenu.setOrden(5);
            testMenu.setMenu_padre_id(2L);
            testMenu.setActivo(false);
            
            assertEquals(5L, testMenu.getId());
            assertEquals("Reportes", testMenu.getNombre());
            assertEquals("/reportes", testMenu.getUrl());
            assertEquals("chart", testMenu.getIcono());
            assertEquals(5, testMenu.getOrden());
            assertEquals(2L, testMenu.getMenu_padre_id());
            assertFalse(testMenu.getActivo());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Menu menu2 = new Menu(1L, "Otra", "/otra", "otro", 2, null, false);
            
            assertEquals(menu, menu2);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertNotEquals(menu, null);
            assertNotEquals(menu, "No soy un Menu");
            assertNotEquals(menu, 123);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Menu menu2 = new Menu(2L, "Inicio", "/", "home", 1, null, true);
            
            assertNotEquals(menu, menu2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Menu menuConIdNull = new Menu(null, "Inicio", "/", "home", 1, null, true);
            
            assertNotEquals(menu, menuConIdNull);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Menu menuSinId = new Menu(null, "Inicio", "/", "home", 1, null, true);
            Menu menuConId = new Menu(1L, "Inicio", "/", "home", 1, null, true);
            
            assertNotEquals(menuSinId, menuConId);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            int hashCode = menu.hashCode();
            
            assertNotEquals(0, hashCode);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Menu menuSinId = new Menu(null, "Inicio", "/", "home", 1, null, true);
            
            assertEquals(0, menuSinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Menu menu2 = new Menu(1L, "Otra", "/otra", "otro", 2, null, false);
            
            assertEquals(menu.hashCode(), menu2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Menu menu2 = new Menu(2L, "Otra", "/otra", "otro", 2, null, false);
            
            assertNotEquals(menu.hashCode(), menu2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String stringMenu = menu.toString();
            
            assertNotNull(stringMenu);
            assertTrue(stringMenu.contains("Menu{"));
            assertTrue(stringMenu.contains("id=1"));
            assertTrue(stringMenu.contains("nombre='Inicio'"));
            assertTrue(stringMenu.contains("url='/'"));
            assertTrue(stringMenu.contains("icono='home'"));
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(menu.equals(menu));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(menu.equals(new Object()));
        }
    }


}
