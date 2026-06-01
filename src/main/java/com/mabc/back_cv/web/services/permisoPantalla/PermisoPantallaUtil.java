package com.mabc.back_cv.web.services.permisoPantalla;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.Pantalla;

import org.springframework.stereotype.Component;

@Component
public class PermisoPantallaUtil {

    public static PermisoPantallaDTO mapToDTO(PermisoPantalla permisoPantalla) {
        if (permisoPantalla == null) {
            return null;
        }
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(permisoPantalla.getId());
        dto.setRol(permisoPantalla.getRol());
        dto.setPantalla(permisoPantalla.getPantalla());
        dto.setAccion_consultar(permisoPantalla.getAccion_consultar());
        dto.setAccion_crear(permisoPantalla.getAccion_crear());
        dto.setAccion_editar(permisoPantalla.getAccion_editar());
        dto.setAccion_eliminar(permisoPantalla.getAccion_eliminar());
        dto.setActivo(permisoPantalla.getActivo());
        return dto;
    }

    public static PermisoPantalla mapToEntity(PermisoPantallaDTO dto) {
        if (dto == null) {
            return null;
        }
        PermisoPantalla permisoPantalla = new PermisoPantalla();
        if(dto.getId() != null) {
            permisoPantalla.setId(dto.getId());
        }
        permisoPantalla.setRol(dto.getRol());
        permisoPantalla.setPantalla(dto.getPantalla());
        permisoPantalla.setAccion_consultar(dto.getAccion_consultar());
        permisoPantalla.setAccion_crear(dto.getAccion_crear());
        permisoPantalla.setAccion_editar(dto.getAccion_editar());
        permisoPantalla.setAccion_eliminar(dto.getAccion_eliminar());
        permisoPantalla.setActivo(dto.getActivo());
        return permisoPantalla;
    }
    
}