package com.mabc.back_cv.web.services.contenidoCurso;

import com.mabc.back_cv.web.repositories.ContenidoCursoRepository;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.mabc.back_cv.web.entities.ContenidoCurso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import com.mabc.back_cv.common.Utils;

@Service
public class ContenidoCursoServiceImpl implements ContenidoCursoService{

    @Autowired
    private ContenidoCursoRepository repository;

    @Autowired
    private Utils utils;

    private ModelMapper modelMapper;

    public List<ContenidoCursoDTO> findAllList(String searchText, Boolean activo){
        List<ContenidoCurso> entities = this.repository.findAllList(searchText, activo);
        return entities
            .stream()
            .map(entity -> modelMapper.map(entity, ContenidoCursoDTO.class))
            .collect(Collectors.toList());
    }

    public Page<ContenidoCursoDTO> findAllPage(String searchText, Integer page, Integer size, Boolean activo){
        Pageable pageable = utils.createPageable(page, size);
        Page<ContenidoCurso> entities = this.repository.findAllPage(searchText, activo, pageable);
        return entities.map(entity -> modelMapper.map(entity, ContenidoCursoDTO.class));
    }

    public ContenidoCursoDTO getById(Long id){
        if(id == null){
            return null;
        }
        ContenidoCurso entity = repository.findById(id).orElse(null);
        return entity != null ? modelMapper.map(entity, ContenidoCursoDTO.class) : null;
    }

    public ContenidoCursoDTO save(ContenidoCursoDTO dto){
        if(dto == null){
            return null;
        }
        ContenidoCurso entity = modelMapper.map(dto, ContenidoCurso.class);
        ContenidoCurso savedEntity = repository.save(entity);
        return savedEntity != null ? modelMapper.map(savedEntity, ContenidoCursoDTO.class) : null;
    }

    public void delete(Long id){
        if(id == null || !repository.existsById(id)){
            throw new IllegalArgumentException("Registro no encontrado o inexistente.");
        }
        repository.deleteById(id);
    }

}