package com.mabc.back_cv.web.services.contenidoCurso;

import com.mabc.back_cv.web.repositories.ContenidoCursoRepository;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.mabc.back_cv.web.entities.ContenidoCurso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de contenido de cursos.
 * Proporciona la lógica de negocio para las operaciones CRUD de contenido de cursos.
 */
@Service
public class ContenidoCursoServiceImpl implements ContenidoCursoService{

    @Autowired
    private ContenidoCursoRepository repository;


    public List<ContenidoCursoDTO> findAllList(String searchText, Boolean activo){
        List<ContenidoCurso> entities = this.repository.findAllList(searchText, activo);
        return entities
            .stream()
            .map(ContenidoCursoMapper::entityToDTO)
            .collect(Collectors.toList());
    }

    public Page<ContenidoCursoDTO> findAllPage(String searchText, Integer page, Integer size, Boolean activo){
        Pageable pageable = Utils.createPageable(page, size);
        Page<ContenidoCurso> entities = this.repository.findAllPage(searchText, activo, pageable);
        return entities.map(ContenidoCursoMapper::entityToDTO);
    }

    public ContenidoCursoDTO getById(Long id){
        if(id == null){
            return null;
        }
        ContenidoCurso entity = repository.findById(id).orElse(null);
        return ContenidoCursoMapper.entityToDTO(entity);
    }

    public ContenidoCursoDTO save(ContenidoCursoDTO dto){
        if(dto == null){
            return null;
        }
        ContenidoCurso entity = ContenidoCursoMapper.dtoToEntity(dto);
        ContenidoCurso savedEntity = repository.save(entity);
        return ContenidoCursoMapper.entityToDTO(savedEntity);
    }

    public void delete(Long id){
        if(id == null || !repository.existsById(id)){
            throw new IllegalArgumentException("Registro no encontrado o inexistente.");
        }
        repository.deleteById(id);
    }

}