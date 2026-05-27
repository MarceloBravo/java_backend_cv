package com.mabc.back_cv.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.entities.User;

@DisplayName("UserPresentation Entity - Tests Unitarios")
public class UserPresentationTest {

    private UserPresentation userPresentation;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123", "Santiago", "español", "1234567890", true, null, null);
        userPresentation = new UserPresentation(1L, 1, "Parrafo 1", user);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un objeto UserPresentation con todos los campos")
        void shouldBuildUserPresentationWithAllFields() {
            assertNotNull(userPresentation);
            assertEquals(1L, userPresentation.getId());
            assertEquals(1, userPresentation.getPosicion());
            assertEquals("Parrafo 1", userPresentation.getParrafo());
            assertEquals(user, userPresentation.getUser());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            userPresentation.setPosicion(2);
            userPresentation.setParrafo("Parrafo 2");   

            assertEquals(userPresentation.getPosicion(), 2);
            assertEquals(userPresentation.getParrafo(), "Parrafo 2");
        }

        @Test
        @DisplayName("Debe obtener todos los campos mediante getters")
        void shouldGetAllFields() {
            assertEquals(userPresentation.getId(), 1L);
            assertEquals(userPresentation.getPosicion(), 1);
            assertEquals(userPresentation.getParrafo(), "Parrafo 1");
            assertEquals(userPresentation.getUser().equals(user), true);
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHasdhcode {

        @Test
        @DisplayName("Debe generar el String")
        void shouldGenerateToString() {
            String expected = "UserPresentation{id=1, posicion=1, parrafo='Parrafo 1'}";
            assertEquals(expected, userPresentation.toString());
        }

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            UserPresentation otraUserPresentation = new UserPresentation(1L, 1, "Parrafo 1", user);
            assertEquals(userPresentation, otraUserPresentation);
            assertEquals(otraUserPresentation, userPresentation);
        }

        @Test
        @DisplayName("No debe ser igual solo por tener la misma posicion si el id es distinto")
        void shouldNotBeEqualWhenOnlyPosicionMatches() {
            UserPresentation otraUserPresentation = new UserPresentation(2L, 2, "Parrafo 1", user);
            assertFalse(userPresentation.equals(otraUserPresentation));
            assertFalse(otraUserPresentation.equals(userPresentation));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo parrafo si el id es distinto")
        void shouldNotBeEqualWhenOnlyParrafoMatches(){
            UserPresentation otraUserPresentation = new UserPresentation(2L, 2, "Parrafo 1", user);
            assertFalse(userPresentation.equals(otraUserPresentation));
            assertFalse(otraUserPresentation.equals(userPresentation));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo user si el id es distinto")
        void shouldNotBeEqualsWhenSameUser(){
            UserPresentation otraUserPresentation = new UserPresentation(2L, 2, "Parrafo 1", user);
            assertFalse(userPresentation.equals(otraUserPresentation));
            assertFalse(otraUserPresentation.equals(userPresentation));
        }

        @Test
        @DisplayName("No debe ser igual cuando el id es nulo")
        void shouldBeDifferentWhenIdIsNull(){
            UserPresentation otraUserPresentation = new UserPresentation(null, 2, "Parrafo 1", user);
            assertFalse(userPresentation.equals(otraUserPresentation));
            assertFalse(otraUserPresentation.equals(userPresentation));
        }

        @Test
        @DisplayName("Debe generar un hascode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero(){
            assertEquals(userPresentation.hashCode() != 0, true);
        }

        @Test
        @DisplayName("Debe generar un hascode igual a 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull(){
            UserPresentation otraUserPresentation = new UserPresentation(null, 2, "Parrafo 1", user);
            assertEquals(otraUserPresentation.hashCode(), 0);
        }

        @Test
        @DisplayName("Debe generar un hascode igual a otro cuando el id es el mismo")
        void shouldGenerateHashCodeEqualWhenIdIsSame(){
            UserPresentation otraUserPresentation = new UserPresentation(1L, 2, "Parrafo 1", user);
            assertEquals(otraUserPresentation.hashCode(), otraUserPresentation.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hascode distinto a otro cuando el id es distinto")
        void shouldGenerateHashCodeDifferentWhenIdIsDifferent(){
            UserPresentation otraUserPresentation = new UserPresentation(2L, 2, "Parrafo 1", user);
            assertNotEquals(userPresentation.hashCode(), otraUserPresentation.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString(){
            String expected = "UserPresentation{id=1, posicion=1, parrafo='Parrafo 1'}";
            assertEquals(expected, userPresentation.toString());
        }

        @Test
        @DisplayName("Debe generar un string distinto al del objeto")
        void shouldGenerateDifferentString(){
            String expected = "UserPresentation{id=2, posicion=2, parrafo='Parrafo 2'}";
            assertNotEquals(expected, userPresentation.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance(){
            assertEquals(userPresentation.equals(userPresentation), true);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent(){
            assertFalse(userPresentation.equals("otro objeto"));
            assertFalse(userPresentation.equals(null));
        }

    }


    
}
