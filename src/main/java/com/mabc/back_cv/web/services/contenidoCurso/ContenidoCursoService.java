package com.mabc.back_cv.web.services.contenidoCurso;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.ContenidoCurso;

import java.util.List;

public interface ContenidoCursoService{

    public List<ContenidoCursoDTO> findAllList(String searchText, Boolean activo);

    public Page<ContenidoCursoDTO> findAllPage(String searchText, Integer page, Integer size, Boolean activo);

    public ContenidoCursoDTO getById(Long id);

    public ContenidoCursoDTO save(ContenidoCursoDTO contenidoCurso);

    public void delete(Long id);
    
}