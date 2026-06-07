package com.mabc.back_cv.web.services.educacion;

import com.mabc.back_cv.web.dto.EducacionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Educacion;


public interface EducacionService{

    public Page<EducacionDTO> findByUserId(Long userId, Integer page, Integer size);

    public Page<EducacionDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    public EducacionDTO findByUserId(Long id);

    public EducacionDTO save(EducacionDTO educacion);

    public void delete(Long id);

}