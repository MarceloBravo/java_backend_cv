package com.mabc.back_cv.web.services.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface DescripcionPortafolioService {

    public List<DescripcionPortafolioDTO> getAll();

    Page<DescripcionPortafolioDTO> getAll(String terminoBuscado, Integer page, Integer size);

    DescripcionPortafolioDTO getById(Long id);

    DescripcionPortafolioDTO save(DescripcionPortafolioDTO detallePortafolioDTO);

    void delete(Long id);

}
