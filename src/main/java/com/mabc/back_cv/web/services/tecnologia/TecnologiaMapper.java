package com.mabc.back_cv.web.services.tecnologia;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.TecnologiaDTO;
import com.mabc.back_cv.web.entities.Tecnologia;


@Component
public class TecnologiaMapper{

    public static TecnologiaDTO entityToDTO(Tecnologia entity){
        if(entity == null){
            return null;
        }
        TecnologiaDTO dto = new TecnologiaDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setPathImage(entity.getPathImage());
        dto.setLogoSvg(entity.getLogoSvg());
        return dto;
    }

    public static Tecnologia dtoToEntity(TecnologiaDTO dto){
        if(dto == null){
            return null;
        }
        Tecnologia entity = new Tecnologia();
        if(dto.getId() != null){
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setPathImage(dto.getPathImage());
        entity.setLogoSvg(dto.getLogoSvg());
        return entity;
    }
}