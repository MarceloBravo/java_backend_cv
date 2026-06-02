package com.mabc.back_cv.permisoPantalla;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabc.back_cv.web.controllers.PermisoPantallaController;
import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PermisoPantallaControllerTest {

    private PermisoPantallaService permisoPantallaService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        permisoPantallaService = Mockito.mock(PermisoPantallaService.class);
        PermisoPantallaController controller = new PermisoPantallaController(permisoPantallaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    private PermisoPantallaDTO buildDto(Long id) {
        Rol rol = new Rol(); rol.setId(1L); rol.setNombre("ADMIN");
        Pantalla p = new Pantalla(); p.setId(2L); p.setNombre_pantalla("PANT");
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(id);
        dto.setRol(rol);
        dto.setPantalla(p);
        dto.setAccion_consultar(true);
        dto.setAccion_crear(false);
        dto.setAccion_editar(false);
        dto.setAccion_eliminar(false);
        dto.setActivo(true);
        return dto;
    }

    @Test
    public void obtenerPermisosPantallaPorRol_Success() throws Exception {
        PermisoPantallaDTO dto = buildDto(1L);
        when(permisoPantallaService.obtenerPermisosPantallaPorRol(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/permisos-pantalla/rol/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void obtenerPermisosPantallaPorRol_ServiceException() throws Exception {
        when(permisoPantallaService.obtenerPermisosPantallaPorRol(1L)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/permisos-pantalla/rol/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("obtenerPermisoPantallaPorRolYPantalla - éxito y no encontrado")
    public void obtenerPermisoPantallaPorRolYPantalla_SuccessAndNotFound() throws Exception {
        PermisoPantallaDTO dto = buildDto(1L);
        when(permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(1L, 2L, true)).thenReturn(dto);

        mockMvc.perform(get("/permisos-pantalla/rol/1/pantalla/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        when(permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(1L, 3L, true)).thenReturn(null);

        mockMvc.perform(get("/permisos-pantalla/rol/1/pantalla/3"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("obtenerPermisoPantallaPorRolYPantalla - Error 500")
    public void obtenerPermisoPantallaPorRolYPantalla_Error() throws Exception {
        when(permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(1L, 2L, true)).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(get("/permisos-pantalla/rol/1/pantalla/2"))
                .andExpect(status().isInternalServerError());
    }
    

    @Test
    public void grabarPermisosPantallaPorRol_Success() throws Exception {
        PermisoPantallaDTO dto = buildDto(null);
        when(permisoPantallaService.grabarPermisosPantallaPorRol(any())).thenReturn(1);

        String json = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(post("/permisos-pantalla/grabar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void grabarPermisosPantallaPorRol_InvalidJson() throws Exception {
        // malformed JSON
        String badJson = "[{\"id\":1,\"rol\": {}}"; // missing closing ]

        mockMvc.perform(post("/permisos-pantalla/grabar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void grabarPermisosPantallaPorRol_MissingProperties_ForwardsToService() throws Exception {
        // DTO with missing rol/pantalla (nulls)
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(null);
        dto.setRol(null);
        dto.setPantalla(null);
        dto.setAccion_consultar(true);

        when(permisoPantallaService.grabarPermisosPantallaPorRol(any())).thenReturn(0);

        String json = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(post("/permisos-pantalla/grabar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        verify(permisoPantallaService).grabarPermisosPantallaPorRol(any());
    }

    @Test
    @DisplayName("Grabar permisos de una pantalla por Rol - con errores")
    public void grabarPermisosPantallaPorRol_WithErrors() throws Exception {
        PermisoPantallaDTO dto = buildDto(null);
        dto.setRol(null);
        dto.setPantalla(null);
        when(permisoPantallaService.grabarPermisosPantallaPorRol(any())).thenReturn(0);

        String json = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(post("/permisos-pantalla/grabar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        verify(permisoPantallaService).grabarPermisosPantallaPorRol(any());
    }

    @Test
    @DisplayName("Grabar permisos de una pantalla por Rol - error del servicio")
    public void grabarPermisosPantallaPorRol_ServiceError() throws Exception {
        PermisoPantallaDTO dto = buildDto(1L);
        when(permisoPantallaService.grabarPermisosPantallaPorRol(any())).thenThrow(new RuntimeException("Error en el servicio"));

        String json = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(post("/permisos-pantalla/grabar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());

        verify(permisoPantallaService).grabarPermisosPantallaPorRol(any());
    }

    @Test
    @DisplayName("Eliminar permisos de una pantalla por Rol - exitoso")
    public void eliminarPermisosPantallaPorRolId_Success() throws Exception {
        Long id_rol = 1L;
        when(permisoPantallaService.eliminarPermisosPantallaPorRolId(id_rol)).thenReturn(true);

        mockMvc.perform(delete("/permisos-pantalla/eliminar/rol/{id_rol}", id_rol))
                .andExpect(status().isOk())
                .andExpect(content().string("PermisosPantalla eliminados correctamente por rol"));
    }

    @Test
    @DisplayName("Eliminar permisos de una pantalla por Rol - no encontrado")
    public void eliminarPermisosPantallaPorRolId_NotFound() throws Exception {
        Long id_rol = 1L;
        when(permisoPantallaService.eliminarPermisosPantallaPorRolId(id_rol)).thenReturn(false);

        mockMvc.perform(delete("/permisos-pantalla/eliminar/rol/{id_rol}", id_rol))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Eliminar permisos de una pantalla por Rol - error del servicio")
    public void eliminarPermisosPantallaPorRolId_Error() throws Exception {
        Long id_rol = 1L;
        when(permisoPantallaService.eliminarPermisosPantallaPorRolId(id_rol)).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(delete("/permisos-pantalla/eliminar/rol/{id_rol}", id_rol))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Desactivar permisos de una pantalla por Rol - exitoso")
    public void desactivarPermisosPantallaPorRolId_Success() throws Exception {
        Long id = 1L;
        when(permisoPantallaService.desactivarPermisosPantallaPorRolId(id)).thenReturn(true);

        mockMvc.perform(put("/permisos-pantalla/desactivar/rol/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("PermisosPantalla desactivados correctamente por rol"));
    }

    @Test
    @DisplayName("Desactivar permisos de una pantalla por Rol - no encontrado")
    public void desactivarPermisosPantallaPorRolId_NotFound() throws Exception {
        Long id = 1L;
        when(permisoPantallaService.desactivarPermisosPantallaPorRolId(id)).thenReturn(false);

        mockMvc.perform(put("/permisos-pantalla/desactivar/rol/{id}", id))
                .andExpect(status().isNotFound());
    }   

    @Test
    @DisplayName("Desactivar permisos de una pantalla por Rol - error del servicio")
    public void desactivarPermisosPantallaPorRolId_Error() throws Exception {
        Long id = 1L;
        when(permisoPantallaService.desactivarPermisosPantallaPorRolId(id)).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(put("/permisos-pantalla/desactivar/rol/{id}", id))
                .andExpect(status().isInternalServerError());
    }

}
