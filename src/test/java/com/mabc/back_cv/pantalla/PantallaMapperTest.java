package com.mabc.back_cv.pantalla;

import com.mabc.back_cv.web.dto.PantallaDTO;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.services.pantalla.PantallaMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PantallaMapperTest {

    private final Menu menu = new Menu(1L, "Menu Test", null, null, 1, null, true);

    @Test
    void entityToDTO_ShouldMapAllFields() {
        Pantalla entity = new Pantalla(1L, "Pantalla Test", menu, true, false, true, false, true, true);

        PantallaDTO dto = PantallaMapper.entityToDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Pantalla Test", dto.getNombre());
        assertEquals(menu, dto.getMenu());
        assertTrue(dto.getActivo());
        assertTrue(dto.getAccion_crear());
        assertFalse(dto.getAccion_editar());
        assertTrue(dto.getAccion_eliminar());
        assertFalse(dto.getAccion_consultar());
        assertTrue(dto.getListar());
    }

    @Test
    void entityToDTO_WithNull_ShouldReturnNull() {
        assertNull(PantallaMapper.entityToDTO(null));
    }

    @Test
    void dtoToEntity_WithId_ShouldMapAllFields() {
        PantallaDTO dto = new PantallaDTO(1L, "Pantalla Test", menu, true, false, true, false, true, false);

        Pantalla entity = PantallaMapper.dtoToEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Pantalla Test", entity.getNombre_pantalla());
        assertEquals(menu, entity.getMenu());
        assertTrue(entity.getActivo());
        assertFalse(entity.getAccion_crear());
        assertTrue(entity.getAccion_editar());
        assertFalse(entity.getAccion_eliminar());
        assertTrue(entity.getAccion_consultar());
        assertFalse(entity.getListar());
    }

    @Test
    void dtoToEntity_WithoutId_ShouldMapAllFieldsWithNullId() {
        PantallaDTO dto = new PantallaDTO(null, "Pantalla Sin ID", menu, false, true, false, true, false, true);

        Pantalla entity = PantallaMapper.dtoToEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Pantalla Sin ID", entity.getNombre_pantalla());
        assertEquals(menu, entity.getMenu());
        assertFalse(entity.getActivo());
        assertTrue(entity.getAccion_crear());
        assertFalse(entity.getAccion_editar());
        assertTrue(entity.getAccion_eliminar());
        assertFalse(entity.getAccion_consultar());
        assertTrue(entity.getListar());
    }

    @Test
    void dtoToEntity_WithNull_ShouldReturnNull() {
        assertNull(PantallaMapper.dtoToEntity(null));
    }
}
