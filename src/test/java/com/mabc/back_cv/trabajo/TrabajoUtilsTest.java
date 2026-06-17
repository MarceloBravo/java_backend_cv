package com.mabc.back_cv.trabajo;

import com.mabc.back_cv.web.services.trabajo.TrabajoUtils;
import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.User;

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
public class TrabajoUtilsTest{

    private TrabajoUtils utils;
    private Trabajo entity;
    private TrabajoDTO dto;
    private User user;
    private UsuarioDTO userDTO;

    @BeforeEach
    void setUp(){
        utils = new TrabajoUtils();
        user = new User();
        user.setId(1L);
        user.setNombre("Juan");
        user.setApellido("Pérez");
        user.setEmail("juan@example.com");
        user.setPassword("pass123");
        user.setActivo(true);

        userDTO = new UsuarioDTO();
        userDTO.setId(1L);
        userDTO.setNombre("Juan");
        userDTO.setApellido("Pérez");
        userDTO.setEmail("juan@example.com");
        userDTO.setPassword("pass123");
        userDTO.setActivo(true);

        entity = new Trabajo(1L, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, user);
        dto = new TrabajoDTO(1L, 1, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, userDTO);
    }

    // --------------------------------------------------------------------
    // Pageable createPageable(Integer page, Integer size)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Genera un Pageable")
    void createPageable_whitAllCorrectParameters_returnPageable(){
        Pageable pageable = utils.createPageable(0, 10);
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    @DisplayName("Genera un Pageable con parametros nulos")
    void createPageable_whitNullParameters_returnPageable(){
        Pageable pageable = utils.createPageable(null, null);
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    @DisplayName("Genera un Pageable con parametros negativos")
    void createPageable_whitNegativeParameters_returnPageable(){
        Pageable pageable = utils.createPageable(-1, -20);
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    // ----------------------------------------------------------
    // entityToDTO
    // ----------------------------------------------------------
    @Test
    @DisplayName("Convierte un Entity Trabajo a TrabajoDTO")
    void entityToDTO_whitValidEntity_returnTrabajoDTO(){
        TrabajoDTO result = utils.entityToDTO(entity);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(entity.getCompany(), result.getCompany());
        assertEquals(entity.getPosition(), result.getPosition());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getUser().getId(), result.getUser().getId());
        assertEquals(entity.getUser().getNombre(), result.getUser().getNombre());
    }

    @Test
    @DisplayName("Retorna un null al pasar un null como parametro")
    void entityToDTO_whitNullEntity_returnNull(){
        TrabajoDTO result = utils.entityToDTO(null);
        assertNull(result);
    }

    // ----------------------------------------------------------
    // dtoToEntiy
    // ----------------------------------------------------------
    @Test
    @DisplayName("Convierte un TrabajoDTO a Entity Trabajo")
    void dtoToEntity_whitValidDTO_returnTrabajo(){
        Trabajo result = utils.dtoToEntity(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(dto.getCompany(), result.getCompany());
        assertEquals(dto.getPosition(), result.getPosition());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getUser().getId(), result.getUser().getId());
        assertEquals(dto.getUser().getNombre(), result.getUser().getNombre());
    }
    
    @Test
    @DisplayName("Convierte un TrabajoDTO a Entity cuando el id es null")
    void dtoToEntity_whithvalidDTOwhenIdIsNull_returnTrabajo(){
        dto.setId(null);
        Trabajo result = utils.dtoToEntity(dto);
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(dto.getCompany(), result.getCompany());
        assertEquals(dto.getPosition(), result.getPosition());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getUser().getId(), result.getUser().getId());
        assertEquals(dto.getUser().getNombre(), result.getUser().getNombre());
    }

    @Test
    @DisplayName("Retorna un null al pasar un null como parametro")
    void dtoToEntity_whitNullDTO_returnNull(){
        Trabajo result = utils.dtoToEntity(null);
        assertNull(result);
    }

}