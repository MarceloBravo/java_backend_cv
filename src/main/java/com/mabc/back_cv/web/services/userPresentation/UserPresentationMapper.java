package com.mabc.back_cv.web.services.userPresentation; 

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

@Component
public class UserPresentationMapper{

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