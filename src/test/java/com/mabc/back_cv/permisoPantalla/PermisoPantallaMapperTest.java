package com.mabc.back_cv.permisoPantalla;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.Rol;

@DisplayName("Pruebas unitarias para PermisoPantallaMapper")
public class PermisoPantallaMapperTest {

    @Test
    @DisplayName("mapToDTO - éxito con datos completos")
    public void testMapToDTO_Success() {
        Rol rol = new Rol();
        rol.setId(10L);
        rol.setNombre("ADMIN");
        rol.setActivo(true);

        Pantalla pantalla = new Pantalla();
        pantalla.setId(20L);
        pantalla.setNombre_pantalla("Pantalla de prueba");
        pantalla.setActivo(true);

        PermisoPantalla entidad = new PermisoPantalla();
        entidad.setId(1L);
        entidad.setRol(rol);
        entidad.setPantalla(pantalla);
        entidad.setAccion_consultar(true);
        entidad.setAccion_crear(false);
        entidad.setAccion_editar(true);
        entidad.setAccion_eliminar(false);
        entidad.setActivo(true);

        PermisoPantallaDTO dto = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToDTO(entidad);

        assertAll("dto completo",
                () -> assertNotNull(dto),
                () -> assertEquals(entidad.getId(), dto.getId()),
                () -> assertEquals(entidad.getRol(), dto.getRol()),
                () -> assertEquals(entidad.getPantalla(), dto.getPantalla()),
                () -> assertEquals(entidad.getAccion_consultar(), dto.getAccion_consultar()),
                () -> assertEquals(entidad.getAccion_crear(), dto.getAccion_crear()),
                () -> assertEquals(entidad.getAccion_editar(), dto.getAccion_editar()),
                () -> assertEquals(entidad.getAccion_eliminar(), dto.getAccion_eliminar()),
                () -> assertEquals(entidad.getActivo(), dto.getActivo()));
    }

    @Test
    @DisplayName("mapToDTO - entrada nula devuelve nulo")
    public void testMapToDTO_NullInput() {
        PermisoPantallaDTO dto = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToDTO(null);

        assertNull(dto);
    }

    @Test
    @DisplayName("mapToDTO - entidad con campos nulos no lanza excepción")
    public void testMapToDTO_EntityWithNullFields() {
        PermisoPantalla entidad = new PermisoPantalla();
        entidad.setId(2L);
        entidad.setRol(null);
        entidad.setPantalla(null);
        entidad.setAccion_consultar(null);
        entidad.setAccion_crear(null);
        entidad.setAccion_editar(null);
        entidad.setAccion_eliminar(null);
        entidad.setActivo(null);

        PermisoPantallaDTO dto = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToDTO(entidad);

        assertAll("dto con valores nulos",
                () -> assertNotNull(dto),
                () -> assertEquals(entidad.getId(), dto.getId()),
                () -> assertNull(dto.getRol()),
                () -> assertNull(dto.getPantalla()),
                () -> assertNull(dto.getAccion_consultar()),
                () -> assertNull(dto.getAccion_crear()),
                () -> assertNull(dto.getAccion_editar()),
                () -> assertNull(dto.getAccion_eliminar()),
                () -> assertNull(dto.getActivo()));
    }

    @Test
    @DisplayName("mapToEntity - éxito con datos completos")
    public void testMapToEntity_Success() {
        Rol rol = new Rol();
        rol.setId(30L);
        rol.setNombre("USER");
        rol.setActivo(false);

        Pantalla pantalla = new Pantalla();
        pantalla.setId(40L);
        pantalla.setNombre_pantalla("Pantalla test");
        pantalla.setActivo(false);

        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(3L);
        dto.setRol(rol);
        dto.setPantalla(pantalla);
        dto.setAccion_consultar(false);
        dto.setAccion_crear(true);
        dto.setAccion_editar(false);
        dto.setAccion_eliminar(true);
        dto.setActivo(false);

        PermisoPantalla entidad = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToEntity(dto);

        assertAll("entidad completa",
                () -> assertNotNull(entidad),
                () -> assertEquals(dto.getId(), entidad.getId()),
                () -> assertEquals(dto.getRol(), entidad.getRol()),
                () -> assertEquals(dto.getPantalla(), entidad.getPantalla()),
                () -> assertEquals(dto.getAccion_consultar(), entidad.getAccion_consultar()),
                () -> assertEquals(dto.getAccion_crear(), entidad.getAccion_crear()),
                () -> assertEquals(dto.getAccion_editar(), entidad.getAccion_editar()),
                () -> assertEquals(dto.getAccion_eliminar(), entidad.getAccion_eliminar()),
                () -> assertEquals(dto.getActivo(), entidad.getActivo()));
    }

    @Test
    @DisplayName("mapToEntity - dto nulo devuelve nulo")
    public void testMapToEntity_NullInput() {
        PermisoPantalla entidad = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToEntity(null);

        assertNull(entidad);
    }

    @Test
    @DisplayName("mapToEntity - dto sin id no asigna id en la entidad")
    public void testMapToEntity_NullId() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(null);
        dto.setRol(new Rol());
        dto.setPantalla(new Pantalla());
        dto.setAccion_consultar(true);
        dto.setAccion_crear(true);
        dto.setAccion_editar(true);
        dto.setAccion_eliminar(true);
        dto.setActivo(true);

        PermisoPantalla entidad = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToEntity(dto);

        assertAll("entidad sin id",
                () -> assertNotNull(entidad),
                () -> assertNull(entidad.getId()),
                () -> assertEquals(dto.getRol(), entidad.getRol()),
                () -> assertEquals(dto.getPantalla(), entidad.getPantalla()),
                () -> assertEquals(dto.getAccion_consultar(), entidad.getAccion_consultar()),
                () -> assertEquals(dto.getAccion_crear(), entidad.getAccion_crear()),
                () -> assertEquals(dto.getAccion_editar(), entidad.getAccion_editar()),
                () -> assertEquals(dto.getAccion_eliminar(), entidad.getAccion_eliminar()),
                () -> assertEquals(dto.getActivo(), entidad.getActivo()));
    }

    @Test
    @DisplayName("mapToEntity - dto con campos nulos no lanza excepción")
    public void testMapToEntity_DtoWithNullFields() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(4L);
        dto.setRol(null);
        dto.setPantalla(null);
        dto.setAccion_consultar(null);
        dto.setAccion_crear(null);
        dto.setAccion_editar(null);
        dto.setAccion_eliminar(null);
        dto.setActivo(null);

        PermisoPantalla entidad = com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaMapper.mapToEntity(dto);

        assertAll("entidad con valores nulos",
                () -> assertNotNull(entidad),
                () -> assertEquals(dto.getId(), entidad.getId()),
                () -> assertNull(entidad.getRol()),
                () -> assertNull(entidad.getPantalla()),
                () -> assertNull(entidad.getAccion_consultar()),
                () -> assertNull(entidad.getAccion_crear()),
                () -> assertNull(entidad.getAccion_editar()),
                () -> assertNull(entidad.getAccion_eliminar()),
                () -> assertNull(entidad.getActivo()));
    }
}
