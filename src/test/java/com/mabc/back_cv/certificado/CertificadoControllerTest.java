package com.mabc.back_cv.certificado;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.certificado.CertificadoService;
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

import java.util.List;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mabc.back_cv.web.controllers.CertificadoController;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para CertificadoController")
class CertificadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CertificadoService certificadoService;

    @InjectMocks
    private CertificadoController certificadoController;

    private ObjectMapper objectMapper;

    private CertificadoDTO certificadoDTO;
    private User usuario;
    private Rol rol;
    private Page<CertificadoDTO> certificadoPage;

    @BeforeEach
    void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(certificadoController)
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

        certificadoDTO = new CertificadoDTO();
        certificadoDTO.setId(1L);
        certificadoDTO.setName("Certificado Java");
        certificadoDTO.setImage("certificado.png");
        certificadoDTO.setUrl("https://certificados.com/java");
        certificadoDTO.setMouse_move_title("Certificado");
        certificadoDTO.setMouse_move_description("Certificado de Java Avanzado");
        certificadoDTO.setUser(null);

        List<CertificadoDTO> contenido = List.of(certificadoDTO);
        certificadoPage = new PageImpl<>(contenido, PageRequest.of(0, 10), contenido.size());
    }

    @Test
    void getAll_conParametrosValidos_retorna200() throws Exception {
        when(certificadoService.findBySearchText(1L, "Java", 0, 10))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_sinParametros_retorna200() throws Exception {
        when(certificadoService.findBySearchText(null, null, null, null))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conUserIdNull_retorna200() throws Exception {
        when(certificadoService.findBySearchText(null, "test", 0, 10))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("searchText", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSearchTextNull_retorna200() throws Exception {
        when(certificadoService.findBySearchText(1L, null, 0, 10))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conPageNull_retorna200() throws Exception {
        when(certificadoService.findBySearchText(1L, "Java", null, 10))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeNull_retorna200() throws Exception {
        when(certificadoService.findBySearchText(1L, "Java", 0, null))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("searchText", "Java")
                .param("page", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conPageNegativo_retorna200() throws Exception {
        when(certificadoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("page", "-1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeCero_retorna200() throws Exception {
        when(certificadoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("size", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conSizeNegativo_retorna200() throws Exception {
        when(certificadoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1")
                .param("size", "-5"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(certificadoService.findBySearchText(anyLong(), any(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/certificado/all")
                .param("userId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getById_conIdValido_retorna200() throws Exception {
        when(certificadoService.findById(1L)).thenReturn(certificadoDTO);

        mockMvc.perform(get("/certificado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Certificado Java"))
                .andExpect(jsonPath("$.url").value("https://certificados.com/java"));
    }

    @Test
    void getById_conIdNoExistente_retorna404() throws Exception {
        when(certificadoService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/certificado/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdCero_retorna404() throws Exception {
        when(certificadoService.findById(0L)).thenReturn(null);

        mockMvc.perform(get("/certificado/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdNegativo_retorna404() throws Exception {
        when(certificadoService.findById(-1L)).thenReturn(null);

        mockMvc.perform(get("/certificado/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(certificadoService.findById(1L)).thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/certificado/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllByUserId_conUserIdValido_retorna200() throws Exception {
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conPageNull_retorna200() throws Exception {
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/user/1")
                .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conSizeNull_retorna200() throws Exception {
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/user/1")
                .param("page", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId_conPaginacionValida_retorna200() throws Exception {
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(certificadoPage);

        mockMvc.perform(get("/certificado/user/1")
                .param("page", "2")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    void getAllByUserId_conPaginaVacia_retorna200() throws Exception {
        Page<CertificadoDTO> paginaVacia = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenReturn(paginaVacia);

        mockMvc.perform(get("/certificado/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getAllByUserId_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(certificadoService.findByUserId(anyLong(), any(), any()))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/certificado/user/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conDTOValido_retorna200() throws Exception {
        when(certificadoService.save(any(CertificadoDTO.class))).thenReturn(certificadoDTO);

        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Certificado Java"))
                .andExpect(jsonPath("$.url").value("https://certificados.com/java"));
    }

    @Test
    void save_conDTOConIdNull_retorna200() throws Exception {
        certificadoDTO.setId(null);
        CertificadoDTO certificadoGuardado = new CertificadoDTO();
        certificadoGuardado.setId(2L);
        certificadoGuardado.setName("Certificado Java");
        certificadoGuardado.setImage("certificado.png");
        certificadoGuardado.setUrl("https://certificados.com/java");
        certificadoGuardado.setMouse_move_title("Certificado");
        certificadoGuardado.setMouse_move_description("Certificado de Java Avanzado");
        certificadoGuardado.setUser(UsuarioMapper.userToDTO(usuario));

        when(certificadoService.save(any(CertificadoDTO.class))).thenReturn(certificadoGuardado);

        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void save_conDTOSinCamposObligatorios_retorna500() throws Exception {
        CertificadoDTO dtoIncompleto = new CertificadoDTO();
        dtoIncompleto.setName(null);
        dtoIncompleto.setUser(UsuarioMapper.userToDTO(usuario));

        when(certificadoService.save(any(CertificadoDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIncompleto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conRequestBodyNull_retorna500() throws Exception {
        when(certificadoService.save(any(CertificadoDTO.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos."));

        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(certificadoService.save(any(CertificadoDTO.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_conJSONInvalido_retorna400() throws Exception {
        mockMvc.perform(post("/certificado/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conIdValido_retorna200() throws Exception {
        doNothing().when(certificadoService).delete(1L);

        mockMvc.perform(delete("/certificado/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Certificado eliminado correctamente\""));
    }

    @Test
    void delete_conIdNoExistente_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(certificadoService).delete(999L);

        mockMvc.perform(delete("/certificado/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el certificado\""));
    }

    @Test
    void delete_conIdCero_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(certificadoService).delete(0L);

        mockMvc.perform(delete("/certificado/delete/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el certificado\""));
    }

    @Test
    void delete_conIdNegativo_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Registro no encontrado o inexistente."))
                .when(certificadoService).delete(-1L);

        mockMvc.perform(delete("/certificado/delete/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el certificado\""));
    }

    @Test
    void delete_conServiceLanzaExcepcion_retorna400() throws Exception {
        doThrow(new RuntimeException("Error de base de datos"))
                .when(certificadoService).delete(1L);

        mockMvc.perform(delete("/certificado/delete/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Error al eliminar el certificado\""));
    }

    @Test
    void delete_conIdNull_retorna400() throws Exception {
        mockMvc.perform(delete("/certificado/delete/null"))
                .andExpect(status().isBadRequest());
    }

}
