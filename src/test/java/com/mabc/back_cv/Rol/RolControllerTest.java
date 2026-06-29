package com.mabc.back_cv.Rol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabc.back_cv.web.controllers.RolController;
import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.services.Rol.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para el controlador de roles {@link RolController}.
 * Utiliza MockMvc en modo standalone para evitar levantar el contexto de
 * seguridad y centrarse exclusivamente en la verificación de endpoints
 * y sus respuestas HTTP.
 */
@ExtendWith(MockitoExtension.class)
class RolControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RolService rolService;

    @InjectMocks
    private RolController rolController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rolController).build();
        objectMapper = new ObjectMapper();
    }

    // -------------------------------------------------------------------------
    // GET /roles/list
    // -------------------------------------------------------------------------

    @Test
    void getAllRoles_ShouldReturnListOfRoles() throws Exception {
        RolDTO rol1 = new RolDTO(1L, "ADMIN", true);
        RolDTO rol2 = new RolDTO(2L, "USER", true);
        List<RolDTO> roles = Arrays.asList(rol1, rol2);

        when(rolService.getAll()).thenReturn(roles);

        mockMvc.perform(get("/roles/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("ADMIN")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("USER")));

        verify(rolService, times(1)).getAll();
    }

    @Test
    void getAllRoles_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(rolService.getAll()).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/roles/list"))
                .andExpect(status().isBadRequest());

        verify(rolService, times(1)).getAll();
    }

    // -------------------------------------------------------------------------
    // GET /roles/{id}
    // -------------------------------------------------------------------------

    @Test
    void getRolById_ExistingId_ShouldReturnRol() throws Exception {
        RolDTO rol = new RolDTO(1L, "ADMIN", true);
        when(rolService.findById(1L)).thenReturn(rol);

        mockMvc.perform(get("/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("ADMIN")))
                .andExpect(jsonPath("$.activo", is(true)));

        verify(rolService, times(1)).findById(1L);
    }

    @Test
    void getRolById_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(rolService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/roles/{id}", 99L))
                .andExpect(status().isBadRequest());

        verify(rolService, times(1)).findById(99L);
    }

    // -------------------------------------------------------------------------
    // GET /roles/all
    // -------------------------------------------------------------------------

    @Test
    void getRolesByPage_ShouldReturnPageOfRoles() throws Exception {
        RolDTO rol = new RolDTO(1L, "ADMIN", true);
        Page<RolDTO> page = new PageImpl<>(List.of(rol), PageRequest.of(0, 10), 1);

        when(rolService.searchBy("", true, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/roles/all")
                .param("nombre", "")
                .param("active", "true")
                .param("page", "0")
                .param("rows", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nombre", is("ADMIN")))
                .andExpect(jsonPath("$.totalElements", is(1)));

        verify(rolService, times(1)).searchBy("", true, 0, 10);
    }

    @Test
    void getRolesByPage_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(rolService.searchBy(any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/roles/all"))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // GET /roles/active
    // -------------------------------------------------------------------------

    @Test
    void getActiveRoles_ShouldReturnActiveRoles() throws Exception {
        RolDTO rol = new RolDTO(1L, "USER", true);
        when(rolService.getActiveRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/roles/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("USER")))
                .andExpect(jsonPath("$[0].activo", is(true)));

        verify(rolService, times(1)).getActiveRoles();
    }

    @Test
    void getActiveRoles_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(rolService.getActiveRoles()).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/roles/active"))
                .andExpect(status().isBadRequest());

        verify(rolService, times(1)).getActiveRoles();
    }

    // -------------------------------------------------------------------------
    // POST /roles/save
    // -------------------------------------------------------------------------

    @Test
    void saveRol_Success_ShouldReturnSavedRol() throws Exception {
        RolDTO toSave = new RolDTO(null, "EDITOR", true);
        RolDTO saved = new RolDTO(3L, "EDITOR", true);

        when(rolService.save(any(RolDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/roles/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("EDITOR")));

        verify(rolService, times(1)).save(any(RolDTO.class));
    }

    @Test
    void saveRol_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        RolDTO toSave = new RolDTO(null, "", true);

        when(rolService.save(any(RolDTO.class))).thenThrow(new RuntimeException("Datos inválidos"));

        mockMvc.perform(post("/roles/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(rolService, times(1)).save(any(RolDTO.class));
    }

    // -------------------------------------------------------------------------
    // DELETE /roles/{id}
    // -------------------------------------------------------------------------

    @Test
    void deleteById_Success_ShouldReturnOkMessage() throws Exception {
        doNothing().when(rolService).delete(1L);

        mockMvc.perform(delete("/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Rol eliminado correctamente"));

        verify(rolService, times(1)).delete(1L);
    }

    @Test
    void deleteById_ServiceThrowsException_ShouldReturnBadRequestMessage() throws Exception {
        doThrow(new RuntimeException("Rol no encontrado")).when(rolService).delete(99L);

        mockMvc.perform(delete("/roles/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: El rol no pudo ser eliminado: Rol no encontrado"));

        verify(rolService, times(1)).delete(99L);
    }
}
