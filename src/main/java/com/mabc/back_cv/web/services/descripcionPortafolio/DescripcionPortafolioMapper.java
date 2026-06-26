package com.mabc.back_cv.web.services.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;

/**
 * Mapper para la conversión entre entidades DescripcionPortafolio y DTOs DescripcionPortafolioDTO.
 */
public class DescripcionPortafolioMapper {

    /**
     * Convierte un DescripcionPortafolioDTO a una entidad DescripcionPortafolio.
     *
     * @param dto DTO a convertir.
     * @return Entidad DescripcionPortafolio convertida o null si el DTO es null.
     */
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

    /**
     * Convierte una entidad DescripcionPortafolio a un DescripcionPortafolioDTO.
     *
     * @param entity Entidad a convertir.
     * @return DescripcionPortafolioDTO convertido o null si la entidad es null.
     */
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