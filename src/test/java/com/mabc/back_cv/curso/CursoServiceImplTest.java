package com.mabc.back_cv.curso;

import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.CursoRepository;
import com.mabc.back_cv.web.services.curso.CursoServiceImpl;
import com.mabc.back_cv.web.services.curso.CursoMapper;

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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    private Curso curso;
    private CursoDTO cursoDTO;
    private User usuario;
    private UsuarioDTO usuarioDTO;
    private Page<Curso> cursoPage;
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

        curso = new Curso();
        curso.setId(1L);
        curso.setName("Curso de Java");
        curso.setTitle("Java Avanzado");
        curso.setInstitute("Oracle University");
        curso.setStartDate(new Date());
        curso.setEndDate(new Date());
        curso.setActivo(true);
        curso.setUsuario(usuario);

        cursoDTO = new CursoDTO();
        cursoDTO.setId(1L);
        cursoDTO.setName("Curso de Java");
        cursoDTO.setTitle("Java Avanzado");
        cursoDTO.setInstitute("Oracle University");
        cursoDTO.setStartDate(new Date());
        cursoDTO.setEndDate(new Date());
        cursoDTO.setActivo(true);
        cursoDTO.setUsuario(usuarioDTO);

        List<Curso> cursoList = Arrays.asList(curso);
        cursoPage = new PageImpl<>(cursoList);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findByUserId_conParametrosValidos_retornaPaginaCorrecta() {
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(cursoDTO.getName(), resultado.getContent().get(0).getName());
        verify(cursoRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void findByUserId_conPageNull_retornaPaginaConDefaults() {
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, null, null);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPageNegativo_retornaPaginaConDefaults() {
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, -1, 10);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeNull_retornaPaginaConDefaults() {
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, 0, null);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeCero_retornaPaginaConDefaults() {
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, 0, 0);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPaginaVacia_retornaPaginaVacia() {
        Page<Curso> paginaVacia = new PageImpl<>(Arrays.asList());
        when(cursoRepository.findByUserId(anyLong(), any())).thenReturn(paginaVacia);

        Page<CursoDTO> resultado = cursoService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void findBySearchText_conSearchTextNull_retornaTodosLosRegistros() {
        when(cursoRepository.getAllByUserId(eq(1L), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findBySearchText(1L, null, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(cursoRepository, times(1)).getAllByUserId(eq(1L), any());
        verify(cursoRepository, never()).findBySearchText(anyLong(), anyString(), any());
    }

    @Test
    void findBySearchText_conSearchTextValido_retornaResultadosFiltrados() {
        when(cursoRepository.findBySearchText(eq(1L), eq("Java"), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findBySearchText(1L, "Java", 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(cursoRepository, times(1)).findBySearchText(eq(1L), eq("Java"), any());
        verify(cursoRepository, never()).getAllByUserId(anyLong(), any());
    }

    @Test
    void findBySearchText_conSearchTextVacio_retornaTodosLosRegistros() {
        when(cursoRepository.findBySearchText(eq(1L), eq(""), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findBySearchText(1L, "", 0, 10);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findBySearchText(eq(1L), eq(""), any());
    }

    @Test
    void findBySearchText_conUserIdNull_retornaTodosLosRegistros() {
        when(cursoRepository.getAllByUserId(isNull(), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findBySearchText(null, null, 0, 10);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).getAllByUserId(isNull(), any());
    }

    @Test
    void findBySearchText_conPaginacionValida_retornaPaginaCorrecta() {
        when(cursoRepository.findBySearchText(eq(1L), eq("test"), any())).thenReturn(cursoPage);

        Page<CursoDTO> resultado = cursoService.findBySearchText(1L, "test", 2, 20);

        assertNotNull(resultado);
        verify(cursoRepository, times(1)).findBySearchText(eq(1L), eq("test"), any());
    }

    @Test
    void findById_conIdValido_retornaDTOCorrecto() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        CursoDTO resultado = cursoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(curso.getId(), resultado.getId());
        assertEquals(curso.getName(), resultado.getName());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_conIdNoExistente_retornaNull() {
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        CursoDTO resultado = cursoService.findById(999L);

        assertNull(resultado);
        verify(cursoRepository, times(1)).findById(999L);
    }

    @Test
    void findById_conIdNull_retornaNull() {
        CursoDTO resultado = cursoService.findById(null);

        assertNull(resultado);
        verify(cursoRepository, never()).findById(anyLong());
    }

    @Test
    void findById_conIdCero_retornaNull() {
        when(cursoRepository.findById(0L)).thenReturn(Optional.empty());

        CursoDTO resultado = cursoService.findById(0L);

        assertNull(resultado);
        verify(cursoRepository, times(1)).findById(0L);
    }

    @Test
    void findById_conIdNegativo_retornaNull() {
        when(cursoRepository.findById(-1L)).thenReturn(Optional.empty());

        CursoDTO resultado = cursoService.findById(-1L);

        assertNull(resultado);
        verify(cursoRepository, times(1)).findById(-1L);
    }

    @Test
    void save_conDTOValido_retornaDTOGuardado() {
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoDTO resultado = cursoService.save(cursoDTO);

        assertNotNull(resultado);
        assertEquals(cursoDTO.getName(), resultado.getName());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void save_conDTONull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.save(null);
        });

        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void save_conDTOConIdNull_retornaDTOGuardadoConIdGenerado() {
        cursoDTO.setId(null);
        curso.setId(2L);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        CursoDTO resultado = cursoService.save(cursoDTO);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void save_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(cursoRepository.save(any(Curso.class))).thenThrow(new RuntimeException("Error de base de datos"));

        assertThrows(RuntimeException.class, () -> {
            cursoService.save(cursoDTO);
        });

        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void delete_conIdValido_eliminaRegistro() {
        when(cursoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cursoRepository).deleteById(1L);

        cursoService.delete(1L);

        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_conIdNull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.delete(null);
        });

        verify(cursoRepository, never()).existsById(anyLong());
        verify(cursoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNoExistente_lanzaIllegalArgumentException() {
        when(cursoRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.delete(999L);
        });

        verify(cursoRepository, times(1)).existsById(999L);
        verify(cursoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdCero_lanzaIllegalArgumentException() {
        when(cursoRepository.existsById(0L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.delete(0L);
        });

        verify(cursoRepository, times(1)).existsById(0L);
        verify(cursoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNegativo_lanzaIllegalArgumentException() {
        when(cursoRepository.existsById(-1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.delete(-1L);
        });

        verify(cursoRepository, times(1)).existsById(-1L);
        verify(cursoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(cursoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Error de base de datos")).when(cursoRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> {
            cursoService.delete(1L);
        });

        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }
}
