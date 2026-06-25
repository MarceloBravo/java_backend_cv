package com.mabc.back_cv.presentacion;

import com.mabc.back_cv.web.dto.PresentacionDTO;
import com.mabc.back_cv.web.entities.Presentacion;
import com.mabc.back_cv.web.repositories.PresentacionRepository;
import com.mabc.back_cv.web.services.presentacion.PresentacionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PresentacionServiceImpl")
class PresentacionServiceImplTest {

    @Mock
    private PresentacionRepository presentacionRepository;

    @InjectMocks
    private PresentacionServiceImpl presentacionService;

    private Pageable pageable;

    @BeforeEach
    void Setup(){
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getPresentaciones_WithPageSize_ShouldReturnPresentacionDTOPage() {
        Presentacion presentacion = new Presentacion(1L, "Texto de presentación", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), pageable, 1);
        when(presentacionRepository.findAll(pageable)).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Texto de presentación", result.getContent().get(0).getParrafo());
        verify(presentacionRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getPresentaciones_WhenRepositoryThrows_ShouldPropagateException() {
        when(presentacionRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Error de acceso a datos"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> presentacionService.getPresentaciones(0, 10));

        assertEquals("Error de acceso a datos", exception.getMessage());
        verify(presentacionRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getPresentacionesByParrafo_WithValidParrafo_ShouldReturnPage() {
        Presentacion presentacion = new Presentacion(2L, "Parrafo especial", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);
        when(presentacionRepository.findByParrafoContainingIgnoreCase(eq("especial"), any(Pageable.class))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones("especial", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Parrafo especial", result.getContent().get(0).getParrafo());
        verify(presentacionRepository, times(1)).findByParrafoContainingIgnoreCase(eq("especial"), any(Pageable.class));
    }

    @Test
    void getPresentacionesByParrafo_WhenRepositoryThrows_ShouldPropagateException() {
        when(presentacionRepository.findByParrafoContainingIgnoreCase(any(), any(Pageable.class)))
                .thenThrow(new RuntimeException("Error de consulta por párrafo"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> presentacionService.getPresentaciones("especial", 0, 10));

        assertEquals("Error de consulta por párrafo", exception.getMessage());
        verify(presentacionRepository, times(1)).findByParrafoContainingIgnoreCase(any(), any(Pageable.class));
    }

    @Test
    void getPresentacionesByUserIdAndParrafo_WithValidArgs_ShouldReturnPage() {
        Presentacion presentacion = new Presentacion(3L, "Presentación por usuario", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);
        when(presentacionRepository.findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("usuario"), any(Pageable.class)))
            .thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(1L, "usuario", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Presentación por usuario", result.getContent().get(0).getParrafo());
        verify(presentacionRepository, times(1)).findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("usuario"), any(Pageable.class));
    }

    @Test
    void getPresentacionesByUserIdAndParrafo_WhenRepositoryThrows_ShouldPropagateException() {
        when(presentacionRepository.findByUserIdAndParrafoContainingIgnoreCase(anyLong(), any(), any(Pageable.class)))
            .thenThrow(new RuntimeException("Error en consulta por usuario y párrafo"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> presentacionService.getPresentaciones(1L, "usuario", 0, 10));

        assertEquals("Error en consulta por usuario y párrafo", exception.getMessage());
        verify(presentacionRepository, times(1)).findByUserIdAndParrafoContainingIgnoreCase(anyLong(), any(), any(Pageable.class));
    }

    @Test
    void getPresentacionByUserId_ExistingId_ShouldReturnPresentacionDTO() {
        Presentacion presentacion = new Presentacion(4L, "Presentación encontrada", null);
        when(presentacionRepository.findByUserId(1L)).thenReturn(presentacion);

        PresentacionDTO result = presentacionService.getPresentacionByUserId(1L);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Presentación encontrada", result.getParrafo());
        verify(presentacionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getPresentacionByUserId_InvalidId_ShouldReturnNull() {
        PresentacionDTO result = presentacionService.getPresentacionByUserId(-1L);

        assertNull(result);
        verify(presentacionRepository, never()).findByUserId(anyLong());
    }

    @Test
    void getPresentacionByUserId_NonExistingId_ShouldReturnNull() {
        when(presentacionRepository.findByUserId(99L)).thenReturn(null);

        PresentacionDTO result = presentacionService.getPresentacionByUserId(99L);

        assertNull(result);
        verify(presentacionRepository, times(1)).findByUserId(99L);
    }

    @Test
    void savePresentacion_ValidDto_ShouldReturnSavedDto() {
        PresentacionDTO input = new PresentacionDTO(null, "Nuevo párrafo", null);
        Presentacion savedEntity = new Presentacion(5L, "Nuevo párrafo", null);
        when(presentacionRepository.save(any(Presentacion.class))).thenReturn(savedEntity);

        PresentacionDTO result = presentacionService.savePresentacion(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Nuevo párrafo", result.getParrafo());
        verify(presentacionRepository, times(1)).save(any(Presentacion.class));
    }

    @Test
    void savePresentacion_NullDto_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> presentacionService.savePresentacion(null));

        assertEquals("La presentación no puede ser nula.", exception.getMessage());
        verify(presentacionRepository, never()).save(any(Presentacion.class));
    }

    @Test
    void deletePresentacion_ExistingUserId_ShouldDeletePresentacion() {
        Presentacion presentacion = new Presentacion(6L, "Para eliminar", null);
        when(presentacionRepository.findByUserId(1L)).thenReturn(presentacion);

        presentacionService.deletePresentacion(1L);

        verify(presentacionRepository, times(1)).findByUserId(1L);
        verify(presentacionRepository, times(1)).delete(presentacion);
    }

    @Test
    void deletePresentacion_NonExistingUserId_ShouldThrowIllegalArgumentException() {
        when(presentacionRepository.findByUserId(99L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> presentacionService.deletePresentacion(99L));

        assertEquals("No se encontró una presentación para el userId: 99", exception.getMessage());
        verify(presentacionRepository, times(1)).findByUserId(99L);
        verify(presentacionRepository, never()).delete(any(Presentacion.class));
    }

    @Test
    void deletePresentacion_NullUserId_ShouldThrowIllegalArgumentException() {
        when(presentacionRepository.findByUserId(null)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> presentacionService.deletePresentacion(null));

        assertEquals("No se encontró una presentación para el userId: null", exception.getMessage());
        verify(presentacionRepository, times(1)).findByUserId(null);
        verify(presentacionRepository, never()).delete(any(Presentacion.class));
    }

    @Test
    void getPresentaciones_NullPageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(10L, "Default page/size", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getPresentaciones_NegativePageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(11L, "Negative page/size", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(-5, -1);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getPresentacionesByParrafo_NullPageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(12L, "Parrafo default", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findByParrafoContainingIgnoreCase(eq("test"), any(Pageable.class))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones("test", null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findByParrafoContainingIgnoreCase(eq("test"), any(Pageable.class));
    }

    @Test
    void getPresentacionesByParrafo_NegativePageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(13L, "Parrafo negative", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findByParrafoContainingIgnoreCase(eq("neg"), any(Pageable.class))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones("neg", -2, -3);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findByParrafoContainingIgnoreCase(eq("neg"), any(Pageable.class));
    }

    @Test
    void getPresentacionesByUserIdAndParrafo_NullPageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(1L, "User parrafo default", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("u"), any(Pageable.class))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(1L, "u", null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("u"), any(Pageable.class));
    }

    @Test
    void getPresentacionesByUserIdAndParrafo_NegativePageSize_ShouldUseDefaults() {
        Presentacion presentacion = new Presentacion(15L, "User parrafo negative", null);
        Page<Presentacion> page = new PageImpl<>(List.of(presentacion), PageRequest.of(0, 10), 1);

        when(presentacionRepository.findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("u2"), any(Pageable.class))).thenReturn(page);

        Page<PresentacionDTO> result = presentacionService.getPresentaciones(1L, "u2", -4, -7);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(presentacionRepository, times(1)).findByUserIdAndParrafoContainingIgnoreCase(eq(1L), eq("u2"), any(Pageable.class));
    }

    @Test
    void getPresentacionByUserId_Null_ShouldReturnNullAndNotCallRepo() {
        PresentacionDTO result = presentacionService.getPresentacionByUserId(null);

        assertNull(result);
        verify(presentacionRepository, never()).findByUserId(any());
    }
}

