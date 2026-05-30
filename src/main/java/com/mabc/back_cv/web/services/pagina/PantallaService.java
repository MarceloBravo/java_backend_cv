package com.mabc.back_cv.web.services.pagina;

import java.util.List;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.PantallaDTO;

public interface PantallaService{

    public List<PantallaDTO> getAllPantallas();

    public PantallaDTO getPantallaById(Long id);

    public PantallaDTO savePantalla(PantallaDTO pantallaDTO);

    public void deletePantalla(Long id);

    public Page<PantallaDTO> searchPantallas(String terminoBuscado, Boolean estado, Integer page, Integer size, String sortBy);
    
}
