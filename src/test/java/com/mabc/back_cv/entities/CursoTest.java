package com.mabc.back_cv.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.entities.User;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Curso Entity - Test Unitarios")
public class CursoTest{

    private Curso curso;
    private User user;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp(){
        startDate = new Date();
        endDate = new Date(startDate.getTime() + 86400000); // Un día después
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, null, new ArrayList<>());
        curso = new Curso(1L, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null, startDate, endDate, true, user);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Curso con todos los campos")
        void shouldBuildCursoWithAllFields() {
            assertNotNull(curso);
            assertEquals(1L, curso.getId());
            assertEquals("Java Avanzado", curso.getName());
            assertEquals("Curso de Java", curso.getTitle());
            assertEquals("Instituto XYZ", curso.getInstitute());
            assertEquals(null, curso.getCertificate());
            assertEquals(null, curso.getContenidos());
            assertEquals(startDate, curso.getStartDate());
            assertEquals(endDate, curso.getEndDate());
            assertTrue(curso.getActivo());
            assertEquals(user, curso.getUsuario());

        }

        @Test
        @DisplayName("Debe crear un Curso vacío con @NoArgsConstructor")
        void shouldCreateCursoWithNoArgsConstructor() {
            Curso emptyCurso = new Curso();

            assertNotNull(emptyCurso);
            assertNull(emptyCurso.getId());
            assertNull(emptyCurso.getName());
            assertNull(emptyCurso.getTitle());
            assertNull(emptyCurso.getInstitute());
            assertNull(emptyCurso.getCertificate());
            assertNull(emptyCurso.getContenidos());
            assertNull(emptyCurso.getStartDate());
            assertNull(emptyCurso.getEndDate());
            assertNull(emptyCurso.getActivo());
            assertNull(emptyCurso.getUsuario());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            curso.setId(2L);
            assertEquals(2L, curso.getId());

            curso.setName("Python Avanzado");
            assertEquals("Python Avanzado", curso.getName());

            curso.setTitle("Nuevo Título");
            assertEquals("Nuevo Título", curso.getTitle());

            curso.setInstitute("Instituto ABC");
            assertEquals("Instituto ABC", curso.getInstitute());

            Date newStartDate = new Date();
            curso.setStartDate(newStartDate);
            assertEquals(newStartDate, curso.getStartDate());

            Date newEndDate = new Date();
            curso.setEndDate(newEndDate);
            assertEquals(newEndDate, curso.getEndDate());

            curso.setActivo(false);
            assertFalse(curso.getActivo());

            User newUser = new User(2L, "Carlos", "López", "carlos@example.com", "1234567890", "Calle 456", "Madrid", "español", "1234567890", true, null, new ArrayList<>());
            curso.setUsuario(newUser);
            assertEquals(newUser, curso.getUsuario());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1L, curso.getId());
            assertEquals("Java Avanzado", curso.getName());
            assertEquals("Curso de Java", curso.getTitle());
            assertEquals("Instituto XYZ", curso.getInstitute());
            assertNull(curso.getCertificate());
            assertNull(curso.getContenidos());
            assertEquals(startDate, curso.getStartDate());
            assertEquals(endDate, curso.getEndDate());
            assertTrue(curso.getActivo());
            assertEquals(user, curso.getUsuario());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Curso curso2 = new Curso(1L, "Otro Nombre", "Otro Título", "Otro Instituto", null, null, startDate, endDate, false, user);
            assertEquals(curso, curso2);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertNotEquals(curso, null);
            assertNotEquals(curso, "No es un Curso");
            assertNotEquals(curso, 1L);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Curso curso2 = new Curso(2L, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null, startDate, endDate, true, user);
            assertNotEquals(curso, curso2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Curso cursoWithNullId = new Curso(null, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null, startDate, endDate, true, user);
            assertNotEquals(curso, cursoWithNullId);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Curso cursoWithNullId = new Curso(null, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null, startDate, endDate, true, user);
            Curso curso2 = new Curso(2L, "Otro Nombre", "Otro Título", "Otro Instituto", null, null, startDate, endDate, false, user);
            assertNotEquals(cursoWithNullId, curso2);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertNotEquals(0, curso.hashCode());
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Curso cursoWithNullId = new Curso(null, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null, startDate, endDate, true, user);
            assertEquals(0, cursoWithNullId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Curso curso2 = new Curso(1L, "Otro Nombre", "Otro Título", "Otro Instituto", null, null, startDate, endDate, false, user);
            assertEquals(curso.hashCode(), curso2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Curso curso2 = new Curso(2L, "Otro Nombre", "Otro Título", "Otro Instituto", null, null, startDate, endDate, false, user);
            assertNotEquals(curso.hashCode(), curso2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String expectedString = "Curso{" +
                    "id=" + 1L +
                    ", name='" + "Java Avanzado" + '\'' +
                    ", title='" + "Curso de Java" + '\'' +
                    ", institute='" + "Instituto XYZ" + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", activo=" + true +
                    '}';
            assertEquals(expectedString, curso.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertEquals(curso, curso);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertNotEquals(curso, new Object());
        }
        
    }
}