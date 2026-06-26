package com.mabc.back_cv.web.services.userPresentation;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.repositories.UserPresentationRepository;

import org.springframework.beans.factory.annotation.Autowired;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de presentaciones de usuario.
 * Proporciona la lógica de negocio para las operaciones CRUD de presentaciones de usuario.
 */
@Service
public class UserPresentationServiceImpl implements UserPresentationService{

    @Autowired
    private UserPresentationRepository userPresentationRepository;
    

    @Override
    public Page<UserPresentationDTO> getAll(String searchText, Long userId,Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        Page<UserPresentation> userPresentationPage = userPresentationRepository.findAll(searchText, userId, pageable);
        Page<UserPresentationDTO> userPresentationDTOPage =userPresentationPage.map(userPresentation -> UserPresentationMapper.entityToDTO(userPresentation));
        return userPresentationDTOPage == null ? Page.empty() : userPresentationDTOPage;
    }

    @Override
    public UserPresentationDTO findById(Long id){
        if(id == null){
            return null;
        }
        UserPresentation entity = userPresentationRepository.findById(id).orElse(null);
        return UserPresentationMapper.entityToDTO(entity);
    }

    @Override
    public UserPresentationDTO save(UserPresentationDTO userPresentation){
        UserPresentation entity = UserPresentationMapper.dtoToEntity(userPresentation);
        if(entity == null){
            return null;
        }
        UserPresentation savedEntity = userPresentationRepository.save(entity);
        return UserPresentationMapper.entityToDTO(savedEntity);   
    }

    @Override
    public void delete(Long id){
         if (id == null) {
            throw new IllegalArgumentException("Error: El id no puede ser nulo.");
        }
        if (!userPresentationRepository.findById(id).isPresent()) {
            throw new RuntimeException("Error: El registro no existe.");
        }
        userPresentationRepository.deleteById(id);
    }


}