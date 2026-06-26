package com.mabc.back_cv.web.services.educacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import com.mabc.back_cv.web.entities.Educacion;
import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.repositories.EducacionRepository;

import com.mabc.back_cv.web.services.educacion.EducacionMapper;

import com.mabc.back_cv.common.Utils;


/**
 * Implementación del servicio de educación.
 * Proporciona la lógica de negocio para las operaciones CRUD de registros educativos.
 */
@Service
public class EducacionServiceImpl implements EducacionService{

    @Autowired
    private EducacionRepository educacionRepository;
    

    @Override
    public Page<EducacionDTO> findByUserId(Long userId, Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        Page<Educacion> entity = educacionRepository.findByUserId(userId, pageable);
        return entity.map(educacion -> EducacionMapper.entityToDTO(educacion));
    }

    @Override
    public Page<EducacionDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        Page<Educacion> entity;
        if(searchText == null){
            entity = educacionRepository.getAllByUserId(userId, pageable);
        }else{
            entity = educacionRepository.findBySearchText(userId, searchText, pageable);
        } 
        return entity.map(educacion -> EducacionMapper.entityToDTO(educacion));
    }

    @Override
    public EducacionDTO findById(Long id){
        if(id == null){
            return null;
        }
        Educacion entity = educacionRepository.findById(id).orElse(null);
        return EducacionMapper.entityToDTO(entity);
    }

    @Override
    public EducacionDTO save(EducacionDTO educacion){
        Educacion entity = EducacionMapper.dtoToEntity(educacion);
        if(entity == null){
            throw new IllegalArgumentException("Datos inválidos.");
        }
        entity = educacionRepository.save(entity);
        return EducacionMapper.entityToDTO(entity);
    }

    @Override
    public void delete(Long id){
        if(id == null || !educacionRepository.existsById(id)){
            throw new IllegalArgumentException("Registro no encontrado o inexistente.");
        }
        educacionRepository.deleteById(id);
    }
}