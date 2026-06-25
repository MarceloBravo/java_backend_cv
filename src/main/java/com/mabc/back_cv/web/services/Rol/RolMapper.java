package com.mabc.back_cv.web.services.Rol;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;

@Component
public class RolMapper {

    public Rol mapToRol(RolDTO rolDTO) {
        if (rolDTO == null || rolDTO.getNombre() == null || rolDTO.getNombre().isEmpty()) {
            return null;
        }

        Rol rol = new Rol();
        rol.setId(rolDTO.getId());
        rol.setNombre(rolDTO.getNombre());
        rol.setActivo(rolDTO.getActivo());
        return rol;
    }

    public RolDTO mapToRolDTO(Rol rol) {
        return new RolDTO(rol.getId(), rol.getNombre(), rol.getActivo());
    }
}
