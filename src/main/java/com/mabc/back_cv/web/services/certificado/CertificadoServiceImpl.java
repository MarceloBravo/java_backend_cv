package com.mabc.back_cv.web.services.certificado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.repositories.CertificadoRepository;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de certificados.
 * Proporciona la lógica de negocio para las operaciones CRUD de certificados.
 */
@Service
public class CertificadoServiceImpl implements CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Override
    public Page<CertificadoDTO> findByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<Certificado> entity = certificadoRepository.findByUserId(userId, pageable);
        return entity.map(CertificadoMapper::entityToDTO);
    }

    @Override
    public Page<CertificadoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<Certificado> entity;
        if (searchText == null) {
            entity = certificadoRepository.getAllByUserId(userId, pageable);
        } else {
            entity = certificadoRepository.findBySearchText(userId, searchText, pageable);
        }
        return entity.map(CertificadoMapper::entityToDTO);
    }

    @Override
    public CertificadoDTO findById(Long id) {
        if (id == null) {
            return null;
        }
        Certificado entity = certificadoRepository.findById(id).orElse(null);
        return CertificadoMapper.entityToDTO(entity);
    }

    @Override
    public CertificadoDTO save(CertificadoDTO certificadoDTO) {
        Certificado entity = CertificadoMapper.dtoToEntity(certificadoDTO);
        if (entity == null) {
            throw new IllegalArgumentException("Datos inválidos.");
        }
        entity = certificadoRepository.save(entity);
        return CertificadoMapper.entityToDTO(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null || !certificadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Registro no encontrado o inexistente.");
        }
        certificadoRepository.deleteById(id);
    }

}
