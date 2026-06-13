package com.mabc.back_cv.tecnologia;

import com.mabc.back_cv.web.dto.TecnologiaDTO;
import com.mabc.back_cv.web.entities.Tecnologia;
import com.mabc.back_cv.web.repositories.TecnologiaRepository;
import com.mabc.back_cv.web.services.tecnologia.TecnologiaServiceImpl;
import com.mabc.back_cv.web.services.tecnologia.TecnologiaUtils;

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

import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

import org.junit.jupiter.api.BeforeEach;


@ExtendWith(MockitoExtension.class)
class TecnologiaServiceImplTest{

    @Mock
    private Tecnologia entity;

    @Mock
    private TecnologiaRepository repository;

    @InjectMocks
    private TecnologiaServiceImpl service;

    private TecnologiaDTO tecnologiaDTO1;
    private TecnologiaDTO tecnologiaDTO2;
    private Tecnologia tecnologia1;
    private Tecnologia tecnologia2;


    @BeforeEach
    void setUp(){
        reset(repository);
            
        tecnologiaDTO1 = new TecnologiaDTO(1L, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg javascript></svg>");
        tecnologiaDTO2 = new TecnologiaDTO(2L, "JavaScript", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/javascript.png", "<svg javascript></svg>");
        
        tecnologia1 = new Tecnologia(1L, "Java", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/java.png", "<svg javascript></svg>");
        tecnologia2 = new Tecnologia(2L, "JavaScript", TipoTecnologiaEnum.LENGUAJE, "/ruta/imagen/javascript.png", "<svg javascript></svg>");
    }


    // --------------------------------------------------------------------
    // Page<TecnologiaDTO> findAll(String searchText, Integer size, Integer page)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una página de las tecnologias con éxito con todos los parámetros correctos")
    void getPageOfTecnologias_whitAllCorrectParameters_returnPageOfTecnologias(){
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllPage("Java", pageable)).thenReturn(new PageImpl<>(Arrays.asList(tecnologia1, tecnologia2), pageable, 2));

        Page<TecnologiaDTO> result = service.findAll("Java", 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java", result.getContent().get(0).getName());
        assertEquals("JavaScript", result.getContent().get(1).getName());
        verify(repository, times(1)).findAllPage("Java", pageable);
    }

    @Test
    @DisplayName("Obtiene una página de las tecnologias con éxito sólo con el texto de búsqueda")
    void getPageOfTecnologias_whitSearchTextParameterOnly_returnPageOfTecnologias(){
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllPage("Java", pageable)).thenReturn(new PageImpl<>(Arrays.asList(tecnologia1, tecnologia2), pageable, 2));

        Page<TecnologiaDTO> result = service.findAll("Java", null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java", result.getContent().get(0).getName());
        assertEquals("JavaScript", result.getContent().get(1).getName());
        verify(repository, times(1)).findAllPage("Java", pageable);
    }

    @Test
    @DisplayName("Obtiene una página de las tecnologias con éxito con parámetros nulos")
    void getPageOfTecnologias_whitNullParameters_returnPageOfTecnologias(){
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllPage("", pageable)).thenReturn(new PageImpl<>(Arrays.asList(tecnologia1, tecnologia2), pageable, 2));

        Page<TecnologiaDTO> result = service.findAll(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java", result.getContent().get(0).getName());
        assertEquals("JavaScript", result.getContent().get(1).getName());
        verify(repository, times(1)).findAllPage("", pageable);
    }


    @Test
    @DisplayName("Obtiene una página de las tecnologias con éxito con parámetros de busqueda correcto y paginacion no válidos")
    void getPageOfTecnologias_whithSearchTextAndInvalidPaginationParameters_returnPageOfTecnologias(){
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllPage("script", pageable)).thenReturn(new PageImpl<>(Arrays.asList(tecnologia2), pageable, 1));

        Page<TecnologiaDTO> result = service.findAll("script", -1, -20);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("JavaScript", result.getContent().get(0).getName());
        verify(repository, times(1)).findAllPage("script", pageable);
    }

    @Test
    @DisplayName("Obtiene una página en blanco de las tecnologias con éxito con parámetros correctos")
    void getPageOfTecnologias_whithAllCorrectParameters_returnEmptyPage(){
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllPage("PHP", pageable)).thenReturn(new PageImpl<>(Arrays.asList(), pageable, 0));

        Page<TecnologiaDTO> result = service.findAll("PHP", 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(repository, times(1)).findAllPage("PHP", pageable);
    }

    // --------------------------------------------------------------------
    // List<TecnologiaDTO> findAll(String searchText) 
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene una lista de las tecnologias con éxito con el parámetro de busqueda correcto")
    void getListOfTecnologias_whitValidSearchTextParameters_returnListOfTecnologias(){
        when(repository.findAllList("Java")).thenReturn(Arrays.asList(tecnologia1, tecnologia2));

        List<TecnologiaDTO> result = service.findAll("Java");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("JavaScript", result.get(1).getName());
        verify(repository, times(1)).findAllList("Java");
    }

    @Test
    @DisplayName("Obtiene una lista de las tecnologias con éxito con el parámetro de busqueda nulo")
    void getListOfTecnologias_whithNullSearchTextParameters_returnListOfTecnologias(){
        when(repository.findAllList("")).thenReturn(Arrays.asList(tecnologia1, tecnologia2));

        List<TecnologiaDTO> result = service.findAll(null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("JavaScript", result.get(1).getName());
        verify(repository, times(1)).findAllList("");
    }

    @Test
    @DisplayName("Obtiene una lista vacia de las tecnologias con el parámetro de busqueda valido")
    void getListOfTecnologias_whithSearchTextParameters_returnEmptyList(){
        when(repository.findAllList("PHP")).thenReturn(Arrays.asList());

        List<TecnologiaDTO> result = service.findAll("PHP");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAllList("PHP");
    }

    // --------------------------------------------------------------------
    // getById(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Obtiene un elemento de tecnologia con éxito con el parámetro de busqueda correcto")
    void getByIdOfTecnologias_whitValidIdParameters_returnElementOfTecnologias(){
        when(repository.findById(1L)).thenReturn(Optional.of(tecnologia1));

        TecnologiaDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Java", result.getName());
        verify(repository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Obtiene un null con el parámetro de busqueda nulo")
    void getByIdOfTecnologias_whithNullIdParameters_returnNull(){

        TecnologiaDTO result = service.getById(null);

        assertNull(result);
        verify(repository, times(0)).findById(null);
    }

    @Test
    @DisplayName("Obtiene un null cuando no existe el elemento con el id proporcionado en el parámetro de busqueda")
    void getByIdOfTecnologias_whithIdNotExists_returnNull(){
        when(repository.findById(9999L)).thenReturn(Optional.empty());

        TecnologiaDTO result = service.getById(9999L);

        assertNull(result);
        verify(repository, times(1)).findById(9999L);
    }
    
    // --------------------------------------------------------------------
    // save(TecnologiaDTO tecnologiaDTO)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("Crea un registro a partir de un Objeto TecnologiaDTO válido con éxito y retorna el Objeto TecnologiaDTO")
    void saveTecnologia_whitValidTecnologiaDTOParameters_returnElementOfTecnologia(){
        when(repository.save(any(Tecnologia.class))).thenReturn(tecnologia1);

        tecnologiaDTO1.setId(null);
        TecnologiaDTO result = service.save(tecnologiaDTO1);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(tecnologiaDTO1.getName(), result.getName());
        verify(repository, times(1)).save(any(Tecnologia.class));
    }


    @Test
    @DisplayName("Llama al metodo save pasando un null y retorna null")
    void saveTecnologias_whithNullIdParameters_returnNull(){

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.save(null));

        assertEquals("Datos no válidos para guardar el registro.", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Retorna un error al intentar crear un nuevo elemento con propiedades no válidas o faltantes")
    void saveTecnologias_whithNoValidProperties_returnError(){
        tecnologia1.setName(null);

        when(repository.save(any(Tecnologia.class))).thenThrow(new RuntimeException("Error de base de datos"));
        
        assertThrows(RuntimeException.class, () -> service.save(tecnologiaDTO1));
        verify(repository, times(1)).save(any(Tecnologia.class));
    }
    
    // --------------------------------------------------------------------
    // delete(Long id)
    // --------------------------------------------------------------------
    @Test
    @DisplayName("elimina un registro a partir de un id válido")
    void deleteTecnologia_whithValidIdParameters_returnVoid(){
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        service.deleteById(1L);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Intenta eliminar un registro con un id que no existe, Genera un error de runtime")
    void deleteTecnologias_whithInexistentIdParameters_returnError(){
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deleteById(1L));
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(0)).deleteById(1L);
    }

    @Test
    @DisplayName("Retorna un error al recibir un id nulo para intentar eliminar un registro")
    void deleteTecnologias_whithNullIdParameters_returnError(){

        assertThrows(RuntimeException.class, () -> service.deleteById(null));
        verifyNoInteractions(repository);
    }
}