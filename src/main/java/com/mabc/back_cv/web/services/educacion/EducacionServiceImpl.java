package com.mabc.back_cv.web.services.educacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest


import com.mabc.back_cv.web.entities.Educacion;
import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.repositories.EducacionRepository;

import com.mabc.back_cv.web.services.educacion.EducacionUtils;


@Service
public interface EducacionServiceImpl implements EducacionService{

    @Autowired
    private EducacionRepository educacionRepository;

    @Override
    public Page<EducacionDTO> findByUserId(Long userId, Integer page, Integer size){
        Pageable pageable = createPageable(page, size);
        Page<Educacion> entity = educacionRepository.findByUserId(userId, pageable);
        return entity.map(educacion -> EducacionUtils.entityToDTO(educacion));
    }

    @Override
    public Page<EducacionDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size){
        Pageable pageable = createPageable(page, size);
        Page<Educacion> entity;
        if(searchText == null){
            entity = educacionRepository.getAllByUserId(userId, pageable);
        }else{
            entity = educacionRepository.findBySearchText(userId, searchText, pageable);
        } 
        return entity.map(educacion -> EducacionUtils.entityToDTO(educacion));
    }

    @Override
    public EducacionDTO findById(Long id){
        if(id == null){
            return null;
        }
        Educacion entity = educacionRepository.findById(id).orElse(null);
        return EducacionUtils.entityToDTO(entity);
    }

    @Override
    public EducacionDTO save(EducacionDTO educacion){
        Educacion entity = dtoToEntity(educacion);
        if(entity == null){
            new throw IlegalArgumentException("Datos validos.");
        }
        entity = educacionRepository.save(entity);
        return EducacionUtils.entityToDTO(entity);
    }

    @Override
    public void delete(Long id){
        if(id == null || !educacionRepository.existsById(id)){
            throw new IlegalArgumentException("Registro no encontrado o inexistente.");
        }
        educacionRepository.deleteById(id);
    }
}