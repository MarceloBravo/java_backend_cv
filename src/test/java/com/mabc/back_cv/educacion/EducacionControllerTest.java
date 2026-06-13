package com.mabc.back_cv.educacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.educacion.EducacionService;
import com.mabc.back_cv.web.services.usuarios.UsuarioUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mabc.back_cv.web.controllers.EducacionController;

import org.junit.jupiter.api.DisplayName;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para EducacionController")
class EducacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EducacionService educacionService;


    @InjectMocks
    private EducacionController educacionController;

    private ObjectMapper objectMapper;

    private EducacionDTO educacionDTO;
    private User usuario;
    private Rol rol;
    private Page<EducacionDTO> educacionPage;



    @BeforeEach
    void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(educacionController)
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

        educacionDTO = new EducacionDTO();
        educacionDTO.setId(1L);
        educacionDTO.setInstitution("Universidad de Chile");
        educacionDTO.setTitle("Ingeniería Civil");
        educacionDTO.setShortTitle("Ing. Civil");
        educacionDTO.setName("Juan Pérez");
        educacionDTO.setDescription("Carrera de ingeniería civil");
        educacionDTO.setYearFrom(2018);
        educacionDTO.setYearTo(2023);
        educacionDTO.setDuration(10);
        educacionDTO.setImage("logo.png");
        educacionDTO.setUrl("https://uchile.cl");
        educacionDTO.setStyles("style1");
        educacionDTO.setUsuario(null); // Usuario null para evitar problemas de serialización en tests de Page

        List<EducacionDTO> contenido = List.of(educacionDTO);
        educacionPage = new PageImpl<>(contenido, PageRequest.of(0, 10), contenido.size());
    }

    // Tests para GET /educacion/all
    @Test
    void getAll_conParametrosValidos_retorna200() throws Exception {
        when(educacionService.findBySearchText(1L, "Ingeniería", 0, 10))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("searchText", "Ingeniería")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }


    @Test
    void getAll_sinParametros_retorna200() throws Exception {
        when(educacionService.findBySearchText(null, null, null, null))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all"))
                .andExpect(status().isOk());
    }



    @Test
    void getAll_conUserIdNull_retorna200() throws Exception {
        when(educacionService.findBySearchText(null, "test", 0, 10))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("searchText", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }


    @Test
    void getAll_conSearchTextNull_retorna200() throws Exception {
        when(educacionService.findBySearchText(1L, null, 0, 10))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }



    @Test
    void getAll_conPageNull_retorna200() throws Exception {
        when(educacionService.findBySearchText(1L, "Ingeniería", null, 10))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("searchText", "Ingeniería")
                .param("size", "10"))
                .andExpect(status().isOk());
    }



    @Test
    void getAll_conSizeNull_retorna200() throws Exception {
        when(educacionService.findBySearchText(1L, "Ingeniería", 0, null))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("searchText", "Ingeniería")
                .param("page", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conPageNegativo_retorna200() throws Exception {
        when(educacionService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("page", "-1"))
                .andExpect(status().isOk());
    }


    @Test
    void getAll_conSizeCero_retorna200() throws Exception {
        when(educacionService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("size", "0"))
                .andExpect(status().isOk());

    }



    @Test
    void getAll_conSizeNegativo_retorna200() throws Exception {
        when(educacionService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1")
                .param("size", "-5"))
                .andExpect(status().isOk());

    }



    @Test
    void getAll_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(educacionService.findBySearchText(anyLong(), any(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/educacion/all")
                .param("userId", "1"))
                .andExpect(status().isInternalServerError());
    }



    // Tests para GET /educacion/{id}

    @Test
    void getById_conIdValido_retorna200() throws Exception {
        when(educacionService.findById(1L)).thenReturn(educacionDTO);

        mockMvc.perform(get("/educacion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institution").value("Universidad de Chile"))
                .andExpect(jsonPath("$.title").value("Ingeniería Civil"));
    }



    @Test
    void getById_conIdNoExistente_retorna404() throws Exception {
        when(educacionService.findById(999L)).thenReturn(null);
        mockMvc.perform(get("/educacion/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdCero_retorna404() throws Exception {
        when(educacionService.findById(0L)).thenReturn(null);
        mockMvc.perform(get("/educacion/0"))
                .andExpect(status().isNotFound());
    }


    @Test
    void getById_conIdNegativo_retorna404() throws Exception {
        when(educacionService.findById(-1L)).thenReturn(null);

        mockMvc.perform(get("/educacion/-1"))
                .andExpect(status().isNotFound());
    }



    @Test
    void getById_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(educacionService.findById(1L)).thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/educacion/1"))
                .andExpect(status().isInternalServerError());
    }



    // Tests para GET /educacion/user/{userId}
    @Test
    void getAllByUserId_conUserIdValido_retorna200() throws Exception {
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/user/1"))
                .andExpect(status().isOk());

    }


    @Test
    void getAllByUserId_conPageNull_retorna200() throws Exception {
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/user/1")
                .param("size", "20"))
                .andExpect(status().isOk());
    }



    @Test
    void getAllByUserId_conSizeNull_retorna200() throws Exception {
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/user/1")
                .param("page", "2"))
                .andExpect(status().isOk());

    }



    @Test
    void getAllByUserId_conPaginacionValida_retorna200() throws Exception {
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenReturn(educacionPage);

        mockMvc.perform(get("/educacion/user/1")
                .param("page", "2")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }


    @Test
    void getAllByUserId_conPaginaVacia_retorna200() throws Exception {
        Page<EducacionDTO> paginaVacia = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenReturn(paginaVacia);

        mockMvc.perform(get("/educacion/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }



    @Test
    void getAllByUserId_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(educacionService.findByUserId(anyLong(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/educacion/user/1"))
                .andExpect(status().isInternalServerError());
    }



    // Tests para POST /educacion/save

    @Test
    void save_conDTOValido_retorna200() throws Exception {
        when(educacionService.save(any(EducacionDTO.class))).thenReturn(educacionDTO);

        mockMvc.perform(post("/educacion/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(educacionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institution").value("Universidad de Chile"))
                .andExpect(jsonPath("$.title").value("Ingeniería Civil"));
    }


    @Test
    void save_conDTOConIdNull_retorna200() throws Exception {
        educacionDTO.setId(null);
        EducacionDTO educacionGuardada = new EducacionDTO();
        educacionGuardada.setId(2L);
        educacionGuardada.setInstitution("Universidad de Chile");
        educacionGuardada.setTitle("Ingeniería Civil");
        educacionGuardada.setShortTitle("Ing. Civil");
        educacionGuardada.setName("Juan Pérez");
        educacionGuardada.setDescription("Carrera de ingeniería civil");
        educacionGuardada.setYearFrom(2018);
        educacionGuardada.setYearTo(2023);
        educacionGuardada.setDuration(10);
        educacionGuardada.setUsuario(UsuarioUtils.userToDTO(usuario));

        when(educacionService.save(any(EducacionDTO.class))).thenReturn(educacionGuardada);

        mockMvc.perform(post("/educacion/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(educacionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void save_conDTOSinCamposObligatorios_retorna500() throws Exception {
        EducacionDTO dtoIncompleto = new EducacionDTO();
        dtoIncompleto.setInstitution(null);
        dtoIncompleto.setTitle(null);
        dtoIncompleto.setUsuario(UsuarioUtils.userToDTO(usuario));

        when(educacionService.save(any(EducacionDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/educacion/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIncompleto)))
                .andExpect(status().isInternalServerError());

    }



    @Test
    void save_conRequestBodyNull_retorna500() throws Exception {
        when(educacionService.save(any(EducacionDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/educacion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }



    @Test
    void save_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(educacionService.save(any(EducacionDTO.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(post("/educacion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(educacionDTO)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void save_conJSONInvalido_retorna400() throws Exception {
        mockMvc.perform(post("/educacion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }



    // Tests para DELETE /educacion/delete/{id}
    @Test
    void delete_conIdValido_retorna200() throws Exception {
        doNothing().when(educacionService).delete(1L);

        mockMvc.perform(delete("/educacion/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Educacion eliminada correctamente\""));
    }


    @Test
    void delete_conIdNoExistente_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(educacionService).delete(999L);

        mockMvc.perform(delete("/educacion/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar la educacion\""));
    }


    @Test
    void delete_conIdCero_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(educacionService).delete(0L);

        mockMvc.perform(delete("/educacion/delete/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar la educacion\""));
    }


    @Test
    void delete_conIdNegativo_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(educacionService).delete(-1L);

        mockMvc.perform(delete("/educacion/delete/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar la educacion\""));

    }



    @Test
    void delete_conServiceLanzaExcepcion_retorna400() throws Exception {
        doThrow(new RuntimeException("Error de base de datos"))
                .when(educacionService).delete(1L);

        mockMvc.perform(delete("/educacion/delete/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar la educacion\""));
    }


    @Test
    void delete_conIdNull_retorna400() throws Exception {
        mockMvc.perform(delete("/educacion/delete/null"))
                .andExpect(status().isBadRequest());
    }

}

