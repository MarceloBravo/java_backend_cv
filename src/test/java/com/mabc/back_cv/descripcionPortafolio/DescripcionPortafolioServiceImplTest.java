package com.mabc.back_cv.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.entities.DescripcionPortafolio;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.repositories.DescripcionPortafolioRepository;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioServiceImpl;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescripcionPortafolioServiceImplTest {

    @Mock
    private DescripcionPortafolioRepository descripcionPortafolioRepository;

    @InjectMocks
    private DescripcionPortafolioServiceImpl descripcionPortafolioService;

    private Portafolio portafolio;

    @BeforeEach
    void setUp() {
        portafolio = new Portafolio();
        portafolio.setId(1L);
        portafolio.setTitle("Portfolio prueba");
    }

    @Test
    void shouldReturnAllDescriptionsWhenSearchTermIsNull() {
        DescripcionPortafolio entity = createEntity(1L, "texto prueba", 1);
        Page<DescripcionPortafolio> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(descripcionPortafolioRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = descripcionPortafolioService.getAll(null, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(entity.getParrafo(), result.getContent().get(0).getParrafo());
    }

    @Test
    void shouldReturnAllDescriptionsWhenSearchTermIsEmpty() {
        DescripcionPortafolio entity = createEntity(10L, "texto vacio", 1);
        Page<DescripcionPortafolio> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(descripcionPortafolioRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = descripcionPortafolioService.getAll("", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(entity.getParrafo(), result.getContent().get(0).getParrafo());
    }

    @Test
    void shouldReturnAllDescriptionsAsList() {
        DescripcionPortafolio entity = createEntity(3L, "texto lista", 1);
        when(descripcionPortafolioRepository.findAll()).thenReturn(List.of(entity));

        List<DescripcionPortafolioDTO> result = descripcionPortafolioService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getParrafo(), result.get(0).getParrafo());
    }

    @Test
    void shouldReturnDtoWhenGetByIdExists() {
        DescripcionPortafolio entity = createEntity(4L, "texto existente", 4);
        when(descripcionPortafolioRepository.findById(4L)).thenReturn(Optional.of(entity));

        DescripcionPortafolioDTO result = descripcionPortafolioService.getById(4L);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getParrafo(), result.getParrafo());
    }

    @Test
    void shouldReturnNullForInvalidIdValues() {
        assertNull(descripcionPortafolioService.getById(null));
        assertNull(descripcionPortafolioService.getById(-2L));
    }

    @Test
    void shouldReturnPagedResultsWhenSearchTermIsProvided() {
        DescripcionPortafolio entity = createEntity(2L, "texto buscado", 2);
        Page<DescripcionPortafolio> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 5), 1);
        when(descripcionPortafolioRepository.findByParrafoContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<DescripcionPortafolioDTO> result = descripcionPortafolioService.getAll("buscado", 0, 5);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(entity.getParrafo(), result.getContent().get(0).getParrafo());
    }

    @Test
    void shouldReturnNullWhenGetByIdDoesNotExist() {
        when(descripcionPortafolioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertNull(descripcionPortafolioService.getById(99L));
    }

    @Test
    void shouldSaveValidDtoSuccessfully() {
        DescripcionPortafolioDTO dto = createDto(3L, "texto guardar", 3);
        DescripcionPortafolio savedEntity = createEntity(3L, dto.getParrafo(), dto.getPosicion());
        when(descripcionPortafolioRepository.save(any(DescripcionPortafolio.class))).thenReturn(savedEntity);

        DescripcionPortafolioDTO result = descripcionPortafolioService.save(dto);

        assertNotNull(result);
        assertEquals(dto.getParrafo(), result.getParrafo());
        assertEquals(dto.getPosicion(), result.getPosicion());
    }

    @Test
    void shouldThrowWhenSaveNullDto() {
        assertThrows(IllegalArgumentException.class, () -> descripcionPortafolioService.save(null));
    }

    @Test
    void shouldThrowWhenSaveReturnsNullEntity() {
        DescripcionPortafolioDTO dto = createDto(5L, "texto entity null", 5);
        try (MockedStatic<DescripcionPortafolioUtils> utilities = mockStatic(DescripcionPortafolioUtils.class)) {
            utilities.when(() -> DescripcionPortafolioUtils.DTOToEntity(dto)).thenReturn(null);
            assertThrows(IllegalArgumentException.class, () -> descripcionPortafolioService.save(dto));
        }
    }

    @Test
    void shouldDeleteWhenIdExists() {
        when(descripcionPortafolioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(descripcionPortafolioRepository).deleteById(1L);

        assertDoesNotThrow(() -> descripcionPortafolioService.delete(1L));
        verify(descripcionPortafolioRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteNonexistentId() {
        when(descripcionPortafolioRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> descripcionPortafolioService.delete(1L));
    }

    @Test
    void shouldThrowWhenDeleteInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> descripcionPortafolioService.delete(-1L));
        assertThrows(IllegalArgumentException.class, () -> descripcionPortafolioService.delete(null));
    }

    private DescripcionPortafolio createEntity(Long id, String parrafo, Integer posicion) {
        DescripcionPortafolio entity = new DescripcionPortafolio();
        entity.setId(id);
        entity.setParrafo(parrafo);
        entity.setPosicion(posicion);
        entity.setPortafolio(portafolio);
        return entity;
    }

    private DescripcionPortafolioDTO createDto(Long id, String parrafo, Integer posicion) {
        return new DescripcionPortafolioDTO(id, parrafo, posicion, portafolio);
    }
}
