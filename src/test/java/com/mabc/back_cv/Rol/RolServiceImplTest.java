package com.mabc.back_cv.Rol;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.services.Rol.RolServiceImpl;
import com.mabc.back_cv.web.services.Rol.RolUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la implementación del servicio {@link RolServiceImpl}.
 * Valida la lógica de negocio del servicio, incluyendo búsqueda por id,
 * paginación, guardado y eliminación de roles, delegando la persistencia
 * al {@link RolRepository} y la conversión al {@link RolUtils} mockeados.
 */
@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private RolUtils rolUtils;

    @InjectMocks
    private RolServiceImpl rolService;

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    void findById_ExistingId_ShouldReturnRolDTO() {
        Rol rol = new Rol(1L, "ADMIN", true, null);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        RolDTO result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getNombre());
        assertTrue(result.getActivo());
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistingId_ShouldReturnNull() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        RolDTO result = rolService.findById(99L);

        assertNull(result);
        verify(rolRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------------------------
    // getAll (list)
    // -------------------------------------------------------------------------

    @Test
    void getAll_ShouldReturnListOfRolDTOs() {
        Rol rol1 = new Rol(1L, "ADMIN", true, null);
        Rol rol2 = new Rol(2L, "USER", true, null);
        when(rolRepository.findAll()).thenReturn(Arrays.asList(rol1, rol2));

        List<RolDTO> result = rolService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombre());
        assertEquals("USER", result.get(1).getNombre());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    void getAll_EmptyRepository_ShouldReturnEmptyList() {
        when(rolRepository.findAll()).thenReturn(Collections.emptyList());

        List<RolDTO> result = rolService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rolRepository, times(1)).findAll();
    }

    // -------------------------------------------------------------------------
    // getAll (paginated)
    // -------------------------------------------------------------------------

    @Test
    void getAllPaged_ShouldReturnPageOfRolDTOs() {
        Rol rol = new Rol(1L, "ADMIN", true, null);
        RolDTO rolDTO = new RolDTO(1L, "ADMIN", true);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rol> rolPage = new PageImpl<>(List.of(rol), pageable, 1);

        when(rolUtils.createPageable(0, 10)).thenReturn(pageable);
        when(rolRepository.findAll(pageable)).thenReturn(rolPage);
        when(rolUtils.mapToRolDTO(rol)).thenReturn(rolDTO);

        Page<RolDTO> result = rolService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("ADMIN", result.getContent().get(0).getNombre());
        verify(rolUtils, times(1)).createPageable(0, 10);
        verify(rolRepository, times(1)).findAll(pageable);
    }

    // -------------------------------------------------------------------------
    // getActiveRoles
    // -------------------------------------------------------------------------

    @Test
    void getActiveRoles_ShouldReturnOnlyActiveRoles() {
        Rol rol = new Rol(1L, "USER", true, null);
        RolDTO rolDTO = new RolDTO(1L, "USER", true);
        when(rolRepository.findByActiveState()).thenReturn(List.of(rol));
        when(rolUtils.mapToRolDTO(rol)).thenReturn(rolDTO);

        List<RolDTO> result = rolService.getActiveRoles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActivo());
        verify(rolRepository, times(1)).findByActiveState();
    }

    // -------------------------------------------------------------------------
    // searchBy
    // -------------------------------------------------------------------------

    @Test
    void searchBy_EmptyNombre_ShouldUsSearchByNombreAndEstado() {
        Rol rol = new Rol(1L, "ADMIN", true, null);
        RolDTO rolDTO = new RolDTO(1L, "ADMIN", true);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rol> rolPage = new PageImpl<>(List.of(rol), pageable, 1);

        when(rolUtils.createPageable(0, 10)).thenReturn(pageable);
        when(rolRepository.searchByNombreAndEstado("", true, pageable)).thenReturn(rolPage);
        when(rolUtils.mapToRolDTO(rol)).thenReturn(rolDTO);

        Page<RolDTO> result = rolService.searchBy("", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(rolRepository, times(1)).searchByNombreAndEstado("", true, pageable);
        verify(rolRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void searchBy_WithNombre_ShouldUseFindAll() {
        Rol rol = new Rol(1L, "ADMIN", true, null);
        RolDTO rolDTO = new RolDTO(1L, "ADMIN", true);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rol> rolPage = new PageImpl<>(List.of(rol), pageable, 1);

        when(rolUtils.createPageable(0, 10)).thenReturn(pageable);
        when(rolRepository.findAll(pageable)).thenReturn(rolPage);
        when(rolUtils.mapToRolDTO(rol)).thenReturn(rolDTO);

        Page<RolDTO> result = rolService.searchBy("ADMIN", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(rolRepository, times(1)).findAll(pageable);
        verify(rolRepository, never()).searchByNombreAndEstado(any(), any(), any());
    }

    // -------------------------------------------------------------------------
    // save
    // -------------------------------------------------------------------------

    @Test
    void save_ValidDTO_ShouldReturnSavedRolDTO() {
        RolDTO inputDTO = new RolDTO(null, "EDITOR", true);
        Rol rolToSave = new Rol(null, "EDITOR", true, null);
        Rol savedRol = new Rol(3L, "EDITOR", true, null);
        RolDTO savedDTO = new RolDTO(3L, "EDITOR", true);

        when(rolUtils.mapToRol(inputDTO)).thenReturn(rolToSave);
        when(rolRepository.save(rolToSave)).thenReturn(savedRol);
        when(rolUtils.mapToRolDTO(savedRol)).thenReturn(savedDTO);

        RolDTO result = rolService.save(inputDTO);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("EDITOR", result.getNombre());
        verify(rolUtils, times(1)).mapToRol(inputDTO);
        verify(rolRepository, times(1)).save(rolToSave);
        verify(rolUtils, times(1)).mapToRolDTO(savedRol);
    }

    @Test
    void save_InvalidDTO_ShouldReturnNull() {
        RolDTO inputDTO = new RolDTO(null, "", true);
        when(rolUtils.mapToRol(inputDTO)).thenReturn(null);

        RolDTO result = rolService.save(inputDTO);

        assertNull(result);
        verify(rolUtils, times(1)).mapToRol(inputDTO);
        verify(rolRepository, never()).save(any(Rol.class));
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    void delete_NullId_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rolService.delete(null));

        assertEquals("Error: El id no puede ser nulo.", exception.getMessage());
        verify(rolRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ValidId_ShouldCallRepository() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(new Rol(1L, "ADMIN", true, null)));

        rolService.delete(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }
}
