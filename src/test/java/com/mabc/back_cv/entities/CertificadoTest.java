package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.entities.User;

@DisplayName("Certificado Entity - Tests Unitarios")
public class CertificadoTest {

    private Certificado certificado;
    private User user;
    private Curso curso;

    @BeforeEach
    void setUp(){
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123",
                "Santiago", "español", "1234567890", true, null, new ArrayList<>());
        curso = new Curso(1L, "Java Avanzado", "Curso de Java", "Instituto XYZ", null, null,
                new Date(), new Date(), true, user);
        certificado = new Certificado(1L, "Certificado Java", "imagen.jpg", "https://certificado.com",
                "Mouse Title", "Mouse Description", user, curso);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Certificado con todos los campos")
        void shouldBuildCertificadoWithAllFields() {
            assertNotNull(certificado);
            assertEquals(1L, certificado.getId());
            assertEquals("Certificado Java", certificado.getName());
            assertEquals("imagen.jpg", certificado.getImage());
            assertEquals("https://certificado.com", certificado.getUrl());
            assertEquals("Mouse Title", certificado.getMouse_move_title());
            assertEquals("Mouse Description", certificado.getMouse_move_description());
            assertEquals(user, certificado.getUser());
            assertEquals(curso, certificado.getCurso());
        }

        @Test
        @DisplayName("Debe crear un Certificado vacío con @NoArgsConstructor")
        void shouldCreateCertificadoWithNoArgsConstructor() {
            Certificado emptyCertificado = new Certificado();

            assertNotNull(emptyCertificado);
            assertNull(emptyCertificado.getId());
            assertNull(emptyCertificado.getName());
            assertNull(emptyCertificado.getUser());
            assertNull(emptyCertificado.getCurso());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            certificado.setName("Certificado Angular");
            certificado.setImage("nueva-imagen.jpg");
            certificado.setUrl("https://nuevo-certificado.com");
            certificado.setMouse_move_title("Nuevo Mouse Title");
            certificado.setMouse_move_description("Nueva Mouse Description");
            certificado.setUser(user);
            certificado.setCurso(curso);

            assertEquals("Certificado Angular", certificado.getName());
            assertEquals("nueva-imagen.jpg", certificado.getImage());
            assertEquals("https://nuevo-certificado.com", certificado.getUrl());
            assertEquals("Nuevo Mouse Title", certificado.getMouse_move_title());
            assertEquals("Nueva Mouse Description", certificado.getMouse_move_description());
            assertEquals(user, certificado.getUser());
            assertEquals(curso, certificado.getCurso());
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(1L, certificado.getId());
            assertEquals("Certificado Java", certificado.getName());
            assertEquals("imagen.jpg", certificado.getImage());
            assertEquals("https://certificado.com", certificado.getUrl());
            assertEquals("Mouse Title", certificado.getMouse_move_title());
            assertEquals("Mouse Description", certificado.getMouse_move_description());
            assertEquals(user, certificado.getUser());
            assertEquals(curso, certificado.getCurso());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Certificado otroCertificado = new Certificado(1L, "Otro nombre", "otra-imagen.jpg",
                    "https://otro.com", "Otro title", "Otra description", user, null);
            assertEquals(certificado, otroCertificado);
            assertEquals(otroCertificado, certificado);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es distinto")
        void shouldNotBeEqualWhenIdIsDifferent() {
            Certificado otroCertificado = new Certificado(2L, "Certificado Java", "imagen.jpg",
                    "https://certificado.com", "Mouse Title", "Mouse Description", user, curso);
            assertNotEquals(certificado, otroCertificado);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertFalse(certificado.equals(null));
            assertFalse(certificado.equals("objeto"));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Certificado otroCertificado = new Certificado(2L, "Certificado Java", "otra-imagen.jpg",
                    "https://otro.com", "Otro title", "Otra description", user, null);
            assertFalse(certificado.equals(otroCertificado));
            assertFalse(otroCertificado.equals(certificado));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Certificado otroCertificado = new Certificado(null, "Certificado Java", "imagen.jpg",
                    "https://certificado.com", "Mouse Title", "Mouse Description", user, curso);
            assertFalse(certificado.equals(otroCertificado));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Certificado certificadoSinId = new Certificado(null, "Certificado Java", "imagen.jpg",
                    "https://certificado.com", "Mouse Title", "Mouse Description", user, curso);
            assertFalse(certificadoSinId.equals(certificado));
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertTrue(certificado.hashCode() != 0);
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Certificado sinId = new Certificado();
            assertEquals(0, sinId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Certificado otroCertificado = new Certificado(1L, "Otro nombre", "otra-imagen.jpg",
                    "https://otro.com", "Otro title", "Otra description", user, null);
            assertEquals(certificado.hashCode(), otroCertificado.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Certificado otroCertificado = new Certificado(2L, "Certificado Java", "imagen.jpg",
                    "https://certificado.com", "Mouse Title", "Mouse Description", user, curso);
            assertNotEquals(certificado.hashCode(), otroCertificado.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String expected = "Certificado{id=1, name='Certificado Java', image='imagen.jpg', "
                    + "url='https://certificado.com', mouse_move_title='Mouse Title', "
                    + "mouse_move_description='Mouse Description'}";
            assertEquals(expected, certificado.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(certificado.equals(certificado));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(certificado.equals("otro objeto"));
            assertFalse(certificado.equals(null));
        }
    }
}
