package com.macb.back_cv.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.repositories.TrabajoRepository;
import com.mabc.back_cv.web.services.trabajo.TrabajoServiceImpl;
import com.mabc.back_cv.web.services.trabajo.TrabajoUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;

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

import org.junit.jupiter.api.BeforeEach;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;

import com.mabc.back_cv.web.entities.Trabajo;
import com.mabc.back_cv.web.entities.User;

import com.mabc.back_cv.common.Utils;

@ExtendWith(MockitoExtension.class)
public class TrabajoServiceImplTest{

    @Mock
    private TrabajoRepository repository;

    @Mock
    private Utils utils;

    @InjectMocks
    private TrabajoServiceImpl service;

    private TrabajoDTO trabajoDTO1;
    private TrabajoDTO trabajoDTO2;
    private Trabajo trabajo1;
    private Trabajo trabajo2;
    private User user1;
    private User user2;
    private UsuarioDTO userDTO1;
    private UsuarioDTO userDTO2;

    private Pageable pageable;


    @BeforeEach
    void setUp(){
        reset(repository);
        user1 = new User();
        user1.setId(1L);
        user1.setNombre("Juan");
        user1.setApellido("Pérez");
        user1.setEmail("juan@example.com");
        user1.setPassword("pass123");
        user1.setActivo(true);

        user2 = new User();
        user2.setId(2L);
        user2.setNombre("Pedro");
        user2.setApellido("Pérez");
        user2.setEmail("pedro@example.com");
        user2.setPassword("pass456");
        user2.setActivo(true);
        
        userDTO1 = new UsuarioDTO();
        userDTO1.setId(1L);
        userDTO1.setNombre("Juan");
        userDTO1.setApellido("Pérez");
        userDTO1.setEmail("juan@example.com");
        userDTO1.setPassword("pass123");
        userDTO1.setActivo(true);

        userDTO2 = new UsuarioDTO();
        userDTO2.setId(2L);
        userDTO2.setNombre("Pedro");
        userDTO2.setApellido("Pérez");
        userDTO2.setEmail("pedro@example.com");
        userDTO2.setPassword("pass456");
        userDTO2.setActivo(true);
        

        trabajoDTO1 = new TrabajoDTO(1L, 1, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, userDTO1);
        trabajoDTO2 = new TrabajoDTO(2L, 2, "Empresa 2", "Posición 2", "Descripción trabajo 2", "2023-02-01", null, true, null, userDTO2);
        trabajo1 = new Trabajo(1L, "Empresa 1", "Posición 1", "Descripción trabajo 1", "2023-01-01", "2023-02-01", false, null, user1);
        trabajo2 = new Trabajo(2L, "Empresa 2", "Posición 2", "Descripción trabajo 2", "2023-02-01", null, true, null, user2);

        pageable = PageRequest.of(0, 10);
    }

    // --------------------------------------------------------------------
    // Page<TrabajoDTO> getAll(Long userId, String searchText)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con todos los parámetros correctos")
    void getAllOfTrabajos_whitAllCorrectParameters_returnListOfTrabajos(){
        when(repository.findAllList(1L, "trabajo")).thenReturn(Arrays.asList(trabajo1, trabajo2));

        List<TrabajoDTO> result = service.getAll(1L, "trabajo");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Descripción trabajo 1", result.get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.get(1).getDescription());
        verify(repository, times(1)).findAllList(1L, "trabajo");
    }
    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con el parámetro searchText nulo")
    void getAllOfTrabajos_whitNullSearchTextParameters_returnListOfTrabajos(){
        when(repository.findAllList(1L, "")).thenReturn(Arrays.asList(trabajo1, trabajo2));

        List<TrabajoDTO> result = service.getAll(1L, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Descripción trabajo 1", result.get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.get(1).getDescription());
        verify(repository, times(1)).findAllList(1L, "");
    }
    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con todos los parámetro nulos")
    void getAllOfTrabajos_whitNullParameters_returnListOfTrabajos(){
        when(repository.findAllList(null, "")).thenReturn(Arrays.asList(trabajo1, trabajo2));

        List<TrabajoDTO> result = service.getAll(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Descripción trabajo 1", result.get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.get(1).getDescription());
        verify(repository, times(1)).findAllList(null, "");
    }

    @Test
    @DisplayName("Obtiene una lista vacía de los trabajos con éxito con todos los parámetros correctos")
    void getAllOfTrabajos_whitAllCorrectParameters_returnEmptyListOfTrabajos(){
        when(repository.findAllList(1L, "trabajo")).thenReturn(Arrays.asList());

        List<TrabajoDTO> result = service.getAll(1L, "trabajo");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAllList(1L, "trabajo");
    }
    
    // --------------------------------------------------------------------
    // Page<TrabajoDTO> getAll(Long userId, String searchText, Integer page, Integer size)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página de los trabajos con éxito con todos los parámetros correctos")
    void getPageOfTrabajos_whitParameters_returnPageOfTrabajos(){
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(repository.findAllPage(1L, "trabajo", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(1L, "trabajo", 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(1L, "trabajo", pageable);
    }
    
    @Test
    @DisplayName("Obtiene una pagina de los trabajos con éxito con el parámetro searchText nulo")
    void getPageOfTrabajos_whitNullSearchTextParameters_returnPageOfTrabajos(){
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(repository.findAllPage(1L, "", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(1L, null, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(1L, "", pageable);
    }
    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con todos los parámetro nulos")
    void getPageOfTrabajos_whitNullParameters_returnPageOfTrabajos(){
        when(utils.createPageable(null, null)).thenReturn(pageable);
        when(repository.findAllPage(null, "", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(null, "", pageable);
    }
    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con el parámetro size nulo")
    void getPageOfTrabajos_whitSizeParamIsNull_returnPageOfTrabajos(){
        when(utils.createPageable(0, null)).thenReturn(pageable);
        when(repository.findAllPage(1L, "trabajo", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(1L, "trabajo", 0, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(1L, "trabajo", pageable);
    }
    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con el parámetro page nulo")
    void getPageOfTrabajos_whitPageParamIsNull_returnPageOfTrabajos(){
        when(utils.createPageable(null, 10)).thenReturn(pageable);
        when(repository.findAllPage(1L, "trabajo", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(1L, "trabajo", null, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(1L, "trabajo", pageable);
    }

    
    @Test
    @DisplayName("Obtiene una lista de los trabajos con éxito con los parámetros page y size nulos")
    void getPageOfTrabajos_whitPageAndSizeParamsAreNull_returnPageOfTrabajos(){
        when(utils.createPageable(null, null)).thenReturn(pageable);
        when(repository.findAllPage(1L, "trabajo", pageable)).thenReturn(new PageImpl<>(Arrays.asList(trabajo1, trabajo2), pageable, 2));

        Page<TrabajoDTO> result = service.getAll(1L, "trabajo", null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Descripción trabajo 1", result.getContent().get(0).getDescription());
        assertEquals("Descripción trabajo 2", result.getContent().get(1).getDescription());
        verify(repository, times(1)).findAllPage(1L, "trabajo", pageable);
    }

    @Test
    @DisplayName("Obtiene una página vaia de los trabajos con éxito con todos los parámetros correctos")
    void getPageOfTrabajos_whitParameters_returnEmptyPageOfTrabajos(){
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(repository.findAllPage(1L, "trabajo", pageable)).thenReturn(new PageImpl<>(Arrays.asList(), pageable, 0));

        Page<TrabajoDTO> result = service.getAll(1L, "trabajo", 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(repository, times(1)).findAllPage(1L, "trabajo", pageable);
    }

    // --------------------------------------------------------------------
    // TrabajoDTO getById(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un trabajo con éxito id válido")
    void getByIdTrabajos_whitIdValid_returnTrabajo(){
        when(repository.findById(1L)).thenReturn(Optional.of(trabajo1));

        TrabajoDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Descripción trabajo 1", result.getDescription());
        verify(repository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Retorna un nulo al recibir un id nulo")
    void getByIdTrabajos_whitIdNull_returnNull(){

        TrabajoDTO result = service.getById(null);

        assertNull(result);
        verify(repository, times(0)).findById(null);
    }

    @Test
    @DisplayName("Retorna un nulo al recibir un id inválido")
    void getByIdTrabajos_whitInvalidaId_returnNull(){
        when(repository.findById(99999L)).thenReturn(Optional.empty());

        TrabajoDTO result = service.getById(99999L);

        assertNull(result);
        verify(repository, times(1)).findById(99999L);
    }
    
    // --------------------------------------------------------------------
    // TrabajoDTO save(TrabajoDTO trabajoDTO)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Registra un nuevo trabajo con éxito")
    void saveTrabajos_Valid_returnTrabajoDTO(){
        Trabajo newTrabajo = new Trabajo();
        newTrabajo.setId(1L);
        newTrabajo.setCompany(trabajo1.getCompany());
        newTrabajo.setPosition(trabajo1.getPosition());
        newTrabajo.setDescription(trabajo1.getDescription());
        newTrabajo.setStartDate(trabajo1.getStartDate());
        newTrabajo.setEndDate(trabajo1.getEndDate());
        newTrabajo.setCurrent(trabajo1.getCurrent());
        newTrabajo.setTecnologias(trabajo1.getTecnologias());
        newTrabajo.setUser(trabajo1.getUser());

        trabajo1.setId(null);
        when(repository.save(any(Trabajo.class))).thenReturn(newTrabajo);

        trabajoDTO1.setId(null);
        TrabajoDTO result = service.save(trabajoDTO1);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(trabajoDTO1.getDescription(), result.getDescription());
        verify(repository, times(1)).save(any(Trabajo.class));
    }
    
    @Test
    @DisplayName("Actualiza un trabajo con éxito")
    void saveTrabajos_UpdateRecord_returnTrabajoDTO(){

        when(repository.save(any(Trabajo.class))).thenReturn(trabajo1);

        TrabajoDTO result = service.save(trabajoDTO1);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(trabajoDTO1.getDescription(), result.getDescription());
        verify(repository, times(1)).save(any(Trabajo.class));
    }
    
    @Test
    @DisplayName("Intenta registrar un nuevo trabajo recibiendo un parámetro null")
    void saveTrabajos_NullParam_returnRuntimeException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.save(null));

        assertEquals("Datos no válidos para guardar el registro.", exception.getMessage());
        verifyNoInteractions(repository);
    }
    
    // --------------------------------------------------------------------
    // void deleteById(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Elimina un trabajo con éxito")
    void deletByIdTrabajos_whitIdValid_returnVoid(){
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Intenta eliminar un trabajo con un parámetro null y retorna un RuntimeException")
    void deletByIdTrabajos_whitNullParam_returnRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteById(null));

        assertEquals("Trabajo no encontrado", exception.getMessage());
        verifyNoInteractions(repository);
    }
    
    @Test
    @DisplayName("Intenta eliminar un trabajo con un id no existente y retorna un RuntimeException")
    void deletByIdTrabajos_whitIOdIOnvalid_returnRuntimeException() {
        when(repository.existsById(99999L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteById(99999L));

        assertEquals("Trabajo no encontrado", exception.getMessage());
        verify(repository, times(0)).deleteById(99999L);
    }
}