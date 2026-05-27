package com.mabc.back_cv.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.mabc.back_cv.web.entities.Educacion;

@DisplayName("Educacion Entity - Tests Unitarios")
public class EducacionTest {
    
    private Educacion educacion;

    @BeforeEach
    void setUp() {
        educacion = new Educacion(1L, "Universidad Nacional", "Ingeniería de Sistemas", 
                                  "Ing. Sistemas", "Carrera de Ingeniería", "Descripción de la carrera", 
                                  2016, 2020, 8, "logo.png", "https://example.com", "color: blue;", null);
    }   

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Educacion con todos los campos")
        void shouldBuildEducacionWithAllFields() {
            Educacion nuevaEducacion = new Educacion(2L, "MIT", "Computer Science", 
                                                     "CS", "Ciencia de la Computación", 
                                                     "Programa de maestría", 2018, 2022, 4, 
                                                     "mit-logo.png", "https://mit.edu", "color: red;", null);
            
            assertNotNull(nuevaEducacion);
            assertEquals(2L, nuevaEducacion.getId());
            assertEquals("MIT", nuevaEducacion.getInstitution());
            assertEquals("Computer Science", nuevaEducacion.getTitle());
            assertEquals("CS", nuevaEducacion.getShortTitle());
            assertEquals("Ciencia de la Computación", nuevaEducacion.getName());
            assertEquals("Programa de maestría", nuevaEducacion.getDescription());
            assertEquals(2018, nuevaEducacion.getYearFrom());
            assertEquals(2022, nuevaEducacion.getYearTo());
            assertEquals(4, nuevaEducacion.getDuration());
            assertEquals("mit-logo.png", nuevaEducacion.getImage());
            assertEquals("https://mit.edu", nuevaEducacion.getUrl());
            assertEquals("color: red;", nuevaEducacion.getStyles());
        }

        @Test
        @DisplayName("Debe crear un Educacion vacío con @NoArgsConstructor")
        void shouldCreateEducacionWithNoArgsConstructor() {
            Educacion educacionVacia = new Educacion();
            
            assertNotNull(educacionVacia);
            assertNull(educacionVacia.getId());
            assertNull(educacionVacia.getInstitution());
            assertNull(educacionVacia.getTitle());
            assertNull(educacionVacia.getShortTitle());
            assertNull(educacionVacia.getName());
            assertNull(educacionVacia.getDescription());
            assertNull(educacionVacia.getYearFrom());
            assertNull(educacionVacia.getYearTo());
            assertNull(educacionVacia.getDuration());
            assertNull(educacionVacia.getImage());
            assertNull(educacionVacia.getUrl());
            assertNull(educacionVacia.getStyles());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            Educacion testEducacion = new Educacion();
            
            testEducacion.setId(5L);
            testEducacion.setInstitution("Stanford University");
            testEducacion.setTitle("Artificial Intelligence");
            testEducacion.setShortTitle("AI");
            testEducacion.setName("Posgrado en IA");
            testEducacion.setDescription("Programa especializado en IA");
            testEducacion.setYearFrom(2021);
            testEducacion.setYearTo(2023);
            testEducacion.setDuration(4);
            testEducacion.setImage("stanford-logo.png");
            testEducacion.setUrl("https://stanford.edu");
            testEducacion.setStyles("color: green;");
            
            assertEquals(5L, testEducacion.getId());
            assertEquals("Stanford University", testEducacion.getInstitution());
            assertEquals("Artificial Intelligence", testEducacion.getTitle());
            assertEquals("AI", testEducacion.getShortTitle());
            assertEquals("Posgrado en IA", testEducacion.getName());
            assertEquals("Programa especializado en IA", testEducacion.getDescription());
            assertEquals(2021, testEducacion.getYearFrom());
            assertEquals(2023, testEducacion.getYearTo());
            assertEquals(4, testEducacion.getDuration());
            assertEquals("stanford-logo.png", testEducacion.getImage());
            assertEquals("https://stanford.edu", testEducacion.getUrl());
            assertEquals("color: green;", testEducacion.getStyles());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Educacion educacion2 = new Educacion(1L, "Otra Universidad", "Otra Carrera", 
                                                 "OC", "Otra", "Descripción distinta", 
                                                 2015, 2019, 8, "otro-logo.png", 
                                                 "https://otra.com", "color: yellow;", null);
            
            assertEquals(educacion, educacion2);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertNotEquals(educacion, null);
            assertNotEquals(educacion, "No soy una Educacion");
            assertNotEquals(educacion, 123);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Educacion educacion2 = new Educacion(2L, "Universidad Nacional", "Ingeniería de Sistemas", 
                                                 "Ing. Sistemas", "Carrera de Ingeniería", 
                                                 "Descripción de la carrera", 2016, 2020, 8, 
                                                 "logo.png", "https://example.com", "color: blue;", null);
            
            assertNotEquals(educacion, educacion2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Educacion educacionConIdNull = new Educacion(null, "Universidad Nacional", 
                                                         "Ingeniería de Sistemas", "Ing. Sistemas", 
                                                         "Carrera de Ingeniería", "Descripción de la carrera", 
                                                         2016, 2020, 8, "logo.png", "https://example.com", 
                                                         "color: blue;", null);
            
            assertNotEquals(educacion, educacionConIdNull);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Educacion educacionSinId = new Educacion(null, "Universidad Nacional", 
                                                     "Ingeniería de Sistemas", "Ing. Sistemas", 
                                                     "Carrera de Ingeniería", "Descripción de la carrera", 
                                                     2016, 2020, 8, "logo.png", "https://example.com", 
                                                     "color: blue;", null);
            Educacion educacionConId = new Educacion(1L, "Universidad Nacional", 
                                                     "Ingeniería de Sistemas", "Ing. Sistemas", 
                                                     "Carrera de Ingeniería", "Descripción de la carrera", 
                                                     2016, 2020, 8, "logo.png", "https://example.com", 
                                                     "color: blue;", null);
            
            assertNotEquals(educacionSinId, educacionConId);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            int hashCode = educacion.hashCode();
            
            assertNotEquals(0, hashCode);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Educacion educacionSinId = new Educacion(null, "Universidad Nacional", 
                                                     "Ingeniería de Sistemas", "Ing. Sistemas", 
                                                     "Carrera de Ingeniería", "Descripción de la carrera", 
                                                     2016, 2020, 8, "logo.png", "https://example.com", 
                                                     "color: blue;", null);
            
            assertEquals(0, educacionSinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Educacion educacion2 = new Educacion(1L, "Otra Universidad", "Otra Carrera", 
                                                 "OC", "Otra", "Descripción distinta", 
                                                 2015, 2019, 8, "otro-logo.png", 
                                                 "https://otra.com", "color: yellow;", null);
            
            assertEquals(educacion.hashCode(), educacion2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Educacion educacion2 = new Educacion(2L, "Otra Universidad", "Otra Carrera", 
                                                 "OC", "Otra", "Descripción distinta", 
                                                 2015, 2019, 8, "otro-logo.png", 
                                                 "https://otra.com", "color: yellow;", null);
            
            assertNotEquals(educacion.hashCode(), educacion2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String stringEducacion = educacion.toString();
            
            assertNotNull(stringEducacion);
            assertTrue(stringEducacion.contains("Educacion{"));
            assertTrue(stringEducacion.contains("id=1"));
            assertTrue(stringEducacion.contains("institution='Universidad Nacional'"));
            assertTrue(stringEducacion.contains("title='Ingeniería de Sistemas'"));
            assertTrue(stringEducacion.contains("shortTitle='Ing. Sistemas'"));
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(educacion.equals(educacion));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(educacion.equals(new Object()));
        }
    }

}
