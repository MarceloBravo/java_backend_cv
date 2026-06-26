package com.mabc.back_cv.web.services.certificado;

import com.mabc.back_cv.web.dto.CertificadoDTO;

import org.springframework.data.domain.Page;

public interface CertificadoService {

    public Page<CertificadoDTO> findByUserId(Long userId, Integer page, Integer size);

    public Page<CertificadoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    public CertificadoDTO findById(Long id);

    public CertificadoDTO save(CertificadoDTO certificadoDTO);

    public void delete(Long id);

}
