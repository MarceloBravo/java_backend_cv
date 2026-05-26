package com.mabc.back_cv.Rol;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.services.Rol.RolUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase utilitaria {@link RolUtils}.
 * Verifica la creación de paginación y los mapeos entre {@link RolDTO}
 * y la entidad {@link Rol} en ambas direcciones.
 */
class RolUtilsTest {

    private RolUtils rolUtils;

    @BeforeEach
    void setUp() {
        rolUtils = new RolUtils();
    }

    // -------------------------------------------------------------------------
    // createPageable
    // -------------------------------------------------------------------------

    @Test
    void createPageable_ValidParams_ShouldReturnPageable() {
        Pageable pageable = rolUtils.createPageable(2, 5);

        assertNotNull(pageable);
        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
    }

    @Test
    void createPageable_NegativePage_ShouldDefaultToZero() {
        Pageable pageable = rolUtils.createPageable(-1, 10);

        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void createPageable_ZeroRows_ShouldDefaultToTen() {
        Pageable pageable = rolUtils.createPageable(0, 0);

        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void createPageable_NegativeRows_ShouldDefaultToTen() {
        Pageable pageable = rolUtils.createPageable(1, -5);

        assertNotNull(pageable);
        assertEquals(1, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    // -------------------------------------------------------------------------
    // mapToRol
    // -------------------------------------------------------------------------

    @Test
    void mapToRol_ValidDTO_ShouldMapAllFields() {
        RolDTO dto = new RolDTO(1L, "ADMIN", true);

        Rol rol = rolUtils.mapToRol(dto);

        assertNotNull(rol);
        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertTrue(rol.getActivo());
    }

    @Test
    void mapToRol_NullDTO_ShouldReturnNull() {
        Rol rol = rolUtils.mapToRol(null);

        assertNull(rol);
    }

    @Test
    void mapToRol_NullNombre_ShouldReturnNull() {
        RolDTO dto = new RolDTO(1L, null, true);

        Rol rol = rolUtils.mapToRol(dto);

        assertNull(rol);
    }

    @Test
    void mapToRol_EmptyNombre_ShouldReturnNull() {
        RolDTO dto = new RolDTO(1L, "", true);

        Rol rol = rolUtils.mapToRol(dto);

        assertNull(rol);
    }

    // -------------------------------------------------------------------------
    // mapToRolDTO
    // -------------------------------------------------------------------------

    @Test
    void mapToRolDTO_ValidRol_ShouldMapAllFields() {
        Rol rol = new Rol(1L, "USER", true, null);

        RolDTO dto = rolUtils.mapToRolDTO(rol);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("USER", dto.getNombre());
        assertTrue(dto.getActivo());
    }

    @Test
    void mapToRolDTO_InactiveRol_ShouldMapActivoAsFalse() {
        Rol rol = new Rol(2L, "VIEWER", false, null);

        RolDTO dto = rolUtils.mapToRolDTO(rol);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("VIEWER", dto.getNombre());
        assertFalse(dto.getActivo());
    }
}
