package com.mabc.back_cv.Rol;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.services.Rol.RolMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase utilitaria {@link RolMapper}.
 * Verifica la creación de paginación y los mapeos entre {@link RolDTO}
 * y la entidad {@link Rol} en ambas direcciones.
 */
class RolMapperTest {

    private RolMapper RolMapper;

    @BeforeEach
    void setUp() {
        RolMapper = new RolMapper();
    }

    // -------------------------------------------------------------------------
    // mapToRol
    // -------------------------------------------------------------------------

    @Test
    void mapToRol_ValidDTO_ShouldMapAllFields() {
        RolDTO dto = new RolDTO(1L, "ADMIN", true);

        Rol rol = RolMapper.mapToRol(dto);

        assertNotNull(rol);
        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertTrue(rol.getActivo());
    }

    @Test
    void mapToRol_NullDTO_ShouldReturnNull() {
        Rol rol = RolMapper.mapToRol(null);

        assertNull(rol);
    }

    @Test
    void mapToRol_NullNombre_ShouldReturnNull() {
        RolDTO dto = new RolDTO(1L, null, true);

        Rol rol = RolMapper.mapToRol(dto);

        assertNull(rol);
    }

    @Test
    void mapToRol_EmptyNombre_ShouldReturnNull() {
        RolDTO dto = new RolDTO(1L, "", true);

        Rol rol = RolMapper.mapToRol(dto);

        assertNull(rol);
    }

    // -------------------------------------------------------------------------
    // mapToRolDTO
    // -------------------------------------------------------------------------

    @Test
    void mapToRolDTO_ValidRol_ShouldMapAllFields() {
        Rol rol = new Rol(1L, "USER", true, null);

        RolDTO dto = RolMapper.mapToRolDTO(rol);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("USER", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    void mapToRolDTO_InactiveRol_ShouldMapActivoAsFalse() {
        Rol rol = new Rol(2L, "VIEWER", false, null);

        RolDTO dto = RolMapper.mapToRolDTO(rol);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("VIEWER", dto.getNombre());
        assertFalse(dto.getActivo());
    }
}
