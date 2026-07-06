package com.mabc.back_cv.tecnologia;

import com.mabc.back_cv.web.controllers.TecnologiaController;
import com.mabc.back_cv.web.services.tecnologia.TecnologiaService;
import com.mabc.back_cv.web.dto.TecnologiaDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;




@ExtendWith(MockitoExtension.class)
public class TecnologiaControllerTest{

    @Mock
    private TecnologiaService service;

    @InjectMocks
    private TecnologiaController controller;

    private MockMvc mockMvc;
    private TecnologiaDTO tecnologiaDTO1;
    private TecnologiaDTO tecnologiaDTO2;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        tecnologiaDTO1 = new TecnologiaDTO(1L, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg javascript></svg>");
        tecnologiaDTO2 = new TecnologiaDTO(2L, "JavaScript", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/javascript.png", "<svg javascript></svg>");
        objectMapper = new ObjectMapper();
    }

    // --------------------------------------------------------------------
    // GET /tecnologias/all
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página  de tecnologias con éxito")
    void getAll_ShouldReturnPageOfTecnologias() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1, tecnologiaDTO2);
        Page<TecnologiaDTO> page = new PageImpl<>(tecnologias, PageRequest.of(0, 10), 2);

        when(service.findAll("", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/api/tecnologias/all")
                .param("searchText", "")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAll("", 0, 10);
    }

    @Test
    @DisplayName("Obtiene una página de tecnologias sin pasar parámetros")
    void getAll_shouldReturnPageOfTecnologiasWithoutParameters() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1, tecnologiaDTO2);
        Page<TecnologiaDTO> page = new PageImpl<>(tecnologias, PageRequest.of(0, 10), 2);

        when(service.findAll("", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/api/tecnologias/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAll("", 0, 10);
    }

    @Test
    @DisplayName("Obtiene una página con un registro de tecnologia con éxito")
    void getAll_ShouldReturnPageWithOnceTecnologia() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1);
        Page<TecnologiaDTO> page = new PageImpl<>(tecnologias, PageRequest.of(0, 10), 1);

        when(service.findAll("Java", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/api/tecnologias/all")
                .param("searchText", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Java")));

        verify(service, times(1)).findAll("Java", 0, 10);
    }

    @Test
    @DisplayName("Obtiene una página de tecnologias con page y size negativos")
    void getAll_shouldReturnPageOfTecnologiasNegativePageAndSizeParameters() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1, tecnologiaDTO2);
        Page<TecnologiaDTO> page = new PageImpl<>(tecnologias, PageRequest.of(0, 10), 2);

        when(service.findAll("", -1, -10)).thenReturn(page);
        mockMvc.perform(get("/api/tecnologias/all")
                .param("searchText", "")
                .param("page", "-1")
                .param("size", "-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAll("", -1, -10);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una página de tecnologias")
    void getAll_shouldHandleError() throws Exception {
        when(service.findAll("", 0, 10)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/tecnologias/all")
                    .param("searchText", "")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().is5xxServerError());

        verify(service, times(1)).findAll("", 0, 10);
    }
    
    // --------------------------------------------------------------------
    // GET /tecnologias/list
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista de las tecnologias con éxito")
    void getList_ShouldReturnListOfTecnologias() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1, tecnologiaDTO2);

        when(service.findAll("")).thenReturn(tecnologias);
        mockMvc.perform(get("/api/tecnologias/list")
                .param("searchText", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).findAll("");
    }

    @Test
    @DisplayName("Obtiene una lista de tecnologias sin pasar parámetros")
    void getList_shouldReturnListOfTecnologiasWithoutParameters() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1, tecnologiaDTO2);

        when(service.findAll("")).thenReturn(tecnologias);
        mockMvc.perform(get("/api/tecnologias/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).findAll("");
    }

    @Test
    @DisplayName("Obtiene una lista con un registro de tecnologia con éxito")
    void getList_ShouldReturnListWithOnceTecnologia() throws Exception {
        List<TecnologiaDTO> tecnologias = Arrays.asList(tecnologiaDTO1);

        when(service.findAll("Java")).thenReturn(tecnologias);
        mockMvc.perform(get("/api/tecnologias/list")
                .param("searchText", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Java")));

        verify(service, times(1)).findAll("Java");
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una lista de tecnologias")
    void getList_shouldHandleError() throws Exception {
        when(service.findAll("")).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/tecnologias/list")
                    .param("searchText", ""))
                    .andExpect(status().is5xxServerError());

        verify(service, times(1)).findAll("");
    }
    

    // --------------------------------------------------------------------
    // GET /tecnologias/getById
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un registros de tecnologia con éxito")
    void getById_ShouldReturnTecnologia() throws Exception {

        when(service.getById(1L)).thenReturn(tecnologiaDTO1);
        mockMvc.perform(get("/api/tecnologias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Java")));

        verify(service, times(1)).getById(1L);
    }
    
    @Test
    @DisplayName("Obtiene un not found al intentar obtener un registro de tecnologia")
    void getById_ShouldReturnNotFoundTecnologia() throws Exception {

        when(service.getById(99999L)).thenReturn((TecnologiaDTO) null);
        mockMvc.perform(get("/api/tecnologias/99999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getById(99999L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener una lista de tecnologias")
    void getById_shouldHandleError() throws Exception {
        when(service.getById(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/tecnologias/1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getById(1L);
    }

    // --------------------------------------------------------------------
    // POST /tecnologias/save
    // --------------------------------------------------------------------
    @Test
    @DisplayName("crea un nuevo registro de tecnologias con éxito")
    void save_ShouldReturnANewTecnologia() throws Exception {
        TecnologiaDTO newTecnologiaDTO = new TecnologiaDTO(null, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg java></svg>");

        when(service.save(any(TecnologiaDTO.class))).thenReturn(tecnologiaDTO1);
        
        mockMvc.perform(post("/api/tecnologias/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTecnologiaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java")));

        verify(service, times(1)).save(any(TecnologiaDTO.class));
    }

    @Test
    @DisplayName("actualiza un registro de tecnologias con éxito")
    void save_ShouldReturnUpdatedTecnologia() throws Exception {
        TecnologiaDTO requestDTO = new TecnologiaDTO(3L, "PHP", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg java></svg>");

        when(service.save(any(TecnologiaDTO.class))).thenReturn(requestDTO);
        
        mockMvc.perform(post("/api/tecnologias/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("PHP")));

        verify(service, times(1)).save(any(TecnologiaDTO.class));
    }

    @Test
    @DisplayName("Gestiona un error al intentar registrar una tecnologias")
    void save_shouldHandleError() throws Exception {
        when(service.save(any(TecnologiaDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/tecnologias/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tecnologiaDTO1)))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).save(any(TecnologiaDTO.class));
    }
    
    // --------------------------------------------------------------------
    // DELETE /tecnologias/delete
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Elimina un registro de tecnologias con éxito")
    void delete_ShouldReturnAStatusOk() throws Exception {

        mockMvc.perform(delete("/api/tecnologias/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar eliminar una tecnologias")
    void delete_shouldHandleError() throws Exception {
        doThrow(new RuntimeException("Error al eliminar en la BD")).when(service).deleteById(1L);

        mockMvc.perform(delete("/api/tecnologias/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).deleteById(1L);
    }
}