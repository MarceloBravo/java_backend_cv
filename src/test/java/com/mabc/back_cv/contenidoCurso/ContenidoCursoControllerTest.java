package com.mabc.back_cv.contenidoCurso;

import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.services.contenidoCurso.ContenidoCursoService;
import com.mabc.back_cv.web.controllers.ContenidoCursoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para ContenidoCurso")
public class ContenidoCursoControllerTest{

    private MockMvc mockMvc;

    @Mock
    private ContenidoCursoService service;

    @InjectMocks
    private ContenidoCursoController controller;

    private ObjectMapper objectMapper;
    private ContenidoCurso contenidoCurso1;
    private ContenidoCurso contenidoCurso2;
    private ContenidoCurso contenidoCurso3;
    private ContenidoCursoDTO contenidoCursoDTO1;
    private ContenidoCursoDTO contenidoCursoDTO2;
    private ContenidoCursoDTO contenidoCursoDTO3;

    @BeforeEach
    void Setup(){
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        contenidoCurso1 = new ContenidoCurso(1L, "Curso 1", "Contenido de ejemplo del curso 1", true);
        contenidoCurso2 = new ContenidoCurso(2L, "Curso 2", "Contenido de ejemplo del curso 2", true);
        contenidoCurso3 = new ContenidoCurso(3L, "Curso 3", "Contenido de ejemplo del curso 3", false);
        
        contenidoCursoDTO1 = new ContenidoCursoDTO(1L, "Curso 1", "Contenido de ejemplo del curso 1", true);
        contenidoCursoDTO2 = new ContenidoCursoDTO(2L, "Curso 2", "Contenido de ejemplo del curso 2", true);
        contenidoCursoDTO3 = new ContenidoCursoDTO(3L, "Curso 3", "Contenido de ejemplo del curso 3", false);
    }

    private ContenidoCursoDTO entityToDTO(ContenidoCurso entity){
        ContenidoCursoDTO dto = new ContenidoCursoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setActivo(entity.getActivo());
        
        return dto;
    }
    
    // --------------------------------------------------------------------
    // GET /contenido-curso/all
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página  de contenidocurso con éxito")
    void getPage_ShouldReturnPageOfContenidoCursos() throws Exception {
        List<ContenidoCursoDTO> contenidoCurso = Arrays.asList(contenidoCursoDTO1, contenidoCursoDTO2);
        Page<ContenidoCursoDTO> page = new PageImpl<>(contenidoCurso, PageRequest.of(0, 10), 2);

        when(service.findAllPage(any(), any(), any(), any())).thenReturn(page);
        mockMvc.perform(get("/contenido-curso/all")
                .param("searchText", "")
                .param("page", "0")
                .param("size", "10")
                .param("activo", "true")
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAllPage(any(), any(), any(), any());
    }
    
    @Test
    @DisplayName("Obtiene una página de contenidoCurso sin pasar parámetros")
    void getPage_shouldReturnPageOfContenidoCursosWithoutParameters() throws Exception {
        List<ContenidoCursoDTO> contenidoCurso = Arrays.asList(contenidoCursoDTO1, contenidoCursoDTO2);
        Page<ContenidoCursoDTO> page = new PageImpl<>(contenidoCurso, PageRequest.of(0, 10), 2);

        when(service.findAllPage(any(), any(), any(), any())).thenReturn(page);
        mockMvc.perform(get("/contenido-curso/all")
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAllPage(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Obtiene una página con un registro de contenidoCurso con éxito")
    void getPage_ShouldReturnPageWithOnceContenidoCurso() throws Exception {
        List<ContenidoCursoDTO> contenidoCurso = Arrays.asList(contenidoCursoDTO1);
        Page<ContenidoCursoDTO> page = new PageImpl<>(contenidoCurso, PageRequest.of(0, 10), 1);

        when(service.findAllPage(any(), any(), any(), any())).thenReturn(page);
        mockMvc.perform(get("/contenido-curso/all")
                .param("searchText", "Curso 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.content[0].description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.content[0].activo", is(contenidoCursoDTO1.getActivo())));

        verify(service, times(1)).findAllPage(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Obtiene una página de contenidoCurso con page y size negativos")
    void getAll_shouldReturnPageOfContenidoCursosNegativePageAndSizeParameters() throws Exception {
        List<ContenidoCursoDTO> contenidoCurso = Arrays.asList(contenidoCursoDTO1, contenidoCursoDTO2);
        Page<ContenidoCursoDTO> page = new PageImpl<>(contenidoCurso, PageRequest.of(0, 10), 2);

        when(service.findAllPage("", -1, -10, true)).thenReturn(page);
        mockMvc.perform(get("/contenido-curso/all")
                .param("searchText", "")
                .param("page", "-1")
                .param("size", "-10")
                .param("activo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.content[0].description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.content[0].activo", is(contenidoCursoDTO1.getActivo())))
                .andExpect(jsonPath("$.content[1].title", is(contenidoCursoDTO2.getTitle())))
                .andExpect(jsonPath("$.content[1].description", is(contenidoCursoDTO2.getDescription())))
                .andExpect(jsonPath("$.content[1].activo", is(contenidoCursoDTO2.getActivo())));

        verify(service, times(1)).findAllPage("", -1, -10, true);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una página de contenidoCurso")
    void getAll_shouldHandleError() throws Exception {
        when(service.findAllPage("", 0, 10, true)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/contenido-curso/all")
                    .param("searchText", "")
                    .param("page", "0")
                    .param("size", "10")
                    .param("activo", "true"))
                    .andExpect(status().is5xxServerError());

        verify(service, times(1)).findAllPage("", 0, 10, true);
    }
    
    // --------------------------------------------------------------------
    // GET /contenido-curso/list
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista de las contenidoCurso con éxito")
    void getList_ShouldReturnListOfContenidoCursos() throws Exception {
        List<ContenidoCursoDTO> contenidoCurso = Arrays.asList(contenidoCursoDTO1, contenidoCursoDTO2);

        when(service.findAllList(null, null)).thenReturn(contenidoCurso);
        mockMvc.perform(get("/contenido-curso/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).findAllList(null, null);
    }

    @Test
    @DisplayName("Obtiene una lista con un registro de contenidoCurso con éxito")
    void getList_ShouldReturnListWithOnceContenidoCurso() throws Exception {
        List<ContenidoCursoDTO> contenidocurso = Arrays.asList(contenidoCursoDTO1);

        when(service.findAllList("Curso 1", true)).thenReturn(contenidocurso);
        mockMvc.perform(get("/contenido-curso/list")
                .param("searchText", "Curso 1")
                .param("activo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.[0].activo", is(contenidoCursoDTO1.getActivo())));

        verify(service, times(1)).findAllList("Curso 1", true);
    }

    @Test
    @DisplayName("Obtiene una lista con un registro éxitosamente, recibiendo sólo el parámetro searchText")
    void getList_ShouldReturnListWitSearchTextParam_returnOnceContenidoCurso() throws Exception {
        List<ContenidoCursoDTO> contenidocurso = Arrays.asList(contenidoCursoDTO1);

        when(service.findAllList("Curso 1", null)).thenReturn(contenidocurso);
        mockMvc.perform(get("/contenido-curso/list")
                .param("searchText", "Curso 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.[0].activo", is(contenidoCursoDTO1.getActivo())));

        verify(service, times(1)).findAllList("Curso 1", null);
    }

    @Test
    @DisplayName("Obtiene una lista con un registro éxitosamente, recibiendo sólo el parámetro activo")
    void getList_ShouldReturnListWitActivoParam_returnOnceContenidoCurso() throws Exception {
        List<ContenidoCursoDTO> contenidocurso = Arrays.asList(contenidoCursoDTO3);

        when(service.findAllList(null, false)).thenReturn(contenidocurso);
        mockMvc.perform(get("/contenido-curso/list")
                .param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].title", is(contenidoCursoDTO3.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(contenidoCursoDTO3.getDescription())))
                .andExpect(jsonPath("$.[0].activo", is(false)));

        verify(service, times(1)).findAllList(null, false);
    }

    @Test
    @DisplayName("Obtiene una lista vacia, recibiendo sólo el parámetro activo como false y un searchText de un registros activo")
    void getList_ShouldReturnListWitAllParams_returnEmptyList() throws Exception {

        when(service.findAllList("Curso 1", false)).thenReturn(Arrays.asList());
        mockMvc.perform(get("/contenido-curso/list")
                .param("searchText", "Curso 1")
                .param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).findAllList("Curso 1", false);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una lista de contenidocurso")
    void getList_shouldHandleError() throws Exception {
        when(service.findAllList(null, null)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/contenido-curso/list"))
                    .andExpect(status().is5xxServerError());

        verify(service, times(1)).findAllList(null, null);
    }
    

    // --------------------------------------------------------------------
    // GET /contenido-curso/getById
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un registros de contenidoCurso con éxito")
    void getById_ShouldReturnContenidoCurso() throws Exception {

        when(service.getById(1L)).thenReturn(contenidoCursoDTO1);
        mockMvc.perform(get("/contenido-curso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.activo", is(contenidoCursoDTO1.getActivo())));

        verify(service, times(1)).getById(1L);
    }
    
    @Test
    @DisplayName("Obtiene un not found al intentar obtener un registro de contenidoCurso")
    void getById_ShouldReturnNotFoundContenidoCurso() throws Exception {

        when(service.getById(99999L)).thenReturn((ContenidoCursoDTO) null);
        mockMvc.perform(get("/contenido-curso/99999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getById(99999L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una lista de contenidocurso")
    void getById_shouldHandleError() throws Exception {
        when(service.getById(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/contenido-curso/1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getById(1L);
    }

    // --------------------------------------------------------------------
    // POST /contenido-curso/save
    // --------------------------------------------------------------------
    @Test
    @DisplayName("crea un nuevo registro de contenidocurso con éxito")
    void save_ShouldReturnANewContenidoCurso() throws Exception {
        ContenidoCursoDTO newContenidoCursoDTO = new ContenidoCursoDTO(null, "Curso 1", "Contenido de ejemplo del curso 1", true);

        when(service.save(any(ContenidoCursoDTO.class))).thenReturn(contenidoCursoDTO1);
        
        mockMvc.perform(post("/contenido-curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newContenidoCursoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(contenidoCursoDTO1.getTitle())))
                .andExpect(jsonPath("$.description", is(contenidoCursoDTO1.getDescription())))
                .andExpect(jsonPath("$.activo", is(contenidoCursoDTO1.getActivo())));

        verify(service, times(1)).save(any(ContenidoCursoDTO.class));
    }

    @Test
    @DisplayName("actualiza un registro de contenidocurso con éxito")
    void save_ShouldReturnUpdatedContenidoCurso() throws Exception {
        ContenidoCursoDTO requestDTO = new ContenidoCursoDTO(3L, "Curso 1", "Contenido de ejemplo del curso 1", true);

        when(service.save(any(ContenidoCursoDTO.class))).thenReturn(requestDTO);
        
        mockMvc.perform(post("/contenido-curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is(requestDTO.getTitle())))
                .andExpect(jsonPath("$.description", is(requestDTO.getDescription())))
                .andExpect(jsonPath("$.activo", is(requestDTO.getActivo())));

        verify(service, times(1)).save(any(ContenidoCursoDTO.class));
    }

    @Test
    @DisplayName("Gestiona un error al intentar registrar un contenidocurso")
    void save_shouldHandleError() throws Exception {
        when(service.save(any(ContenidoCursoDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/contenido-curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contenidoCursoDTO1)))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).save(any(ContenidoCursoDTO.class));
    }
    
    // --------------------------------------------------------------------
    // DELETE /contenido-curso/delete
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Elimina un registro de contenidocurso con éxito")
    void delete_ShouldReturnAStatusOk() throws Exception {

        mockMvc.perform(delete("/contenido-curso/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar eliminar una contenidocurso")
    void delete_shouldHandleError() throws Exception {
        doThrow(new RuntimeException("Error al eliminar en la BD")).when(service).delete(1L);

        mockMvc.perform(delete("/contenido-curso/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar eliminar un contenidocurso inexistente")
    void deleteANonExistentRecord_shouldHandleError() throws Exception {
        doThrow(new RuntimeException("Registro no encontrado o inexistente.")).when(service).delete(1L);

        mockMvc.perform(delete("/contenido-curso/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(1L);
    }
}