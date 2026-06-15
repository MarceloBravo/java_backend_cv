package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.repositories.TrabajoRepository;
import com.mabc.back_cv.web.services.trabajo.TrabajoUtils;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class TrabajoService implements TrabajoService {

    @Autowired
    private TrabajoRepository repository;

    @Override
    public List<TrabajoDTO> getAll(Long userId, String searchText) {
        if (searchText == null) {
            searchText = "";
        }
        return repository.findAllList(userId, searchText)
                .stream()
                .map(TrabajoUtils::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TrabajoDTO> getAll(Long userId, S
    .tring searchText, Integer page, Integer size) {
        Pageable pageable = TrabajoUtils.createPageable(page, size);
        if (searchText == null) {
            searchText = "";
        }
        return repository.findAllPage(userId, searchText, pageable)
                .map(TrabajoUtils::entityToDTO);
    }

    @Override
    public TrabajoDTO getById(Long id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id)
                .map(TrabajoUtils::entityToDTO)
                .orElse(null);
    }

    @Override
    public TrabajoDTO save(TrabajoDTO trabajoDTO) {
        if (trabajoDTO == null) {
            throw new IllegalArgumentException("Datos no válidos para guardar el registro.");
        }
        Trabajo entity = TrabajoUtils.dtoToEntity(trabajoDTO);
        entity = repository.save(entity);
        return TrabajoUtils.entityToDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new RuntimeException("Trabajo no encontrado");
        }
        repository.deleteById(id);
    }

}