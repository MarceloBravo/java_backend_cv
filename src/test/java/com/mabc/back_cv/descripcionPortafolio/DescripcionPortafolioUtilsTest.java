package com.mabc.back_cv.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

class DescripcionPortafolioUtilsTest {

    @Test
    void shouldReturnDefaultPageableWhenPageAndSizeAreNullOrInvalid() {
        Pageable pageable1 = DescripcionPortafolioUtils.createPageable(null, null);
        assertEquals(0, pageable1.getPageNumber());
        assertEquals(10, pageable1.getPageSize());

        Pageable pageable2 = DescripcionPortafolioUtils.createPageable(-1, 0);
        assertEquals(0, pageable2.getPageNumber());
        assertEquals(10, pageable2.getPageSize());
    }

    @Test
    void shouldMapDtoToEntityAndBack() {
        Portafolio portafolio = new Portafolio();
        portafolio.setId(1L);
        portafolio.setTitle("Mi portafolio");

        DescripcionPortafolioDTO dto = new DescripcionPortafolioDTO(
                5L,
                "Descripción prueba",
                2,
                portafolio);

        DescripcionPortafolio entity = DescripcionPortafolioUtils.DTOToEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getParrafo(), entity.getParrafo());
        assertEquals(dto.getPosicion(), entity.getPosicion());
        assertEquals(dto.getPortafolio(), entity.getPortafolio());

        DescripcionPortafolioDTO mappedDto = DescripcionPortafolioUtils.entityToDTO(entity);
        assertNotNull(mappedDto);
        assertEquals(entity.getId(), mappedDto.getId());
        assertEquals(entity.getParrafo(), mappedDto.getParrafo());
        assertEquals(entity.getPosicion(), mappedDto.getPosicion());
        assertEquals(entity.getPortafolio(), mappedDto.getPortafolio());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(DescripcionPortafolioUtils.DTOToEntity(null));
    }

    @Test
    void shouldMapDtoWithoutIdToEntityWithNullId() {
        Portafolio portafolio = new Portafolio();
        portafolio.setId(1L);
        portafolio.setTitle("Mi portafolio");

        DescripcionPortafolioDTO dto = new DescripcionPortafolioDTO(
                null,
                "Descripción sin id",
                2,
                portafolio);

        DescripcionPortafolio entity = DescripcionPortafolioUtils.DTOToEntity(dto);
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(dto.getParrafo(), entity.getParrafo());
        assertEquals(dto.getPosicion(), entity.getPosicion());
        assertEquals(dto.getPortafolio(), entity.getPortafolio());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(DescripcionPortafolioUtils.entityToDTO(null));
    }
}
