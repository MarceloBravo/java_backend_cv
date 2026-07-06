package com.mabc.back_cv.curso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.curso.CursoService;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mabc.back_cv.web.controllers.CursoController;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para CursoController")
class CursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    private ObjectMapper objectMapper;

    private CursoDTO cursoDTO;
    private User usuario;
    private Rol rol;
    private Page<CursoDTO> cursoPage;

    @BeforeEach
    void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(cursoController)
                .setMessageConverters(converter)
                .build();

        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");

        usuario = new User();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("password123");
        usuario.setFono("123456789");
        usuario.setDireccion("Calle 123");
        usuario.setCiudad("Santiago");
        usuario.setIdioma("es");
        usuario.setRol(rol);

        cursoDTO = new CursoDTO();
        cursoDTO.setId(1L);
        cursoDTO.setName("Curso de Java");
        cursoDTO.setTitle("Java Avanzado");
        cursoDTO.setInstitute("Oracle University");
        cursoDTO.setStartDate(new Date());
        cursoDTO.setEndDate(new Date());
        cursoDTO.setActivo(true);
        cursoDTO.setUsuario(null);

        List<CursoDTO> contenido = List.of(cursoDTO);
        cursoPage = new PageImpl<>(contenido, PageRequest.of(0, 10), contenido.size());
    }

    @Test
    void getAll_conParametrosValidos_retorna200() throws Exception {
        when(cursoService.findBySearchText(1L, "Java", 0, 10))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_sinParametros_retorna200() throws Exception {
        when(cursoService.findBySearchText(null, null, null, null))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conUserIdNull_retorna200() throws Exception {
        when(cursoService.findBySearchText(null, "test", 0, 10))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("searchText", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSearchTextNull_retorna200() throws Exception {
        when(cursoService.findBySearchText(1L, null, 0, 10))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conPageNull_retorna200() throws Exception {
        when(cursoService.findBySearchText(1L, "Java", null, 10))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeNull_retorna200() throws Exception {
        when(cursoService.findBySearchText(1L, "Java", 0, null))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("page", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conPageNegativo_retorna200() throws Exception {
        when(cursoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("page", "-1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeCero_retorna200() throws Exception {
        when(cursoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("size", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeNegativo_retorna200() throws Exception {
        when(cursoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1")
                .param("size", "-5"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(cursoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/api/curso/all")
                .param("userId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getById_conIdValido_retorna200() throws Exception {
        when(cursoService.findById(1L)).thenReturn(cursoDTO);

        mockMvc.perform(get("/api/curso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Curso de Java"))
                .andExpect(jsonPath("$.title").value("Java Avanzado"));
    }

    @Test
    void getById_conIdNoExistente_retorna404() throws Exception {
        when(cursoService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/curso/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdCero_retorna404() throws Exception {
        when(cursoService.findById(0L)).thenReturn(null);

        mockMvc.perform(get("/api/curso/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdNegativo_retorna404() throws Exception {
        when(cursoService.findById(-1L)).thenReturn(null);

        mockMvc.perform(get("/api/curso/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(cursoService.findById(1L)).thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/api/curso/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllByUserId_conUserIdValido_retorna200() throws Exception {
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conPageNull_retorna200() throws Exception {
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/user/1")
                .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conSizeNull_retorna200() throws Exception {
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/user/1")
                .param("page", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conPaginacionValida_retorna200() throws Exception {
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(cursoPage);

        mockMvc.perform(get("/api/curso/user/1")
                .param("page", "2")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    void getAllByUserId_conPaginaVacia_retorna200() throws Exception {
        Page<CursoDTO> paginaVacia = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(paginaVacia);

        mockMvc.perform(get("/api/curso/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getAllByUserId_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(cursoService.findByUserId(anyLong(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/api/curso/user/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conDTOValido_retorna200() throws Exception {
        when(cursoService.save(any(CursoDTO.class))).thenReturn(cursoDTO);

        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Curso de Java"))
                .andExpect(jsonPath("$.title").value("Java Avanzado"));
    }

    @Test
    void save_conDTOConIdNull_retorna200() throws Exception {
        cursoDTO.setId(null);
        CursoDTO cursoGuardado = new CursoDTO();
        cursoGuardado.setId(2L);
        cursoGuardado.setName("Curso de Java");
        cursoGuardado.setTitle("Java Avanzado");
        cursoGuardado.setInstitute("Oracle University");
        cursoGuardado.setStartDate(new Date());
        cursoGuardado.setEndDate(new Date());
        cursoGuardado.setUsuario(UsuarioMapper.userToDTO(usuario));

        when(cursoService.save(any(CursoDTO.class))).thenReturn(cursoGuardado);

        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void save_conDTOSinCamposObligatorios_retorna500() throws Exception {
        CursoDTO dtoIncompleto = new CursoDTO();
        dtoIncompleto.setName(null);
        dtoIncompleto.setUsuario(UsuarioMapper.userToDTO(usuario));

        when(cursoService.save(any(CursoDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIncompleto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conRequestBodyNull_retorna500() throws Exception {
        when(cursoService.save(any(CursoDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(cursoService.save(any(CursoDTO.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conJSONInvalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/curso/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conIdValido_retorna200() throws Exception {
        doNothing().when(cursoService).delete(1L);

        mockMvc.perform(delete("/api/curso/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Curso eliminado correctamente\""));
    }

    @Test
    void delete_conIdNoExistente_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(cursoService).delete(999L);

        mockMvc.perform(delete("/api/curso/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el curso\""));
    }

    @Test
    void delete_conIdCero_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(cursoService).delete(0L);

        mockMvc.perform(delete("/api/curso/delete/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el curso\""));
    }

    @Test
    void delete_conIdNegativo_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(cursoService).delete(-1L);

        mockMvc.perform(delete("/api/curso/delete/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el curso\""));
    }

    @Test
    void delete_conServiceLanzaExcepcion_retorna400() throws Exception {
        doThrow(new RuntimeException("Error de base de datos"))
                .when(cursoService).delete(1L);

        mockMvc.perform(delete("/api/curso/delete/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el curso\""));
    }

    @Test
    void delete_conIdNull_retorna400() throws Exception {
        mockMvc.perform(delete("/api/curso/delete/null"))
                .andExpect(status().isBadRequest());
    }
}
