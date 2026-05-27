package com.mabc.back_cv.entities;

import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.Tecnologia;
import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Trabajo Entity - Test Unitarios")
public class TrabajoTest{

    private Trabajo trabajo;
    private String startDate;
    private String endDate;
    private User user;
    private Rol rol;
    private List<Tecnologia> tecnologias;

    @BeforeEach
    void setUp(){
        startDate = "2024-01-01";
        endDate = "2024-12-31";
        rol = new Rol(1L, "ROLE_USER", true, new ArrayList<>());
        user = new User(1L, "Juan", "Pérez", "juan.perez@example.com", "1234567890", "Calle 123",
                "Santiago", "español", "1234567890", true, rol, new ArrayList<>());
        tecnologias = new ArrayList<>();
        tecnologias.add(new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, null, null));
        trabajo = new Trabajo(1L, "ABC Ltda.", "Desarrollador", "Desarrollador Fullstack", startDate, endDate, false,
                tecnologias, user);
    }

    @Nested
    @DisplayName("Builder y Constructores")
    class BuilderAndConstructors {

        @Test
        @DisplayName("Debe construir un Trabajo con todos los campos")
        void shouldBuildTrabajoWithAllFields() {
            assertNotNull(trabajo);
            assertEquals(1L, trabajo.getId());
            assertEquals("ABC Ltda.", trabajo.getCompany());
            assertEquals("Desarrollador", trabajo.getPosition());
            assertEquals("Desarrollador Fullstack", trabajo.getDescription());
            assertEquals(startDate, trabajo.getStartDate());
            assertEquals(endDate, trabajo.getEndDate());
            assertFalse(trabajo.getCurrent());
            assertEquals(1, trabajo.getTecnologias().size());
            assertEquals("Java", trabajo.getTecnologias().get(0).getName());
            assertEquals(user, trabajo.getUser());
        }

        @Test
        @DisplayName("Debe crear un Trabajo vacío con @NoArgsConstructor")
        void shouldCreateTrabajoWithNoArgsConstructor() {
            Trabajo emptyTrabajo = new Trabajo();
            assertNotNull(emptyTrabajo);
            assertNull(emptyTrabajo.getId());
            assertNull(emptyTrabajo.getCompany());
            assertNull(emptyTrabajo.getPosition());
            assertNull(emptyTrabajo.getDescription());
            assertNull(emptyTrabajo.getStartDate());
            assertNull(emptyTrabajo.getEndDate());
            assertNull(emptyTrabajo.getCurrent());
            assertNotNull(emptyTrabajo.getTecnologias());
            assertTrue(emptyTrabajo.getTecnologias().isEmpty());
            assertNull(emptyTrabajo.getUser());
        }

        @Test
        @DisplayName("Getters y Setters")
        void shouldGetAndSetFields() {
            trabajo.setId(2L);
            trabajo.setCompany("XYZ SpA");
            trabajo.setPosition("Tech Lead");
            trabajo.setDescription("Lider técnico backend");
            trabajo.setStartDate("2025-01-01");
            trabajo.setEndDate("2025-12-31");
            trabajo.setCurrent(true);

            List<Tecnologia> nuevasTecnologias = new ArrayList<>();
            nuevasTecnologias.add(new Tecnologia(2L, "Spring", TipoTecnologiaEnum.FRAMEWORK, null, null));
            trabajo.setTecnologias(nuevasTecnologias);
            trabajo.setUser(user);

            assertEquals(2L, trabajo.getId());
            assertEquals("XYZ SpA", trabajo.getCompany());
            assertEquals("Tech Lead", trabajo.getPosition());
            assertEquals("Lider técnico backend", trabajo.getDescription());
            assertEquals("2025-01-01", trabajo.getStartDate());
            assertEquals("2025-12-31", trabajo.getEndDate());
            assertTrue(trabajo.getCurrent());
            assertEquals(1, trabajo.getTecnologias().size());
            assertEquals("Spring", trabajo.getTecnologias().get(0).getName());
            assertEquals(user, trabajo.getUser());
        }
    }

    @Nested
    @DisplayName("ToString, Equals y HashCode")
    class ToStringEqualsHashcode {

        @Test
        @DisplayName("Debe ser igual cuando ambos tienen el mismo id")
        void shouldBeEqualWhenSameId() {
            Trabajo trabajo2 = new Trabajo(1L, "Otra empresa", "Otra posición", "Otra descripción",
                    "2020-01-01", "2020-12-31", true, new ArrayList<>(), user);
            assertEquals(trabajo, trabajo2);
            assertEquals(trabajo2, trabajo);
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con null o clase distinta")
        void shouldNotBeEqualWithNullOrDifferentClass() {
            assertFalse(trabajo.equals(null));
            assertFalse(trabajo.equals("No es un Trabajo"));
        }

        @Test
        @DisplayName("No debe ser igual solo por tener el mismo nombre si el id es distinto")
        void shouldNotBeEqualWhenOnlyNameMatches() {
            Trabajo trabajo2 = new Trabajo(2L, "ABC Ltda.", "Desarrollador", "Desarrollador Fullstack",
                    startDate, endDate, false, tecnologias, user);
            assertNotEquals(trabajo, trabajo2);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del otro objeto es null")
        void shouldBeDifferentWhenOtherIdIsNull() {
            Trabajo trabajoWithNullId = new Trabajo(null, "ABC Ltda.", "Desarrollador", "Desarrollador Fullstack",
                    startDate, endDate, false, tecnologias, user);
            assertNotEquals(trabajo, trabajoWithNullId);
        }

        @Test
        @DisplayName("No debe ser igual cuando el id del objeto actual es null")
        void shouldReturnFalseWhenCurrentIdIsNull() {
            Trabajo trabajoWithNullId = new Trabajo(null, "ABC Ltda.", "Desarrollador", "Desarrollador Fullstack",
                    startDate, endDate, false, tecnologias, user);
            Trabajo trabajo2 = new Trabajo(2L, "Otra empresa", "Otra posición", "Otra descripción",
                    "2020-01-01", "2020-12-31", true, new ArrayList<>(), user);
            assertNotEquals(trabajoWithNullId, trabajo2);
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto a 0")
        void shouldGenerateHashCodeDifferentToZero() {
            assertNotEquals(0, trabajo.hashCode());
        }

        @Test
        @DisplayName("Debe generar hashCode 0 cuando el id es null")
        void shouldGenerateHashCodeZeroWhenIdIsNull() {
            Trabajo trabajoWithNullId = new Trabajo(null, "ABC Ltda.", "Desarrollador", "Desarrollador Fullstack",
                    startDate, endDate, false, tecnologias, user);
            assertEquals(0, trabajoWithNullId.hashCode());
        }

        @Test
        @DisplayName("Debe generar el mismo hashCode para el mismo id")
        void shouldGenerateSameHashCodeForSameId() {
            Trabajo trabajo2 = new Trabajo(1L, "Otra empresa", "Otra posición", "Otra descripción",
                    "2020-01-01", "2020-12-31", true, new ArrayList<>(), user);
            assertEquals(trabajo.hashCode(), trabajo2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un hashCode distinto cuando el id es distinto")
        void shouldGenerateDifferentHashCodeWhenIdIsDifferent() {
            Trabajo trabajo2 = new Trabajo(2L, "Otra empresa", "Otra posición", "Otra descripción",
                    "2020-01-01", "2020-12-31", true, new ArrayList<>(), user);
            assertNotEquals(trabajo.hashCode(), trabajo2.hashCode());
        }

        @Test
        @DisplayName("Debe generar un string del objeto")
        void shouldGenerateString() {
            String expectedString = "Trabajo{" +
                    "id=" + 1L +
                    ", company='" + "ABC Ltda." + '\'' +
                    ", position='" + "Desarrollador" + '\'' +
                    ", description='" + "Desarrollador Fullstack" + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", current=" + false +
                    '}';
            assertEquals(expectedString, trabajo.toString());
        }

        @Test
        @DisplayName("Debe ser igual a sí mismo por referencia")
        void shouldBeEqualToSameInstance() {
            assertTrue(trabajo.equals(trabajo));
        }

        @Test
        @DisplayName("No debe ser igual cuando se compara con un objeto de distinta clase")
        void shouldBeDifferentWhenClassIsDifferent() {
            assertFalse(trabajo.equals(new Object()));
        }
    }
    
}