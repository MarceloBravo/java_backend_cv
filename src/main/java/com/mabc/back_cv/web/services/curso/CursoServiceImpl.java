package com.mabc.back_cv.web.services.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.repositories.CursoRepository;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de cursos.
 * Proporciona la lógica de negocio para las operaciones CRUD de cursos.
 */
@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public Page<CursoDTO> findByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<Curso> entity = cursoRepository.findByUserId(userId, pageable);
        return entity.map(CursoMapper::entityToDTO);
    }

    @Override
    public Page<CursoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<Curso> entity;
        if (searchText == null) {
            entity = cursoRepository.getAllByUserId(userId, pageable);
        } else {
            entity = cursoRepository.findBySearchText(userId, searchText, pageable);
        }
        return entity.map(CursoMapper::entityToDTO);
    }

    @Override
    public CursoDTO findById(Long id) {
        if (id == null) {
            return null;
        }
        Curso entity = cursoRepository.findById(id).orElse(null);
        return CursoMapper.entityToDTO(entity);
    }

    @Override
    public CursoDTO save(CursoDTO cursoDTO) {
        Curso entity = CursoMapper.dtoToEntity(cursoDTO);
        if (entity == null) {
            throw new IllegalArgumentException("Datos inválidos.");
        }
        entity = cursoRepository.save(entity);
        return CursoMapper.entityToDTO(entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null || !cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("Registro no encontrado o inexistente.");
        }
        cursoRepository.deleteById(id);
    }

}
