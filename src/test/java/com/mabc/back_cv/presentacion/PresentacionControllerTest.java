package com.mabc.back_cv.presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabc.back_cv.web.controllers.PresentacionController;
import com.mabc.back_cv.web.dto.PresentacionDTO;
import com.mabc.back_cv.web.services.presentacion.PresentacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PresentacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PresentacionService presentacionService;

    @InjectMocks
    private PresentacionController presentacionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(presentacionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_NoFilters_ShouldReturnPageOfPresentaciones() throws Exception {
        PresentacionDTO dto = new PresentacionDTO(1L, "Párrafo de prueba", null);
        Page<PresentacionDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(presentacionService.getPresentaciones(0L, 10L)).thenReturn(page);

        mockMvc.perform(get("/api/presentacion/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].parrafo").value("Párrafo de prueba"));

        verify(presentacionService, times(1)).getPresentaciones(0L, 10L);
    }

    @Test
    void getAll_WithParrafoFilter_ShouldReturnPageOfPresentaciones() throws Exception {
        PresentacionDTO dto = new PresentacionDTO(2L, "Filtro texto", null);
        Page<PresentacionDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(presentacionService.getPresentaciones(eq("filtro"), anyLong(), anyLong())).thenReturn(page);

        mockMvc.perform(get("/api/presentacion/all")
                        .param("parrafo", "filtro")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].parrafo").value("Filtro texto"));

        verify(presentacionService, times(1)).getPresentaciones(eq("filtro"), anyLong(), anyLong());
    }

    @Test
    void getAll_WithParrafoAndUserId_ShouldReturnPageOfPresentaciones() throws Exception {
        PresentacionDTO dto = new PresentacionDTO(3L, "Usuario y filtro", null);
        Page<PresentacionDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(presentacionService.getPresentaciones(eq(1L), eq("usuario"), anyLong(), anyLong())).thenReturn(page);

        mockMvc.perform(get("/api/presentacion/all")
                        .param("parrafo", "usuario")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(3))
                .andExpect(jsonPath("$.content[0].parrafo").value("Usuario y filtro"));

        verify(presentacionService, times(1)).getPresentaciones(eq(1L), eq("usuario"), anyLong(), anyLong());
    }

    @Test
    void getAll_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(presentacionService.getPresentaciones(anyLong(), anyLong())).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(get("/api/presentacion/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());

        verify(presentacionService, times(1)).getPresentaciones(anyLong(), anyLong());
    }

    @Test
    void getByUserId_ExistingId_ShouldReturnPresentacion() throws Exception {
        PresentacionDTO dto = new PresentacionDTO(4L, "Presentación encontrada", null);
        when(presentacionService.getPresentacionByUserId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/presentacion/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.parrafo").value("Presentación encontrada"));

        verify(presentacionService, times(1)).getPresentacionByUserId(1L);
    }

    @Test
    void getByUserId_NonExistingId_ShouldReturnOkWithEmptyBody() throws Exception {
        when(presentacionService.getPresentacionByUserId(99L)).thenReturn(null);

        mockMvc.perform(get("/api/presentacion/user/{userId}", 99L))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(presentacionService, times(1)).getPresentacionByUserId(99L);
    }

    @Test
    void getByUserId_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        when(presentacionService.getPresentacionByUserId(1L)).thenThrow(new RuntimeException("Error de búsqueda"));

        mockMvc.perform(get("/api/presentacion/user/{userId}", 1L))
                .andExpect(status().isBadRequest());

        verify(presentacionService, times(1)).getPresentacionByUserId(1L);
    }

    @Test
    void save_ShouldReturnSavedPresentacion() throws Exception {
        PresentacionDTO request = new PresentacionDTO(null, "Guardar texto", null);
        PresentacionDTO response = new PresentacionDTO(5L, "Guardar texto", null);

        when(presentacionService.savePresentacion(any(PresentacionDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/presentacion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.parrafo").value("Guardar texto"));

        verify(presentacionService, times(1)).savePresentacion(any(PresentacionDTO.class));
    }

    @Test
    void save_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        PresentacionDTO request = new PresentacionDTO(null, "Guardar texto", null);
        when(presentacionService.savePresentacion(any(PresentacionDTO.class))).thenThrow(new RuntimeException("Error al guardar"));

        mockMvc.perform(post("/api/presentacion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(presentacionService, times(1)).savePresentacion(any(PresentacionDTO.class));
    }

    @Test
    void delete_ShouldReturnOk() throws Exception {
        doNothing().when(presentacionService).deletePresentacion(1L);

        mockMvc.perform(delete("/api/presentacion/delete/{id}", 1L))
                .andExpect(status().isOk());

        verify(presentacionService, times(1)).deletePresentacion(1L);
    }

    @Test
    void delete_NonExistingId_ShouldReturnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("No se encontró una presentación para el userId: 99"))
                .when(presentacionService).deletePresentacion(99L);

        mockMvc.perform(delete("/api/presentacion/delete/{id}", 99L))
                .andExpect(status().isBadRequest());

        verify(presentacionService, times(1)).deletePresentacion(99L);
    }

    @Test
    void delete_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("Error al eliminar"))
                .when(presentacionService).deletePresentacion(1L);

        mockMvc.perform(delete("/api/presentacion/delete/{id}", 1L))
                .andExpect(status().isBadRequest());

        verify(presentacionService, times(1)).deletePresentacion(1L);
    }
}