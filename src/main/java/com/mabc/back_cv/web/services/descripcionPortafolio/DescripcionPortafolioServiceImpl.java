package com.mabc.back_cv.web.services.descripcionPortafolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;
import com.mabc.back_cv.web.repositories.DescripcionPortafolioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mabc.back_cv.common.Utils;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioMapper;

@Service
public class DescripcionPortafolioServiceImpl implements DescripcionPortafolioService {

    @Autowired
    private DescripcionPortafolioRepository descripcionPortafolioRepository;

    @Override
    public Page<DescripcionPortafolioDTO> getAll(String terminoBuscado, Integer page, Integer size) {
        if (terminoBuscado == null || terminoBuscado.isEmpty()) {
            return getAll(page, size);
        }
        return searchByParrafo(terminoBuscado, page, size);
    }

    public List<DescripcionPortafolioDTO> getAll() {
        List<DescripcionPortafolio> entities = descripcionPortafolioRepository.findAll();
        return entities.stream()
                .map(DescripcionPortafolioMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    private Page<DescripcionPortafolioDTO> getAll(Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<DescripcionPortafolio> entities = descripcionPortafolioRepository.findAll(pageable);
        return entities.map(DescripcionPortafolioMapper::entityToDTO);

    }

    private Page<DescripcionPortafolioDTO> searchByParrafo(String terminoBuscado, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<DescripcionPortafolio> entities = descripcionPortafolioRepository
                .findByParrafoContainingIgnoreCase(terminoBuscado, pageable);
        return entities.map(DescripcionPortafolioMapper::entityToDTO);
    }

    @Override
    public DescripcionPortafolioDTO getById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        Optional<DescripcionPortafolio> descripcionPortafolio = descripcionPortafolioRepository.findById(id);
        if (descripcionPortafolio.isPresent()) {
            return DescripcionPortafolioMapper.entityToDTO(descripcionPortafolio.get());
        }
        return null;
    }

    @Override
    public DescripcionPortafolioDTO save(DescripcionPortafolioDTO detallePortafolioDTO) {
        if (detallePortafolioDTO == null) {
            throw new IllegalArgumentException("El detalle del portafolio no puede ser null");
        }
        DescripcionPortafolio entity = DescripcionPortafolioMapper.DTOToEntity(detallePortafolioDTO);
        if (entity == null) {
            throw new IllegalArgumentException("El detalle del portafolio no puede ser null");
        }
        DescripcionPortafolio savedDescripcionPortafolio = descripcionPortafolioRepository.save(entity);
        return DescripcionPortafolioMapper.entityToDTO(savedDescripcionPortafolio);
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0 || !descripcionPortafolioRepository.existsById(id)) {
            throw new IllegalArgumentException("Descripción de portafolio no encontrada con id: " + id);
        }
        descripcionPortafolioRepository.deleteById(id);
    }

}
