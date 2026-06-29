package com.mabc.back_cv.trabajo;

import com.mabc.back_cv.web.controllers.TrabajoController;
import com.mabc.back_cv.web.services.trabajo.TrabajoService;
import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;

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


@ExtendWith(MockitoExtension.class)
public class TrabajoControllerTest{

    @Mock
    private TrabajoService service;

    @InjectMocks
    private TrabajoController controller;

    private MockMvc mockMvc;
    private UsuarioDTO userDTO1;
    private UsuarioDTO userDTO2;
    private TrabajoDTO trabajoDTO1;
    private TrabajoDTO trabajoDTO2;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        userDTO1 = new UsuarioDTO();
        userDTO1.setId(1L);
        userDTO1.setNombre("Juan");
        userDTO1.setApellido("Pérez");
        userDTO1.setEmail("juan@example.com");
        userDTO1.setPassword("pass123");
        userDTO1.setActivo(true);

        userDTO2 = new UsuarioDTO();
        userDTO2.setId(2L);
        userDTO2.setNombre("Pedro");
        userDTO2.setApellido("Pérez");
        userDTO2.setEmail("pedro@example.com");
        userDTO2.setPassword("pass456");
        userDTO2.setActivo(true);

        trabajoDTO1 = new TrabajoDTO(1L, 1, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, userDTO1);
        trabajoDTO2 = new TrabajoDTO(2L, 2, "Empresa 2", "Posición 2", "Descripción trabajo 2", "2023-02-01", null, true, null, userDTO2);
        objectMapper = new ObjectMapper();
    }

    // --------------------------------------------------------------------
    // GET /trabajos/list
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista  de trabajos con éxito")
    void getAll_ShouldReturnListOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1, trabajoDTO2);

        when(service.getAll(1L, "")).thenReturn(trabajos);
        mockMvc.perform(get("/trabajos/list")
                .param("searchText", "")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).getAll(1L, "");
    }
    @Test
    @DisplayName("Obtiene una lista de trabajos con éxito con un userId valido y un termino de búsqueda")
    void getAll_whithUserIdValidAndSearchTerm_houldReturnListOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1);

        when(service.getAll(1L, "Empresa 1")).thenReturn(trabajos);
        mockMvc.perform(get("/trabajos/list")
                .param("searchText", "Empresa 1")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).getAll(1L, "Empresa 1");
    }
    
    @Test
    @DisplayName("Obtiene una lista de trabajos con éxito sólo con id de usuario")
    void getAll_WhithUserIdOnly_ShouldReturnListOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1, trabajoDTO2);

        when(service.getAll(1L, "")).thenReturn(trabajos);
        mockMvc.perform(get("/trabajos/list")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).getAll(1L, "");
    }
    
    @Test
    @DisplayName("Gsetiona un error en el controller al intentar obtener una lista de trabajos")
    void getAll_ShouldHandleError() throws Exception {

        when(service.getAll(1L, "")).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/trabajos/list")
                .param("userId", "1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getAll(1L, "");
    }
    
    @Test
    @DisplayName("Obtiene una lista de trabajos con éxito sin parámetros")
    void getAll_WhithOutParameters_ShouldReturnListOfTrabajos() throws Exception {
        
        mockMvc.perform(get("/trabajos/list"))
                    .andExpect(status().is4xxClientError());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Retorna una lista vacia con un userId no válido")
    void getAll_whithUserIdInvalid_shouldReturnEmptList() throws Exception {
        when(service.getAll(99999L, "")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/trabajos/list")
                    .param("userId", "99999"))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).getAll(99999L, "");
    }

    // --------------------------------------------------------------------
    // GET /trabajos/all
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página  de trabajos con éxito")
    void getPage_ShouldReturnPageOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1, trabajoDTO2);
        Page<TrabajoDTO> page = new PageImpl<>(trabajos, PageRequest.of(0, 10), 2);

        when(service.getAll(1L, "", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/trabajos/all")
                .param("searchText", "")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).getAll(1L, "", 0, 10);
    }
    
    @Test
    @DisplayName("Obtiene una página  de trabajos con éxito con un userId valido y un termino de búsqueda")
    void getPage_whithUserIdValidAndSearchTerm_houldReturnPageOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1);
        Page<TrabajoDTO> page = new PageImpl<>(trabajos, PageRequest.of(0, 10), 1);

        when(service.getAll(1L, "Empresa 1", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/trabajos/all")
                .param("searchText", "Empresa 1")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(service, times(1)).getAll(1L, "Empresa 1", 0, 10);
    }
    
    @Test
    @DisplayName("Obtiene una página  de trabajos con éxito con un userId valido, pero pagina y tamaño no validos")
    void getPage_whithUserIdValidAndPageAndSizeInvalid_shouldReturnPageOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1, trabajoDTO2);
        Page<TrabajoDTO> page = new PageImpl<>(trabajos, PageRequest.of(0, 10), 2);

        when(service.getAll(1L, "", -10, -200)).thenReturn(page);
        mockMvc.perform(get("/trabajos/all")
                .param("searchText", "")
                .param("userId", "1")
                .param("page", "-10")
                .param("size", "-200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).getAll(1L, "", -10, -200);
    }
    
    @Test
    @DisplayName("Obtiene una página  de trabajos con éxito sólo con id de usuaario")
    void getPage_WhithUserIdOnly_ShouldReturnPageOfTrabajos() throws Exception {
        List<TrabajoDTO> trabajos = Arrays.asList(trabajoDTO1, trabajoDTO2);
        Page<TrabajoDTO> page = new PageImpl<>(trabajos, PageRequest.of(0, 10), 2);

        when(service.getAll(1L, "", 0, 10)).thenReturn(page);
        mockMvc.perform(get("/trabajos/all")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).getAll(1L, "", 0, 10);
    }
    
    @Test
    @DisplayName("Gsetiona un error en el controller al intentar obtener una página de trabajos")
    void getPage_ShouldHandleError() throws Exception {

        when(service.getAll(1L, "", 0, 10)).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/trabajos/all")
                .param("userId", "1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getAll(1L, "", 0, 10);
    }
    
    @Test
    @DisplayName("Obtiene una página  de trabajos con éxito sin parámetros")
    void getPage_WhithOutParameters_ShouldReturnPageOfTrabajos() throws Exception {
        
        mockMvc.perform(get("/trabajos/all"))
                    .andExpect(status().is4xxClientError());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Retorna una página vacia con un userId no válido")
    void getPage_whithUserIdInvalid_shouldReturnEmptPage() throws Exception {
        Page<TrabajoDTO> page = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        when(service.getAll(99999L, "", 0, 10)).thenReturn(page);

        mockMvc.perform(get("/trabajos/all")
                    .param("userId", "99999"))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        verify(service, times(1)).getAll(99999L, "", 0, 10);
    }

    // --------------------------------------------------------------------
    // GET /trabajos/getById
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un registros de trabajo con éxito")
    void getById_ShouldReturnTrabajo() throws Exception {

        when(service.getById(1L)).thenReturn(trabajoDTO1);
        mockMvc.perform(get("/trabajos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.company", is("Empresa 1")));

        verify(service, times(1)).getById(1L);
    }
    
    @Test
    @DisplayName("Obtiene un not found al intentar obtener un registro de trabajo no existente")
    void getById_ShouldReturnNotFoundTrabajo() throws Exception {

        when(service.getById(99999L)).thenReturn((TrabajoDTO) null);
        mockMvc.perform(get("/trabajos/99999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getById(99999L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar obtener un trabajo")
    void getById_shouldHandleError() throws Exception {
        when(service.getById(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/trabajos/1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getById(1L);
    }

    // --------------------------------------------------------------------
    // POST /trabajos/save
    // --------------------------------------------------------------------
    @Test
    @DisplayName("crea un nuevo registro de trabajos con éxito")
    void save_ShouldReturnANewTrabajo() throws Exception {
        TrabajoDTO newTrabajoDTO = new TrabajoDTO(null, 1, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, userDTO1);

        when(service.save(any(TrabajoDTO.class))).thenReturn(trabajoDTO1);
        
        mockMvc.perform(post("/trabajos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTrabajoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.company", is("Empresa 1")));

        verify(service, times(1)).save(any(TrabajoDTO.class));
    }

    @Test
    @DisplayName("actualiza un registro de trabajos con éxito")
    void save_ShouldReturnUpdatedTrabajo() throws Exception {
        TrabajoDTO requestDTO = new TrabajoDTO(3L, 1, "Empresa 3", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, userDTO1);

        when(service.save(any(TrabajoDTO.class))).thenReturn(requestDTO);
        
        mockMvc.perform(post("/trabajos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.company", is("Empresa 3")));

        verify(service, times(1)).save(any(TrabajoDTO.class));
    }

    @Test
    @DisplayName("Gestiona un error al intentar registrar una trabajos")
    void save_shouldHandleError() throws Exception {
        when(service.save(any(TrabajoDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/trabajos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trabajoDTO1)))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).save(any(TrabajoDTO.class));
    }
    
    // --------------------------------------------------------------------
    // DELETE /trabajos/delete
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Elimina un registro de trabajos con éxito")
    void delete_ShouldReturnAStatusOk() throws Exception {

        mockMvc.perform(delete("/trabajos/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Gestiona un error al intentar eliminar una trabajos")
    void delete_shouldHandleError() throws Exception {
        doThrow(new RuntimeException("Error al eliminar en la BD")).when(service).deleteById(1L);

        mockMvc.perform(delete("/trabajos/delete/1"))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).deleteById(1L);
    }
}