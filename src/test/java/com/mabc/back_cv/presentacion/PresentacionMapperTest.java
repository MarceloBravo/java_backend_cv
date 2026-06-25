package com.mabc.back_cv.presentacion;

import com.mabc.back_cv.web.dto.PresentacionDTO;
import com.mabc.back_cv.web.entities.Presentacion;
import org.junit.jupiter.api.Test;

import static com.mabc.back_cv.web.services.presentacion.PresentacionMapper.dtoToEntity;
import static com.mabc.back_cv.web.services.presentacion.PresentacionMapper.entityToDTO;
import static org.junit.jupiter.api.Assertions.*;

class PresentacionMapperTest {

    @Test
    void entityToDTO_WithValidEntity_ReturnsDTO() {
        Presentacion presentacion = new Presentacion(1L, "Parrafo prueba", null);

        PresentacionDTO dto = entityToDTO(presentacion);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Parrafo prueba", dto.getParrafo());
        assertNull(dto.getUser());
    }

    @Test
    void entityToDTO_NullInput_ReturnsNull() {
        PresentacionDTO dto = entityToDTO(null);
        assertNull(dto);
    }

    @Test
    void dtoToEntity_WithPositiveIdAndSpaces_SanitizesAndSetsId() {
        PresentacionDTO dto = new PresentacionDTO(2L, "  Hola   mundo  ", null);

        Presentacion entity = dtoToEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Hola mundo", entity.getParrafo());
        assertNull(entity.getUser());
    }

    @Test
    void dtoToEntity_WithNullId_DoesNotSetId() {
        PresentacionDTO dto = new PresentacionDTO(null, "  texto  ", null);

        Presentacion entity = dtoToEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("texto", entity.getParrafo());
    }

    @Test
    void dtoToEntity_WithZeroId_DoesNotSetId() {
        PresentacionDTO dto = new PresentacionDTO(0L, "  texto  ", null);

        Presentacion entity = dtoToEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("texto", entity.getParrafo());
    }

    @Test
    void dtoToEntity_NullInput_ReturnsNull() {
        Presentacion entity = dtoToEntity(null);
        assertNull(entity);
    }

    @Test
    void dtoToEntity_WithNullParrafo_ReturnsEntityWithNullParrafo() {
        PresentacionDTO dto = new PresentacionDTO(null, null, null);

        Presentacion entity = dtoToEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getParrafo());
        assertNull(entity.getUser());
    }
}
