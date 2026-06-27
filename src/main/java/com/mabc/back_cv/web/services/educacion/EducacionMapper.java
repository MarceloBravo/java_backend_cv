package com.mabc.back_cv.web.services.educacion;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.Educacion;
import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

/**
 * Mapper para la conversión entre entidades Educacion y DTOs EducacionDTO.
 */
@Component
public class EducacionMapper{

    /**
     * Convierte una entidad Educacion a un EducacionDTO.
     *
     * @param educacion Entidad a convertir.
     * @return EducacionDTO convertido o null si la entidad es null.
     */
    public static EducacionDTO entityToDTO(Educacion educacion){
        if(educacion == null){
            return null;
        }
        EducacionDTO educacionDTO = new EducacionDTO();
        educacionDTO.setId(educacion.getId());
        educacionDTO.setInstitution(educacion.getInstitution());
        educacionDTO.setTitle(educacion.getTitle());
        educacionDTO.setShortTitle(educacion.getShortTitle());
        educacionDTO.setName(educacion.getName());
        educacionDTO.setDescription(educacion.getDescription());
        educacionDTO.setYearFrom(educacion.getYearFrom());
        educacionDTO.setYearTo(educacion.getYearTo());
        educacionDTO.setDuration(educacion.getDuration());
        educacionDTO.setImage(educacion.getImage());
        educacionDTO.setUrl(educacion.getUrl());
        educacionDTO.setStyles(educacion.getStyles());
        educacionDTO.setUsuario(UsuarioMapper.userToDTO(educacion.getUsuario()));
        return educacionDTO;
    }

    /**
     * Convierte un EducacionDTO a una entidad Educacion.
     *
     * @param educacionDTO DTO a convertir.
     * @return Entidad Educacion convertida o null si el DTO es null.
     */
    public static Educacion dtoToEntity(EducacionDTO educacionDTO){
        if(educacionDTO == null){
            return null;
        }
        Educacion educacion = new Educacion();
        if(educacionDTO.getId() != null){
            educacion.setId(educacionDTO.getId());
        }
        educacion.setInstitution(educacionDTO.getInstitution());
        educacion.setTitle(educacionDTO.getTitle());
        educacion.setShortTitle(educacionDTO.getShortTitle());
        educacion.setName(educacionDTO.getName());
        educacion.setDescription(educacionDTO.getDescription());
        educacion.setYearFrom(educacionDTO.getYearFrom());
        educacion.setYearTo(educacionDTO.getYearTo());
        educacion.setDuration(educacionDTO.getDuration());
        educacion.setImage(educacionDTO.getImage());
        educacion.setUrl(educacionDTO.getUrl());
        educacion.setStyles(educacionDTO.getStyles());
        educacion.setUsuario(UsuarioMapper.DTOToUser(educacionDTO.getUsuario()));
        return educacion;
    }
}