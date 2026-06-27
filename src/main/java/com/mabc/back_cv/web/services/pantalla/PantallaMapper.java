package com.mabc.back_cv.web.services.pantalla;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.dto.PantallaDTO;

@Component
public class PantallaMapper{

    public static PantallaDTO entityToDTO(Pantalla pantalla){
        if(pantalla == null){
            return null;
        }
        PantallaDTO dto = new PantallaDTO();
        dto.setId(pantalla.getId());
        dto.setNombre(pantalla.getNombre_pantalla());
        dto.setMenu(pantalla.getMenu());
        dto.setActivo(pantalla.getActivo());
        dto.setAccion_crear(pantalla.getAccion_crear());
        dto.setAccion_editar(pantalla.getAccion_editar());
        dto.setAccion_eliminar(pantalla.getAccion_eliminar());
        dto.setAccion_consultar(pantalla.getAccion_consultar());
        dto.setListar(pantalla.getListar());

        return dto;
    }

    public static Pantalla dtoToEntity(PantallaDTO dto){
        if(dto == null){
            return null;
        }
        Pantalla entity = new Pantalla();
        if(dto.getId() != null){
            entity.setId(dto.getId());
        }
        entity.setNombre_pantalla(dto.getNombre());
        entity.setMenu(dto.getMenu());
        entity.setActivo(dto.getActivo());
        entity.setAccion_crear(dto.getAccion_crear());
        entity.setAccion_editar(dto.getAccion_editar());
        entity.setAccion_eliminar(dto.getAccion_eliminar());
        entity.setAccion_consultar(dto.getAccion_consultar());
        entity.setListar(dto.getListar());

        return entity;
    }

    
}