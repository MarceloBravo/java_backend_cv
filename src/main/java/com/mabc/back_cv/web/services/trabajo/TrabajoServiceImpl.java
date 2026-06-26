package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.repositories.TrabajoRepository;
import com.mabc.back_cv.web.services.trabajo.TrabajoMapper;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;

import com.mabc.back_cv.web.services.trabajo.TrabajoService;
import com.mabc.back_cv.web.services.trabajo.TrabajoMapper;
import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.entities.Trabajo;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de trabajos o experiencias laborales.
 * Proporciona la lógica de negocio para las operaciones CRUD de trabajos.
 */
@Service
public class TrabajoServiceImpl implements TrabajoService {

    @Autowired
    private TrabajoRepository repository;
    

    @Override
    public List<TrabajoDTO> getAll(Long userId, String searchText) {
        if (searchText == null) {
            searchText = "";
        }
        return repository.findAllList(userId, searchText)
                .stream()
                .map(TrabajoMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TrabajoDTO> getAll(Long userId, String searchText, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        if (searchText == null) {
            searchText = "";
        }
        return repository.findAllPage(userId, searchText, pageable)
                .map(TrabajoMapper::entityToDTO);
    }

    @Override
    public TrabajoDTO getById(Long id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id)
                .map(TrabajoMapper::entityToDTO)
                .orElse(null);
    }

    @Override
    public TrabajoDTO save(TrabajoDTO trabajoDTO) {
        if (trabajoDTO == null) {
            throw new IllegalArgumentException("Datos no válidos para guardar el registro.");
        }
        Trabajo entity = TrabajoMapper.dtoToEntity(trabajoDTO);
        entity = repository.save(entity);
        return TrabajoMapper.entityToDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new RuntimeException("Trabajo no encontrado");
        }
        repository.deleteById(id);
    }

}