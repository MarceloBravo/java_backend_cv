package com.mabc.back_cv.web.services.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;

public class DescripcionPortafolioMapper {

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