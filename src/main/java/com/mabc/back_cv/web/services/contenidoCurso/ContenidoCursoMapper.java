package com.mabc.back_cv.web.services.contenidoCurso;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.dto.ContenidoCursoDTO;

/**
 * Mapper para la conversión entre entidades ContenidoCurso y DTOs ContenidoCursoDTO.
 */
@Component
public class ContenidoCursoMapper{

    /**
     * Convierte una entidad ContenidoCurso a un ContenidoCursoDTO.
     *
     * @param contenidoCurso Entidad a convertir.
     * @return CursoDTO convertido o null si la entidad es null.
     */
    public static ContenidoCursoDTO entityToDTO(ContenidoCurso contenidoCurso) {
        if (contenidoCurso == null) {
            return null;
        }
        ContenidoCursoDTO dto = new ContenidoCursoDTO();
        dto.setId(contenidoCurso.getId());
        dto.setTitle(contenidoCurso.getTitle());
        dto.setDescription(contenidoCurso.getDescription());
        dto.setActivo(contenidoCurso.getActivo());
        return dto;
    }

    /**
     * Convierte un ContenidoCursoDTO a una entidad ContenidoCurso.
     *
     * @param contenidoCursoDTO DTO a convertir.
     * @return Entidad contenidoCurso convertida o null si el DTO es null.
     */
    public static ContenidoCurso dtoToEntity(ContenidoCursoDTO contenidoCursoDTO) {
        if (contenidoCursoDTO == null) {
            return null;
        }
        ContenidoCurso contenidoCurso = new ContenidoCurso();
        if (contenidoCursoDTO.getId() != null) {
            contenidoCurso.setId(contenidoCursoDTO.getId());
        }
        contenidoCurso.setTitle(contenidoCursoDTO.getTitle());
        contenidoCurso.setDescription(contenidoCursoDTO.getDescription());
        contenidoCurso.setActivo(contenidoCursoDTO.getActivo());
        return contenidoCurso;
    }

}