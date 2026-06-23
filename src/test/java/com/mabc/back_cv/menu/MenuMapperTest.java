package com.mabc.back_cv.menu;

import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.services.Menu.MenuMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el mapeador {@link MenuMapper}.
 * Verifica que los mapeos entre MenuDTO y Menu (entidad) se realicen
 * correctamente en ambas direcciones para todos sus campos.
 */
class MenuMapperTest {

    private MenuMapper menuMapper;

    @BeforeEach
    void setUp() {
        menuMapper = new MenuMapper();
    }

    @Test
    void convertToMenuEntity_ShouldMapAllFields() {
        MenuDTO dto = new MenuDTO(1L, "Inicio", "/home", "home-icon", 1, 2L, true, null);

        Menu entity = menuMapper.convertToMenuEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Inicio", entity.getNombre());
        assertEquals("/home", entity.getUrl());
        assertEquals("home-icon", entity.getIcono());
        assertEquals(1, entity.getOrden());
        assertEquals(2L, entity.getMenu_padre_id());
        assertTrue(entity.getActivo());
    }

    @Test
    void convertToMenuEntityWhitNoId_ShouldMapAllFields() {
        MenuDTO dto = new MenuDTO(null, "Inicio", "/home", "home-icon", 1, 2L, true, null);

        Menu entity = menuMapper.convertToMenuEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Inicio", entity.getNombre());
        assertEquals("/home", entity.getUrl());
        assertEquals("home-icon", entity.getIcono());
        assertEquals(1, entity.getOrden());
        assertEquals(2L, entity.getMenu_padre_id());
        assertTrue(entity.getActivo());
    }

    @Test
    void convertToDTO_ShouldMapAllFields() {
        Menu entity = new Menu(1L, "Inicio", "/home", "home-icon", 1, 2L, true);

        MenuDTO dto = menuMapper.convertToDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Inicio", dto.getNombre());
        assertEquals("/home", dto.getUrl());
        assertEquals("home-icon", dto.getIcono());
        assertEquals(1, dto.getOrden());
        assertEquals(2L, dto.getMenuPadreId());
        assertTrue(dto.getActivo());
        assertNull(dto.getSubMenus());
    }
}
