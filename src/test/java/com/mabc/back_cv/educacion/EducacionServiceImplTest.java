package com.mabc.back_cv.educacion;

import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Educacion;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.EducacionRepository;
import com.mabc.back_cv.web.services.educacion.EducacionServiceImpl;
import com.mabc.back_cv.web.services.educacion.EducacionUtils;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.mabc.back_cv.common.Utils;

@ExtendWith(MockitoExtension.class)
class EducacionServiceImplTest {

    @Mock
    private EducacionRepository educacionRepository;
    
    @Mock
    private Utils utils;


    @InjectMocks
    private EducacionServiceImpl educacionService;

    private Educacion educacion;
    private EducacionDTO educacionDTO;
    private User usuario;
    private UsuarioDTO usuarioDTO;
    private Page<Educacion> educacionPage;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Perez");

        educacion = new Educacion();
        educacion.setId(1L);
        educacion.setInstitution("Universidad de Chile");
        educacion.setTitle("Ingeniería Civil");
        educacion.setShortTitle("Ing. Civil");
        educacion.setName("Juan Pérez");
        educacion.setDescription("Carrera de ingeniería civil");
        educacion.setYearFrom(2018);
        educacion.setYearTo(2023);
        educacion.setDuration(10);
        educacion.setImage("logo.png");
        educacion.setUrl("https://uchile.cl");
        educacion.setStyles("style1");
        educacion.setUsuario(usuario);

        educacionDTO = new EducacionDTO();
        educacionDTO.setId(1L);
        educacionDTO.setInstitution("Universidad de Chile");
        educacionDTO.setTitle("Ingeniería Civil");
        educacionDTO.setShortTitle("Ing. Civil");
        educacionDTO.setName("Juan Pérez");
        educacionDTO.setDescription("Carrera de ingeniería civil");
        educacionDTO.setYearFrom(2018);
        educacionDTO.setYearTo(2023);
        educacionDTO.setDuration(10);
        educacionDTO.setImage("logo.png");
        educacionDTO.setUrl("https://uchile.cl");
        educacionDTO.setStyles("style1");
        educacionDTO.setUsuario(usuarioDTO);

        List<Educacion> educacionList = Arrays.asList(educacion);
        educacionPage = new PageImpl<>(educacionList);
        
        pageable = PageRequest.of(0, 10);
    }

    // Tests para findByUserId

    @Test
    void findByUserId_conParametrosValidos_retornaPaginaCorrecta() {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(educacionPage);
        
        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(educacionDTO.getInstitution(), resultado.getContent().get(0).getInstitution());
        verify(educacionRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void findByUserId_conPageNull_retornaPaginaConDefaults() {
        when(utils.createPageable(null, null)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, null, null);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPageNegativo_retornaPaginaConDefaults() {
        when(utils.createPageable(-1, 10)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, -1, 10);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeNull_retornaPaginaConDefaults() {
        when(utils.createPageable(0, null)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, 0, null);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeCero_retornaPaginaConDefaults() {
        when(utils.createPageable(0, 0)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, 0, 0);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPaginaVacia_retornaPaginaVacia() {
        Page<Educacion> paginaVacia = new PageImpl<>(Arrays.asList());
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.findByUserId(anyLong(), any())).thenReturn(paginaVacia);

        Page<EducacionDTO> resultado = educacionService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    // Tests para findBySearchText

    @Test
    void findBySearchText_conSearchTextNull_retornaTodosLosRegistros() {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.getAllByUserId(eq(1L), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findBySearchText(1L, null, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(educacionRepository, times(1)).getAllByUserId(eq(1L), any());
        verify(educacionRepository, never()).findBySearchText(anyLong(), anyString(), any());
    }

    @Test
    void findBySearchText_conSearchTextValido_retornaResultadosFiltrados() {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.findBySearchText(eq(1L), eq("Ingeniería"), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findBySearchText(1L, "Ingeniería", 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(educacionRepository, times(1)).findBySearchText(eq(1L), eq("Ingeniería"), any());
        verify(educacionRepository, never()).getAllByUserId(anyLong(), any());
    }

    @Test
    void findBySearchText_conSearchTextVacio_retornaTodosLosRegistros() {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.findBySearchText(eq(1L), eq(""), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findBySearchText(1L, "", 0, 10);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findBySearchText(eq(1L), eq(""), any());
    }

    @Test
    void findBySearchText_conUserIdNull_retornaTodosLosRegistros() {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        when(educacionRepository.getAllByUserId(isNull(), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findBySearchText(null, null, 0, 10);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).getAllByUserId(isNull(), any());
    }

    @Test
    void findBySearchText_conPaginacionValida_retornaPaginaCorrecta() {
        when(utils.createPageable(2, 20)).thenReturn(pageable);
        when(educacionRepository.findBySearchText(eq(1L), eq("test"), any())).thenReturn(educacionPage);

        Page<EducacionDTO> resultado = educacionService.findBySearchText(1L, "test", 2, 20);

        assertNotNull(resultado);
        verify(educacionRepository, times(1)).findBySearchText(eq(1L), eq("test"), any());
    }

    // Tests para findById

    @Test
    void findById_conIdValido_retornaDTOCorrecto() {
        when(educacionRepository.findById(1L)).thenReturn(Optional.of(educacion));

        EducacionDTO resultado = educacionService.findById(1L);

        assertNotNull(resultado);
        assertEquals(educacion.getId(), resultado.getId());
        assertEquals(educacion.getInstitution(), resultado.getInstitution());
        verify(educacionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_conIdNoExistente_retornaNull() {
        when(educacionRepository.findById(999L)).thenReturn(Optional.empty());

        EducacionDTO resultado = educacionService.findById(999L);

        assertNull(resultado);
        verify(educacionRepository, times(1)).findById(999L);
    }

    @Test
    void findById_conIdNull_retornaNull() {
        EducacionDTO resultado = educacionService.findById(null);

        assertNull(resultado);
        verify(educacionRepository, never()).findById(anyLong());
    }

    @Test
    void findById_conIdCero_retornaNull() {
        when(educacionRepository.findById(0L)).thenReturn(Optional.empty());

        EducacionDTO resultado = educacionService.findById(0L);

        assertNull(resultado);
        verify(educacionRepository, times(1)).findById(0L);
    }

    @Test
    void findById_conIdNegativo_retornaNull() {
        when(educacionRepository.findById(-1L)).thenReturn(Optional.empty());

        EducacionDTO resultado = educacionService.findById(-1L);

        assertNull(resultado);
        verify(educacionRepository, times(1)).findById(-1L);
    }

    // Tests para save

    @Test
    void save_conDTOValido_retornaDTOGuardado() {
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacion);

        EducacionDTO resultado = educacionService.save(educacionDTO);

        assertNotNull(resultado);
        assertEquals(educacionDTO.getInstitution(), resultado.getInstitution());
        verify(educacionRepository, times(1)).save(any(Educacion.class));
    }

    @Test
    void save_conDTONull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            educacionService.save(null);
        });

        verify(educacionRepository, never()).save(any(Educacion.class));
    }

    @Test
    void save_conDTOConIdNull_retornaDTOGuardadoConIdGenerado() {
        educacionDTO.setId(null);
        educacion.setId(2L);
        when(educacionRepository.save(any(Educacion.class))).thenReturn(educacion);

        EducacionDTO resultado = educacionService.save(educacionDTO);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(educacionRepository, times(1)).save(any(Educacion.class));
    }

    @Test
    void save_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(educacionRepository.save(any(Educacion.class))).thenThrow(new RuntimeException("Error de base de datos"));

        assertThrows(RuntimeException.class, () -> {
            educacionService.save(educacionDTO);
        });

        verify(educacionRepository, times(1)).save(any(Educacion.class));
    }

    // Tests para delete

    @Test
    void delete_conIdValido_eliminaRegistro() {
        when(educacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(educacionRepository).deleteById(1L);

        educacionService.delete(1L);

        verify(educacionRepository, times(1)).existsById(1L);
        verify(educacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_conIdNull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            educacionService.delete(null);
        });

        verify(educacionRepository, never()).existsById(anyLong());
        verify(educacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNoExistente_lanzaIllegalArgumentException() {
        when(educacionRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            educacionService.delete(999L);
        });

        verify(educacionRepository, times(1)).existsById(999L);
        verify(educacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdCero_lanzaIllegalArgumentException() {
        when(educacionRepository.existsById(0L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            educacionService.delete(0L);
        });

        verify(educacionRepository, times(1)).existsById(0L);
        verify(educacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNegativo_lanzaIllegalArgumentException() {
        when(educacionRepository.existsById(-1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            educacionService.delete(-1L);
        });

        verify(educacionRepository, times(1)).existsById(-1L);
        verify(educacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(educacionRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Error de base de datos")).when(educacionRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> {
            educacionService.delete(1L);
        });

        verify(educacionRepository, times(1)).existsById(1L);
        verify(educacionRepository, times(1)).deleteById(1L);
    }
}
