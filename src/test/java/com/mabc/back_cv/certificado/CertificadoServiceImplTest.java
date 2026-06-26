package com.mabc.back_cv.certificado;

import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.CertificadoRepository;
import com.mabc.back_cv.web.services.certificado.CertificadoServiceImpl;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificadoServiceImplTest {

    @Mock
    private CertificadoRepository certificadoRepository;

    @InjectMocks
    private CertificadoServiceImpl certificadoService;

    private Certificado certificado;
    private CertificadoDTO certificadoDTO;
    private User usuario;
    private UsuarioDTO usuarioDTO;
    private Page<Certificado> certificadoPage;
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

        certificado = new Certificado();
        certificado.setId(1L);
        certificado.setName("Certificado Java");
        certificado.setImage("certificado.png");
        certificado.setUrl("https://certificados.com/java");
        certificado.setMouse_move_title("Certificado");
        certificado.setMouse_move_description("Certificado de Java Avanzado");
        certificado.setUser(usuario);

        certificadoDTO = new CertificadoDTO();
        certificadoDTO.setId(1L);
        certificadoDTO.setName("Certificado Java");
        certificadoDTO.setImage("certificado.png");
        certificadoDTO.setUrl("https://certificados.com/java");
        certificadoDTO.setMouse_move_title("Certificado");
        certificadoDTO.setMouse_move_description("Certificado de Java Avanzado");
        certificadoDTO.setUser(usuarioDTO);

        List<Certificado> certificadoList = Arrays.asList(certificado);
        certificadoPage = new PageImpl<>(certificadoList);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findByUserId_conParametrosValidos_retornaPaginaCorrecta() {
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(certificadoDTO.getName(), resultado.getContent().get(0).getName());
        verify(certificadoRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void findByUserId_conPageNull_retornaPaginaConDefaults() {
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, null, null);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPageNegativo_retornaPaginaConDefaults() {
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, -1, 10);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeNull_retornaPaginaConDefaults() {
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, 0, null);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conSizeCero_retornaPaginaConDefaults() {
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, 0, 0);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findByUserId(eq(1L), any());
    }

    @Test
    void findByUserId_conPaginaVacia_retornaPaginaVacia() {
        Page<Certificado> paginaVacia = new PageImpl<>(Arrays.asList());
        when(certificadoRepository.findByUserId(anyLong(), any())).thenReturn(paginaVacia);

        Page<CertificadoDTO> resultado = certificadoService.findByUserId(1L, 0, 10);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void findBySearchText_conSearchTextNull_retornaTodosLosRegistros() {
        when(certificadoRepository.getAllByUserId(eq(1L), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findBySearchText(1L, null, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(certificadoRepository, times(1)).getAllByUserId(eq(1L), any());
        verify(certificadoRepository, never()).findBySearchText(anyLong(), anyString(), any());
    }

    @Test
    void findBySearchText_conSearchTextValido_retornaResultadosFiltrados() {
        when(certificadoRepository.findBySearchText(eq(1L), eq("Java"), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findBySearchText(1L, "Java", 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(certificadoRepository, times(1)).findBySearchText(eq(1L), eq("Java"), any());
        verify(certificadoRepository, never()).getAllByUserId(anyLong(), any());
    }

    @Test
    void findBySearchText_conSearchTextVacio_retornaTodosLosRegistros() {
        when(certificadoRepository.findBySearchText(eq(1L), eq(""), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findBySearchText(1L, "", 0, 10);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findBySearchText(eq(1L), eq(""), any());
    }

    @Test
    void findBySearchText_conUserIdNull_retornaTodosLosRegistros() {
        when(certificadoRepository.getAllByUserId(isNull(), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findBySearchText(null, null, 0, 10);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).getAllByUserId(isNull(), any());
    }

    @Test
    void findBySearchText_conPaginacionValida_retornaPaginaCorrecta() {
        when(certificadoRepository.findBySearchText(eq(1L), eq("test"), any())).thenReturn(certificadoPage);

        Page<CertificadoDTO> resultado = certificadoService.findBySearchText(1L, "test", 2, 20);

        assertNotNull(resultado);
        verify(certificadoRepository, times(1)).findBySearchText(eq(1L), eq("test"), any());
    }

    @Test
    void findById_conIdValido_retornaDTOCorrecto() {
        when(certificadoRepository.findById(1L)).thenReturn(Optional.of(certificado));

        CertificadoDTO resultado = certificadoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(certificado.getId(), resultado.getId());
        assertEquals(certificado.getName(), resultado.getName());
        verify(certificadoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_conIdNoExistente_retornaNull() {
        when(certificadoRepository.findById(999L)).thenReturn(Optional.empty());

        CertificadoDTO resultado = certificadoService.findById(999L);

        assertNull(resultado);
        verify(certificadoRepository, times(1)).findById(999L);
    }

    @Test
    void findById_conIdNull_retornaNull() {
        CertificadoDTO resultado = certificadoService.findById(null);

        assertNull(resultado);
        verify(certificadoRepository, never()).findById(anyLong());
    }

    @Test
    void findById_conIdCero_retornaNull() {
        when(certificadoRepository.findById(0L)).thenReturn(Optional.empty());

        CertificadoDTO resultado = certificadoService.findById(0L);

        assertNull(resultado);
        verify(certificadoRepository, times(1)).findById(0L);
    }

    @Test
    void findById_conIdNegativo_retornaNull() {
        when(certificadoRepository.findById(-1L)).thenReturn(Optional.empty());

        CertificadoDTO resultado = certificadoService.findById(-1L);

        assertNull(resultado);
        verify(certificadoRepository, times(1)).findById(-1L);
    }

    @Test
    void save_conDTOValido_retornaDTOGuardado() {
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(certificado);

        CertificadoDTO resultado = certificadoService.save(certificadoDTO);

        assertNotNull(resultado);
        assertEquals(certificadoDTO.getName(), resultado.getName());
        verify(certificadoRepository, times(1)).save(any(Certificado.class));
    }

    @Test
    void save_conDTONull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            certificadoService.save(null);
        });

        verify(certificadoRepository, never()).save(any(Certificado.class));
    }

    @Test
    void save_conDTOConIdNull_retornaDTOGuardadoConIdGenerado() {
        certificadoDTO.setId(null);
        certificado.setId(2L);
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(certificado);

        CertificadoDTO resultado = certificadoService.save(certificadoDTO);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(certificadoRepository, times(1)).save(any(Certificado.class));
    }

    @Test
    void save_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(certificadoRepository.save(any(Certificado.class))).thenThrow(new RuntimeException("Error de base de datos"));

        assertThrows(RuntimeException.class, () -> {
            certificadoService.save(certificadoDTO);
        });

        verify(certificadoRepository, times(1)).save(any(Certificado.class));
    }

    @Test
    void delete_conIdValido_eliminaRegistro() {
        when(certificadoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(certificadoRepository).deleteById(1L);

        certificadoService.delete(1L);

        verify(certificadoRepository, times(1)).existsById(1L);
        verify(certificadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_conIdNull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            certificadoService.delete(null);
        });

        verify(certificadoRepository, never()).existsById(anyLong());
        verify(certificadoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNoExistente_lanzaIllegalArgumentException() {
        when(certificadoRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            certificadoService.delete(999L);
        });

        verify(certificadoRepository, times(1)).existsById(999L);
        verify(certificadoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdCero_lanzaIllegalArgumentException() {
        when(certificadoRepository.existsById(0L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            certificadoService.delete(0L);
        });

        verify(certificadoRepository, times(1)).existsById(0L);
        verify(certificadoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conIdNegativo_lanzaIllegalArgumentException() {
        when(certificadoRepository.existsById(-1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            certificadoService.delete(-1L);
        });

        verify(certificadoRepository, times(1)).existsById(-1L);
        verify(certificadoRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_conRepositoryLanzaExcepcion_propagaExcepcion() {
        when(certificadoRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Error de base de datos")).when(certificadoRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> {
            certificadoService.delete(1L);
        });

        verify(certificadoRepository, times(1)).existsById(1L);
        verify(certificadoRepository, times(1)).deleteById(1L);
    }

}
