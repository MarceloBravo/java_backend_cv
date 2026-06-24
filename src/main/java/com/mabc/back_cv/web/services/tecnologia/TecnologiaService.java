package com.mabc.back_cv.web.services.tecnologia;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.TecnologiaDTO;

public interface TecnologiaService {

    Page<TecnologiaDTO> findAll(String searchText, Integer page, Integer size);
    
    List<TecnologiaDTO> findAll(String searchText);

    TecnologiaDTO getById(Long id);

    TecnologiaDTO save(TecnologiaDTO tecnologiaDTO);

    void deleteById(Long id);
    
}