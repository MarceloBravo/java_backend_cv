package com.mabc.back_cv.contenidoCurso;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.repositories.ContenidoCursoRepository;
import com.mabc.back_cv.web.services.contenidoCurso.ContenidoCursoServiceImpl;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;

import org.modelmapper.ModelMapper;

import com.mabc.back_cv.common.Utils;

@ExtendWith(MockitoExtension.class)
public class ContenidoCursoServiceImplTest{

    @Mock
    private ContenidoCursoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Utils utils;

    @InjectMocks
    private ContenidoCursoServiceImpl service;

    private ContenidoCurso contenidoCurso1;
    private ContenidoCurso contenidoCurso2;
    private ContenidoCurso contenidoCurso3;
    private ContenidoCursoDTO contenidoCursoDTO1;
    private ContenidoCursoDTO contenidoCursoDTO2;
    private ContenidoCursoDTO contenidoCursoDTO3;
    private Pageable pageable;

    @BeforeEach
    void Setup(){
        reset(repository);

        contenidoCurso1 = new ContenidoCurso(1L, "Curso 1", "Contenido de ejemplo del curso 1", true);
        contenidoCurso2 = new ContenidoCurso(2L, "Curso 2", "Contenido de ejemplo del curso 2", true);
        contenidoCurso3 = new ContenidoCurso(3L, "Curso 3", "Contenido de ejemplo del curso 3", false);
        
        contenidoCursoDTO1 = new ContenidoCursoDTO(1L, "Curso 1", "Contenido de ejemplo del curso 1", true);
        contenidoCursoDTO2 = new ContenidoCursoDTO(2L, "Curso 2", "Contenido de ejemplo del curso 2", true);
        contenidoCursoDTO3 = new ContenidoCursoDTO(3L, "Curso 3", "Contenido de ejemplo del curso 3", false);

        pageable = PageRequest.of(0, 10);
    }

    private ContenidoCursoDTO entityToDTO(ContenidoCurso entity){
        ContenidoCursoDTO dto = new ContenidoCursoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setActivo(entity.getActivo());
        
        return dto;
    }

    // --------------------------------------------------------------------
    // List<ContenidoCursoDTO> findAllList(String searchText, Boolean activo)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista de los Contenidos de un Curso con éxito con todos los parámetros correctos")
    void getListOfContenidoCurso_whitAllCorrectParameters_returnPageOfContenidoCurso(){
        when(repository.findAllList("Curso", true)).thenReturn(Arrays.asList(contenidoCurso1, contenidoCurso2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {
            ContenidoCurso entidadId = invocation.getArgument(0);             
            return entityToDTO(entidadId);
        });

        List<ContenidoCursoDTO> result = service.findAllList("Curso", true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Curso 1", result.get(0).getTitle());
        assertEquals("Curso 2", result.get(1).getTitle());
        verify(repository, times(1)).findAllList("Curso", true);
    }

    @Test
    @DisplayName("Obtiene una lista de los Contenidos de un Curso con éxito sin el parámetro searchText")
    void getListOfContenidoCurso_whitSearchTextIsNull_returnPageOfContenidoCurso(){
        when(repository.findAllList(null, true)).thenReturn(Arrays.asList(contenidoCurso1, contenidoCurso2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        List<ContenidoCursoDTO> result = service.findAllList(null, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Curso 1", result.get(0).getTitle());
        assertEquals("Curso 2", result.get(1).getTitle());
        verify(repository, times(1)).findAllList(null, true);
    }

    @Test
    @DisplayName("Obtiene una lista de los Contenidos de un Curso con éxito sin el parámetro activo")
    void getListOfContenidoCurso_whitActivoIsNull_returnPageOfContenidoCurso(){
        when(repository.findAllList("Curso", null)).thenReturn(Arrays.asList(contenidoCurso1, contenidoCurso2, contenidoCurso3));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        List<ContenidoCursoDTO> result = service.findAllList("Curso", null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Curso 1", result.get(0).getTitle());
        assertEquals("Curso 2", result.get(1).getTitle());
        assertEquals("Curso 3", result.get(2).getTitle());
        verify(repository, times(1)).findAllList("Curso", null);
    }
    
    @Test
    @DisplayName("Obtiene una list de los Contenidos de un Curso con éxito con el parámetro activo = Falso")
    void getListOfContenidoCurso_whitActivoIsFalse_returnPageOfContenidoCurso(){
        when(repository.findAllList("Curso", false)).thenReturn(Arrays.asList(contenidoCurso3));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        List<ContenidoCursoDTO> result = service.findAllList("Curso", false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Curso 3", result.get(0).getTitle());
        verify(repository, times(1)).findAllList("Curso", false);
    }
        
    @Test
    @DisplayName("Obtiene una list de los Contenidos de un Curso con todos sus parámetros nulos")
    void getListOfContenidoCurso_whitAllParametersAreNull_returnPageOfContenidoCurso(){
        when(repository.findAllList(null, null)).thenReturn(Arrays.asList(contenidoCurso1, contenidoCurso2, contenidoCurso3));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        List<ContenidoCursoDTO> result = service.findAllList(null, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Curso 1", result.get(0).getTitle());
        assertEquals("Curso 2", result.get(1).getTitle());
        assertEquals("Curso 3", result.get(2).getTitle());
        verify(repository, times(1)).findAllList(null, null);
    }

    // --------------------------------------------------------------------
    // Page<ContenidoCursoDTO> findAllPage(String searchText, Integer page, Integer size, Boolean activo)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con todos los parámetros correctos")
    void getPageOfContenidoCurso_whitAllCorrectParameters_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {
            ContenidoCurso entidadId = invocation.getArgument(0);             
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", 0, 10, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage("Curso", true, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito sin el parámetro searchText")
    void getPageOfContenidoCurso_whitSearchTextIsNull_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,10)).thenReturn(pageable);
        when(repository.findAllPage(null, true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage(null, 0, 10, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage(null, true, pageable);
    }

    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito sin el parámetro activo")
    void getPageOfContenidoCurso_whitActivoIsNull_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", null, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2, contenidoCurso3), pageable, 3));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", 0, 10, null);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        assertEquals("Curso 3", result.getContent().get(2).getTitle());
        verify(repository, times(1)).findAllPage("Curso", null, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con el parámetro activo = Falso")
    void getPageOfContenidoCurso_whitActivoIsFalse_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", false, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso3), pageable, 1));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", 0, 10, false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Curso 3", result.getContent().get(0).getTitle());
        verify(repository, times(1)).findAllPage("Curso", false, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con el parámetro size nulo")
    void getPageOfContenidoCurso_whitSizeIsNull_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,null)).thenReturn(pageable);
        when(repository.findAllPage("Curso", true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", 0, null, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage("Curso", true, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con el parámetro page nulo")
    void getPageOfContenidoCurso_whitPageIsNull_returnPageOfContenidoCurso(){
        when(utils.createPageable(null, 10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", null, 10, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage("Curso", true, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con todos sus parámetros nulos")
    void getPageOfContenidoCurso_whitAllParametersAreNull_returnPageOfContenidoCurso(){
        when(utils.createPageable(null,null)).thenReturn(pageable);
        when(repository.findAllPage(null, null, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2, contenidoCurso3), pageable, 3));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage(null, null, null, null);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        assertEquals("Curso 3", result.getContent().get(2).getTitle());
        verify(repository, times(1)).findAllPage(null, null, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con el parámetro page negativo")
    void getPageOfContenidoCurso_whitPageIsNegative_returnPageOfContenidoCurso(){
        when(utils.createPageable(-10,10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", -10, 10, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage("Curso", true, pageable);
    }
    
    @Test
    @DisplayName("Obtiene una página de los Contenidos de un Curso con éxito con el parámetro size negativo")
    void getPageOfContenidoCurso_whitSizeIsNegative_returnPageOfContenidoCurso(){
        when(utils.createPageable(0,-10)).thenReturn(pageable);
        when(repository.findAllPage("Curso", true, pageable)).thenReturn(new PageImpl<>(Arrays.asList(contenidoCurso1, contenidoCurso2), pageable, 2));
        
        when(modelMapper.map(any(), eq(ContenidoCursoDTO.class))).thenAnswer(invocation -> {            
            ContenidoCurso entidadId = invocation.getArgument(0); 
            return entityToDTO(entidadId);
        });

        Page<ContenidoCursoDTO> result = service.findAllPage("Curso", 0, -10, true);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Curso 1", result.getContent().get(0).getTitle());
        assertEquals("Curso 2", result.getContent().get(1).getTitle());
        verify(repository, times(1)).findAllPage("Curso", true, pageable);
    }

    // --------------------------------------------------------------------
    // ContenidoCursoDTO getById(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un registrio de los Contenidos de un Curso con éxito con todos los parámetros correctos")
    void getByIdOfContenidoCurso_whitValidId_returnContenidoCurso(){
        when(repository.findById(1L)).thenReturn(Optional.of(contenidoCurso1));
        when(modelMapper.map(contenidoCurso1, ContenidoCursoDTO.class)).thenReturn(contenidoCursoDTO1);
        
        ContenidoCursoDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Curso 1", result.getTitle());
        verify(repository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Obtiene un registro nulo con un ID no existente")
    void getByIdOfContenidoCurso_whitInvalidId_returnNull(){
        when(repository.findById(99999L)).thenReturn(Optional.empty());
        
        ContenidoCursoDTO result = service.getById(99999L);

        assertNull(result);
        verify(repository, times(1)).findById(99999L);
    }
    
    @Test
    @DisplayName("Retorna nulo al recibir un parámetro nulo")
    void getByIdOfContenidoCurso_whenIdIsNull_returnNull(){
        
        ContenidoCursoDTO result = service.getById(null);

        assertNull(result);
        verifyNoInteractions(repository);
    }
    
    // --------------------------------------------------------------------
    // ContenidoCursoDTO save(ContenidoCursoDTO dto)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Guarda un registro de Contenidos de un Curso con éxito")
    void saveNewContenidoCurso_returnContenidoCurso(){
        ContenidoCurso newContenidoCurso = new ContenidoCurso(null, "Curso 1", "Contenido de ejemplo del curso 1", true);
        ContenidoCursoDTO newContenidoCursoDTO = new ContenidoCursoDTO(null, "Curso 1", "Contenido de ejemplo del curso 1", true);
        when(repository.save(newContenidoCurso)).thenReturn(contenidoCurso1);
        when(modelMapper.map(newContenidoCursoDTO, ContenidoCurso.class)).thenReturn(newContenidoCurso);
        when(modelMapper.map(contenidoCurso1, ContenidoCursoDTO.class)).thenReturn(contenidoCursoDTO1);
        
        ContenidoCursoDTO result = service.save(newContenidoCursoDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Curso 1", result.getTitle());
        verify(repository, times(1)).save(newContenidoCurso);
    }
    
    @Test
    @DisplayName("Intenta guardar un registro de Contenidos de un Curso con éxito, retorna null")
    void saveNewContenidoCurso_returnNull(){
        ContenidoCurso newContenidoCurso = new ContenidoCurso(null, "Curso 1", "Contenido de ejemplo del curso 1", true);
        ContenidoCursoDTO newContenidoCursoDTO = new ContenidoCursoDTO(null, "Curso 1", "Contenido de ejemplo del curso 1", true);
        when(repository.save(newContenidoCurso)).thenReturn(null);
        when(modelMapper.map(newContenidoCursoDTO, ContenidoCurso.class)).thenReturn(newContenidoCurso);
        
        ContenidoCursoDTO result = service.save(newContenidoCursoDTO);

        assertNull(result);
        verify(repository, times(1)).save(newContenidoCurso);
    }
    
    @Test
    @DisplayName("Intenta guardar un registro recibiendo un parámetro null, retorna null")
    void saveNewContenidoCurso_whitNullObject_returnNull(){
        ContenidoCursoDTO result = service.save(null);

        assertNull(result);
        verifyNoInteractions(repository);
    }
    
    // --------------------------------------------------------------------
    // void delete(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Elimina un registro de Contenidos de un Curso con éxito")
    void deleteContenidoCurso_returnVoid(){
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        
        service.delete(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Intenta eliminar un registro de Contenidos de un Curso con id inexistente")
    void deleteContenidoCurso_whitIdInvalid_returnError(){
        when(repository.existsById(99999L)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> service.delete(99999L));

        verify(repository, times(1)).existsById(99999L);
        verify(repository, never()).deleteById(99999L);
    }
    
    @Test
    @DisplayName("Intenta eliminar un registro de Contenidos de un Curso con id nulo")
    void deleteContenidoCurso_whitIdNull_returnError(){
        
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));

        verifyNoInteractions(repository);
    }
}