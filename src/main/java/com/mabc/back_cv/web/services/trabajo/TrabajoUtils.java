package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;

import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Trabajo;

import org.springframework.data.domain.PageRequest;


@Component
public class TrabajoUtils{

    public static Pageable createPageable(Integer page, Integer size){
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(page, size);
    }

    public static TrabajoDTO entityToDTO(Trabajo entity){
        if(entity == null){
            return null;
        }
        TrabajoDTO dto = new TrabajoDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCompany(entity.getCompany());
        dto.setPosition(entity.getPosition());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setActual(entity.getActual());
        return dto;
    }

    public static Trabajo dtoToEntity(TrabajoDTO dto){
        if(dto == null){
            return null;
        }
        Trabajo entity = new Trabajo();
        if(dto.getId() != null){
            entity.setId(dto.getId());
        }
        entity.setUserId(dto.getUserId());
        entity.setCompany(dto.getCompany());
        entity.setPosition(dto.getPosition());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setActual(dto.getActual());
        return entity;
    }

}