package com.mabc.back_cv.web.services.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import org.springframework.data.domain.Page;

public interface PortafolioService {

    public PortafolioDTO savePortafolio(PortafolioDTO portafolio);

    public PortafolioDTO getPortafolioById(Long id);

    public PortafolioDTO getPortafolioByUserId(Long userId);

    public Page<PortafolioDTO> getPage(Long userId, String searchText, Integer page, Integer size);

    public void deletePortafolio(Long id);
}