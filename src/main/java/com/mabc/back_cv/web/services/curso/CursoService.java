package com.mabc.back_cv.web.services.curso;

import com.mabc.back_cv.web.dto.CursoDTO;

import org.springframework.data.domain.Page;

public interface CursoService {

    public Page<CursoDTO> findByUserId(Long userId, Integer page, Integer size);

    public Page<CursoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    public CursoDTO findById(Long id);

    public CursoDTO save(CursoDTO cursoDTO);

    public void delete(Long id);

}
