package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;

import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Trabajo;

import org.springframework.data.domain.PageRequest;

import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;


@Component
public class TrabajoMapper{
    
    public static TrabajoDTO entityToDTO(Trabajo entity){
        if(entity == null){
            return null;
        }
        TrabajoDTO dto = new TrabajoDTO();
        dto.setId(entity.getId());
        dto.setUser(UsuarioMapper.userToDTO(entity.getUser()));
        dto.setCompany(entity.getCompany());
        dto.setPosition(entity.getPosition());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCurrent(entity.getCurrent());
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
        entity.setUser(UsuarioMapper.DTOToUser(dto.getUser()));
        entity.setCompany(dto.getCompany());
        entity.setPosition(dto.getPosition());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setCurrent(dto.getCurrent());
        return entity;
    }

}