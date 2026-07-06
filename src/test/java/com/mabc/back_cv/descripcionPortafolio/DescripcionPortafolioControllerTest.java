package com.mabc.back_cv.descripcionPortafolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabc.back_cv.web.controllers.DescripcionPortafolioController;
import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;

@ExtendWith(MockitoExtension.class)
class DescripcionPortafolioControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private DescripcionPortafolioService descripcionPortafolioService;

    @InjectMocks
    private DescripcionPortafolioController descripcionPortafolioController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(descripcionPortafolioController)
                .build();
    }

    @Test
    void shouldReturnAllDescriptions() throws Exception {
        DescripcionPortafolioDTO dto = createDto(1L, "texto prueba", 1);
        when(descripcionPortafolioService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/descripcion-portafolio/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].parrafo").value(dto.getParrafo()));
    }

    
    @Test
    @DisplayName("Error: maneja un error cuando el servicio lanza excepción al obtener todos los registros")
    void errorServicioLanzaExcepcionAlobtenerTosoLosRegistros() throws Exception {
        doThrow(new RuntimeException("Error al buscar portafolios"))
        .when(descripcionPortafolioService)
        .getAll();

        mockMvc.perform(get("/api/descripcion-portafolio/list"))
                .andExpect(status().isBadRequest());

        verify(descripcionPortafolioService, times(1)).getAll();
    }

    @Test
    void shouldReturnSearchResultsWithAllQueryParameters() throws Exception {
        DescripcionPortafolioDTO dto = createDto(2L, "busqueda prueba", 2);
        Page<DescripcionPortafolioDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1);
        when(descripcionPortafolioService.getAll(eq("busqueda"), eq(0), eq(5))).thenReturn(page);

        mockMvc.perform(get("/api/descripcion-portafolio/all")
                        .param("terminoBuscado", "busqueda")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].parrafo").value(dto.getParrafo()));
    }

    @Test
    void shouldReturnSearchResultsWhenParametersAreMissing() throws Exception {
        DescripcionPortafolioDTO dto = createDto(3L, "sin parametros", 3);
        Page<DescripcionPortafolioDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(descripcionPortafolioService.getAll(null, null, null)).thenReturn(page);

        mockMvc.perform(get("/api/descripcion-portafolio/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void shouldReturnBadRequestForInvalidPageParameter() throws Exception {
        mockMvc.perform(get("/api/descripcion-portafolio/all")
                        .param("page", "abc")
                        .param("size", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Error: maneja un error cuando el servicio lanza excepción")
    void errorServicioLanzaExcepcion() throws Exception {
        doThrow(new RuntimeException("Error al buscar portafolios"))
        .when(descripcionPortafolioService)
        .getAll(any(), anyInt(), anyInt());

        mockMvc.perform(get("/api/descripcion-portafolio/all")
                .param("terminoBuscado", "abc")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isInternalServerError());

        verify(descripcionPortafolioService, times(1)).getAll(any(), anyInt(), anyInt());
    }

    @Test
    void shouldReturnNotFoundWhenGetByIdDoesNotExist() throws Exception {
        when(descripcionPortafolioService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/descripcion-portafolio/99"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldReturnFoundWhenGetByIdIsValid() throws Exception {
        DescripcionPortafolioDTO dto = createDto(1L, "texto prueba", 1);
        when(descripcionPortafolioService.getById(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/api/descripcion-portafolio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void shouldReturnServerErrorWhenGetByIdThrowsException() throws Exception {
        when(descripcionPortafolioService.getById(anyLong())).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(get("/api/descripcion-portafolio/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldSaveDescriptionSuccessfully() throws Exception {
        DescripcionPortafolioDTO dto = createDto(null, "guardar descripcion", 1);
        DescripcionPortafolioDTO savedDto = createDto(10L, "guardar descripcion", 1);
        when(descripcionPortafolioService.save(any(DescripcionPortafolioDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/descripcion-portafolio/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedDto.getId()))
                .andExpect(jsonPath("$.parrafo").value(savedDto.getParrafo()));
    }

    @Test
    void shouldReturnServerErrorWhenSaveThrowsException() throws Exception {
        DescripcionPortafolioDTO dto = createDto(null, "error guardar", 1);
        when(descripcionPortafolioService.save(any(DescripcionPortafolioDTO.class)))
                .thenThrow(new RuntimeException("fail"));

        mockMvc.perform(post("/api/descripcion-portafolio/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldDeleteDescriptionSuccessfully() throws Exception {
        doNothing().when(descripcionPortafolioService).delete(1L);

        mockMvc.perform(delete("/api/descripcion-portafolio/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Descripción de portafolio eliminada correctamente"));
    }

    @Test
    void shouldReturnBadRequestWhenDeleteThrowsException() throws Exception {
        doThrow(new IllegalArgumentException("no existe")).when(descripcionPortafolioService).delete(1L);

        mockMvc.perform(delete("/api/descripcion-portafolio/delete/1"))
                .andExpect(status().isBadRequest());
    }

    private DescripcionPortafolioDTO createDto(Long id, String parrafo, Integer posicion) {
        Portafolio portafolio = new Portafolio();
        portafolio.setId(1L);
        portafolio.setTitle("portafolio prueba");
        return new DescripcionPortafolioDTO(id, parrafo, posicion, portafolio);
    }
}
