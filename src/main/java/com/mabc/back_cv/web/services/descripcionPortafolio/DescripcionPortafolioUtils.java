package com.mabc.back_cv.web.services.descripcionPortafolio;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;

@Component
public class DescripcionPortafolioUtils {

    public static Pageable createPageable(Integer page, Integer size) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size <= 0 ? 10 : size;
        return PageRequest.of(page, size);
    }

    public static DescripcionPortafolio DTOToEntity(DescripcionPortafolioDTO dto) {
        if (dto == null) {
            return null;
        }
        DescripcionPortafolio entity = new DescripcionPortafolio();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setParrafo(dto.getParrafo());
        entity.setPortafolio(dto.getPortafolio());
        entity.setPosicion(dto.getPosicion());
        return entity;
    }

    public static DescripcionPortafolioDTO entityToDTO(DescripcionPortafolio entity) {
        if (entity == null) {
            return null;
        }
        return new DescripcionPortafolioDTO(
                entity.getId(),
                entity.getParrafo(),
                entity.getPosicion(),
                entity.getPortafolio());
    }
}