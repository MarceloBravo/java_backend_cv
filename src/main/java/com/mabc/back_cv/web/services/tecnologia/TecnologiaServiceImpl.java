package com.mabc.back_cv.web.services.tecnologia;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.TecnologiaDTO;
import com.mabc.back_cv.web.entities.Tecnologia;
import com.mabc.back_cv.web.repositories.TecnologiaRepository;

import org.springframework.beans.factory.annotation.Autowired;

import com.mabc.back_cv.web.services.tecnologia.TecnologiaMapper;

import com.mabc.back_cv.common.Utils;


@Service
public class TecnologiaServiceImpl implements TecnologiaService{

    @Autowired
    private TecnologiaRepository repository;


    public Page<TecnologiaDTO> findAll(String searchText, Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        if(searchText == null){
            searchText = "";
        }
        Page<Tecnologia> pageTecnologia = repository.findAllPage(searchText, pageable);
        return pageTecnologia.map(TecnologiaMapper::entityToDTO);
    }
    
    public List<TecnologiaDTO> findAll(String searchText){
        if(searchText == null){
            searchText = "";
        }
        List<Tecnologia> listTecnologia = repository.findAllList(searchText);
        return listTecnologia
                .stream()
                .map(TecnologiaMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public TecnologiaDTO getById(Long id){
        if(id == null){
            return null;
        }
        Tecnologia entity = repository.findById(id).orElse(null);
        return TecnologiaMapper.entityToDTO(entity); 
    }

    public TecnologiaDTO save(TecnologiaDTO tecnologiaDTO){
        Tecnologia entity = TecnologiaMapper.dtoToEntity(tecnologiaDTO);
        if(tecnologiaDTO == null){
            throw new IllegalArgumentException("Datos no válidos para guardar el registro.");
        }
        entity = repository.save(entity);
        return TecnologiaMapper.entityToDTO(entity); 
    }

    public void deleteById(Long id){
        if(id == null || !repository.existsById(id)){
            throw new RuntimeException("Tecnologia no encontrada");
        }
        repository.deleteById(id);
    }

}