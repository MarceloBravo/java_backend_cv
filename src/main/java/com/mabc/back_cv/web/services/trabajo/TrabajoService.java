package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.TecnologiaDTO;

import java.util.List;

public interface TrabajoService{
    
    List<TrabajoDTO> getAll(Long userId, String searchText);

    Page<TrabajoDTO> getAll(Long userId, String searchText, Integer page, Integer size);

    TrabajoDTO getById(Long id);
    
    TrabajoDTO save(TrabajoDTO trabajo);

    void deleteById(Long id);

}