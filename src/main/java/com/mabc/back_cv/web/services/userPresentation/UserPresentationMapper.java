package com.mabc.back_cv.web.services.userPresentation; 

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

/**
 * Mapper para la conversión entre entidades UserPresentation y DTOs UserPresentationDTO.
 */
@Component
public class UserPresentationMapper{

    /**
     * Convierte un UserPresentationDTO a una entidad UserPresentation.
     *
     * @param userPresentationDTO DTO a convertir.
     * @return Entidad UserPresentation convertida o null si el DTO es null.
     */
    public static  UserPresentation dtoToEntity(UserPresentationDTO userPresentationDTO){
        if(userPresentationDTO == null){
            return null;
        }
        UserPresentation userPresentation = new UserPresentation();
        if(userPresentationDTO.getId() != null){
            userPresentation.setId(userPresentationDTO.getId());
        }
        userPresentation.setPosicion(userPresentationDTO.getPosicion());
        userPresentation.setParrafo(userPresentationDTO.getParrafo());
        userPresentation.setUser(UsuarioMapper.DTOToUser(userPresentationDTO.getUser()));
        return userPresentation;
    }

    /**
     * Convierte una entidad UserPresentation a un UserPresentationDTO.
     *
     * @param userPresentation Entidad a convertir.
     * @return UserPresentationDTO convertido o null si la entidad es null.
     */
    public static  UserPresentationDTO entityToDTO(UserPresentation userPresentation){
        if(userPresentation == null){
            return null;
        }
        UserPresentationDTO userPresentationDTO = new UserPresentationDTO();
        userPresentationDTO.setId(userPresentation.getId());
        userPresentationDTO.setPosicion(userPresentation.getPosicion());
        userPresentationDTO.setParrafo(userPresentation.getParrafo());
        userPresentationDTO.setUser(UsuarioMapper.userToDTO(userPresentation.getUser()));
        return userPresentationDTO;
    }
}