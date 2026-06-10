package com.mabc.back_cv.web.services.userPresentation; 

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.services.usuarios.UsuarioUtils;

@Component
public class UserPresentationUtils{

    
    public static  Pageable createPageable(Integer page, Integer rows){
        page = (page == null || page < 0) ? 0 : page;
        rows = (rows == null || rows <= 0) ? 10 : rows;
        return PageRequest.of(page, rows);
    }

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
        userPresentation.setUser(UsuarioUtils.DTOToUser(userPresentationDTO.getUser()));
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
        userPresentationDTO.setUser(UsuarioUtils.userToDTO(userPresentation.getUser()));
        return userPresentationDTO;
    }
}