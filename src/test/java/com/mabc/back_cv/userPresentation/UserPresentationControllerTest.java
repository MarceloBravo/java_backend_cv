package com.mabc.back_cv.userPresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mabc.back_cv.web.controllers.UserPresentationController;
import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.springframework.data.domain.PageRequest;



@ExtendWith(MockitoExtension.class)
@DisplayName("UserPresentationController Tests")
public class UserPresentationControllerTest{

    private MockMvc mockMvc;

    @Mock
    private UserPresentationService userPresentationService;

    @InjectMocks
    private UserPresentationController userPresentationController;

    private ObjectMapper objectMapper;

    private UserPresentationDTO userPresentationDTO;
    private UsuarioDTO usuarioDTO;
    private Page<UserPresentationDTO> userPresentationPage;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        
        mockMvc = MockMvcBuilders.standaloneSetup(userPresentationController)
                .setMessageConverters(converter)
                .build();
        
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Perez");
        
        userPresentationDTO = new UserPresentationDTO();
        userPresentationDTO.setId(1L);
        userPresentationDTO.setPosicion(1);
        userPresentationDTO.setParrafo("Este es un párrafo de prueba");
        userPresentationDTO.setUser(null); // Usuario null para evitar problemas de serialización en tests de Page
        
        userPresentationPage = new PageImpl<>(Arrays.asList(userPresentationDTO));
    }

    // Tests para GET /userPresentation/all

    @Test
    void getAll_conParametrosValidos_retorna200() throws Exception {
        List<UserPresentationDTO> list = Arrays.asList(userPresentationDTO);
        Page<UserPresentationDTO> page = new PageImpl<>(list, PageRequest.of(0, 10), 1);

        // Configuramos el mock limpiamente para este escenario
        when(userPresentationService.getAll("párrafo", 1L, 0, 10))
            .thenReturn(page);

        mockMvc.perform(get("/userPresentation/all")
                        .param("searchText", "párrafo")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk());
                
        verify(userPresentationService, times(1)).getAll("párrafo", 1L, 0, 10);
    }

    @Test
    void getAll_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(userPresentationService.getAll(any(), any(), any(), any()))
       .thenThrow(new RuntimeException("Error simulado en la base de datos"));

    mockMvc.perform(get("/userPresentation/all")
                    .param("userId", "1")
                    .param("page", "0")
                    .param("size", "10"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void getAll_sinParametros_retorna500() throws Exception {
        when(userPresentationService.getAll(null, null, null, null))
        .thenThrow(new NullPointerException("NPE al desempaquetar parámetros"));

        mockMvc.perform(get("/userPresentation/all"))
            .andExpect(status().isInternalServerError()); // Ahora
    }

    // Tests para GET /userPresentation/{id}

    @Test
    void getById_conIdValido_retorna200() throws Exception {
        when(userPresentationService.findById(1L)).thenReturn(userPresentationDTO);

        mockMvc.perform(get("/userPresentation/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_conIdCero_retorna404() throws Exception {
        when(userPresentationService.findById(0L)).thenReturn(null);

        mockMvc.perform(get("/userPresentation/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdNegativo_retorna404() throws Exception {
        when(userPresentationService.findById(-1L)).thenReturn(null);

        mockMvc.perform(get("/userPresentation/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conIdNoExistente_retorna404() throws Exception {
        when(userPresentationService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/userPresentation/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_conServiceLanzaExcepcion_retorna500() throws Exception {
        when(userPresentationService.findById(1L)).thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(get("/userPresentation/1"))
                .andExpect(status().isInternalServerError());
    }

    // Tests para POST /userPresentation/save

    @Test
    void save_conDTOValido_retorna200() throws Exception {
        userPresentationDTO.setUser(usuarioDTO);
        when(userPresentationService.save(any(UserPresentationDTO.class))).thenReturn(userPresentationDTO);
        
        mockMvc.perform(post("/userPresentation/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPresentationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parrafo").value("Este es un párrafo de prueba"));
    }

    @Test
    void save_conServiceLanzaExcepcion_retorna500() throws Exception {
        userPresentationDTO.setUser(usuarioDTO);
        when(userPresentationService.save(any(UserPresentationDTO.class))).thenThrow(new RuntimeException("Error"));
        
        mockMvc.perform(post("/userPresentation/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPresentationDTO)))
                .andExpect(status().isInternalServerError());
    }

    // Tests para DELETE /userPresentation/delete/{id}

    @Test
    void delete_conIdValido_retorna200() throws Exception {
        doNothing().when(userPresentationService).delete(1L);

        mockMvc.perform(delete("/userPresentation/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"UserPresentation eliminada correctamente\""));
    }

    @Test
    void delete_conIdCero_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Error: El id no puede ser nulo."))
                .when(userPresentationService).delete(0L);

        mockMvc.perform(delete("/userPresentation/delete/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conIdNegativo_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Error: El id no puede ser nulo."))
                .when(userPresentationService).delete(-1L);

        mockMvc.perform(delete("/userPresentation/delete/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conIdNoExistente_retorna400() throws Exception {
        doThrow(new RuntimeException("Error: El registro no existe."))
                .when(userPresentationService).delete(999L);

        mockMvc.perform(delete("/userPresentation/delete/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conIdNull_retorna400() throws Exception {
        mockMvc.perform(delete("/userPresentation/delete/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_conServiceLanzaExcepcion_retorna400() throws Exception {
        doThrow(new RuntimeException("Error al eliminar"))
                .when(userPresentationService).delete(1L);

        mockMvc.perform(delete("/userPresentation/delete/1"))
                .andExpect(status().isBadRequest());
    }
}
