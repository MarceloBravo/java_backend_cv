package com.mabc.back_cv.web.services.usuarios;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.User;

/**
 * Clase utilitaria para conversiones entre Usuario y UsuarioDTO.
 * Proporciona métodos estáticos para mapear entre entidades y DTOs.
 * 
 * @author MaBC
 * @version 1.0
 */
@Component
public class UsuarioUtils {

    /**
     * Convierte un UsuarioDTO a una entidad User.
     * 
     * @param usuarioDTO El DTO del usuario a convertir
     * @return Entidad User con los datos del DTO
     */
    public static User DTOToUser(UsuarioDTO usuarioDTO) {
        if(usuarioDTO == null){
            return null;
        }
        User user = new User();
        if (usuarioDTO.getId() != null) {
            user.setId(usuarioDTO.getId());
        }

        user.setNombre(usuarioDTO.getNombre());
        user.setApellido(usuarioDTO.getApellido());
        user.setEmail(usuarioDTO.getEmail());
        user.setPassword(usuarioDTO.getPassword());
        user.setRol(usuarioDTO.getRol());
        user.setActivo(usuarioDTO.getActivo());
        user.setParrafos(usuarioDTO.getParrafos());
        return user;
    }

    /**
     * Convierte una entidad User a un UsuarioDTO.
     * 
     * @param user La entidad User a convertir
     * @return DTO del usuario con los datos de la entidad
     */
    public static UsuarioDTO userToDTO(User user) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(user.getId());
        usuarioDTO.setNombre(user.getNombre());
        usuarioDTO.setApellido(user.getApellido());
        usuarioDTO.setEmail(user.getEmail());
        usuarioDTO.setRol(user.getRol());
        usuarioDTO.setActivo(user.getActivo());
        usuarioDTO.setParrafos(user.getParrafos());
        return usuarioDTO;
    }
}