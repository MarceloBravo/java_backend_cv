package com.mabc.back_cv.tecnologia;

import com.mabc.back_cv.web.services.tecnologia.TecnologiaMapper;
import com.mabc.back_cv.web.dto.TecnologiaDTO;
import com.mabc.back_cv.web.entities.Tecnologia;
import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class TecnologiaMapperTest{

    private TecnologiaMapper utils;
    private Tecnologia entity;
    private TecnologiaDTO dto;

    @BeforeEach
    void setUp(){
        utils = new TecnologiaMapper();
        entity = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg javascript></svg>");
        dto = new TecnologiaDTO(1L, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg javascript></svg>");
    }

    // ----------------------------------------------------------
    // entityToDTO
    // ----------------------------------------------------------
    @Test
    @DisplayName("Convierte un Entity Tecnologia a TecnologiaDTO")
    void entityToDTO_whitValidEntity_returnTecnologiaDTO(){
        TecnologiaDTO result = utils.entityToDTO(entity);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
        assertEquals(TipoTecnologiaEnum.LENGUAJE, result.getType());
        assertEquals("/ruta/imagen/java.png", result.getPathImage());
        assertEquals("<svg javascript></svg>", result.getLogoSvg());
    }

    @Test
    @DisplayName("Retorna un null al pasar un null como parametro")
    void entityToDTO_whitNullEntity_returnNull(){
        TecnologiaDTO result = utils.entityToDTO(null);
        assertNull(result);
    }

    // ----------------------------------------------------------
    // dtoToEntiy
    // ----------------------------------------------------------
    @Test
    @DisplayName("Convierte un TecnologiaDTO a Entity Tecnologia")
    void dtoToEntity_whitValidDTO_returnTecnologia(){
        Tecnologia result = utils.dtoToEntity(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
        assertEquals(TipoTecnologiaEnum.LENGUAJE, result.getType());
        assertEquals("/ruta/imagen/java.png", result.getPathImage());
        assertEquals("<svg javascript></svg>", result.getLogoSvg());
    }
    
    @Test
    @DisplayName("Convierte un TecnologiaDTO a Entity cuando el id es null")
    void dtoToEntity_whithvalidDTOwhenIdIsNull_returnTecnologia(){
        dto.setId(null);
        Tecnologia result = utils.dtoToEntity(dto);
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals("Java", result.getName());
        assertEquals(TipoTecnologiaEnum.LENGUAJE, result.getType());
        assertEquals("/ruta/imagen/java.png", result.getPathImage());
        assertEquals("<svg javascript></svg>", result.getLogoSvg());
    }

    @Test
    @DisplayName("Retorna un null al pasar un null como parametro")
    void dtoToEntity_whitNullDTO_returnNull(){
        Tecnologia result = utils.dtoToEntity(null);
        assertNull(result);
    }




}