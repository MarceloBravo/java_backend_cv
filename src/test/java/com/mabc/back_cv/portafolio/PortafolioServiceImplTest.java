package com.mabc.back_cv.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.PortafolioRepository;
import com.mabc.back_cv.web.services.portafolio.PortafolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de PortafolioServiceImpl")
class PortafolioServiceImplTest {

    @Mock
    private PortafolioRepository portafolioRepository;

    @InjectMocks
    private PortafolioServiceImpl service;

    private User userBase;
    private Portafolio portafolioBase;
    private PortafolioDTO dtoBases;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        userBase = new User();
        userBase.setId(1L);
        userBase.setNombre("Juan");
        userBase.setApellido("Pérez");
        userBase.setEmail("juan@example.com");
        userBase.setPassword("pass123");
        userBase.setActivo(true);

        portafolioBase = new Portafolio();
        portafolioBase.setId(10L);
        portafolioBase.setTitle("Mi Portafolio");
        portafolioBase.setImage("imagen.png");
        portafolioBase.setVideo("video.mp4");
        portafolioBase.setMouseMoveTitle("Hover título");
        portafolioBase.setMouseMoveDescription("Hover descripción");
        portafolioBase.setParagraph("Párrafo inferior");
        portafolioBase.setLink("https://ejemplo.com");
        portafolioBase.setUser(userBase);
        portafolioBase.setDescription(new ArrayList<>());

        dtoBases = new PortafolioDTO();
        dtoBases.setId(10L);
        dtoBases.setTitle("Mi Portafolio");
        dtoBases.setImage("imagen.png");
        dtoBases.setVideo("video.mp4");
        dtoBases.setMouseMoveTitle("Hover título");
        dtoBases.setMouseMoveDescription("Hover descripción");
        dtoBases.setParagraph("Párrafo inferior");
        dtoBases.setLink("https://ejemplo.com");
        dtoBases.setUser(userBase);

        pageable = PageRequest.of(0, 10);
    }

    // =========================================================================
    // getPortafolioById
    // =========================================================================
    @Nested
    @DisplayName("getPortafolioById")
    class GetPortafolioByIdTests {

        @Test
        @DisplayName("Éxito: retorna PortafolioDTO cuando el id existe")
        void exitoCuandoIdExiste() {
            when(portafolioRepository.findById(10L)).thenReturn(Optional.of(portafolioBase));

            PortafolioDTO result = service.getPortafolioById(10L);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Mi Portafolio", result.getTitle());
            verify(portafolioRepository).findById(10L);
        }

        @Test
        @DisplayName("Error: retorna null cuando el id no existe en la base de datos")
        void errorCuandoIdNoExiste() {
            when(portafolioRepository.findById(99L)).thenReturn(Optional.empty());

            PortafolioDTO result = service.getPortafolioById(99L);

            assertNull(result);
            verify(portafolioRepository).findById(99L);
        }

        @Test
        @DisplayName("Parámetro nulo: retorna null sin invocar el repositorio")
        void idNuloRetornaNull() {
            PortafolioDTO result = service.getPortafolioById(null);

            assertNull(result);
            verifyNoInteractions(portafolioRepository);
        }
    }

    // =========================================================================
    // getPortafolioByUserId
    // =========================================================================
    @Nested
    @DisplayName("getPortafolioByUserId")
    class GetPortafolioByUserIdTests {

        @Test
        @DisplayName("Éxito: retorna PortafolioDTO cuando el userId tiene portafolio asociado")
        void exitoCuandoUserIdExiste() {
            when(portafolioRepository.findByUserId(1L)).thenReturn(portafolioBase);

            PortafolioDTO result = service.getPortafolioByUserId(1L);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            verify(portafolioRepository).findByUserId(1L);
        }

        @Test
        @DisplayName("Error: retorna null cuando el usuario no tiene portafolio asociado")
        void errorCuandoUsuarioSinPortafolio() {
            when(portafolioRepository.findByUserId(99L)).thenReturn(null);

            PortafolioDTO result = service.getPortafolioByUserId(99L);

            assertNull(result);
            verify(portafolioRepository).findByUserId(99L);
        }

        @Test
        @DisplayName("Parámetro nulo: retorna null sin invocar el repositorio")
        void userIdNuloRetornaNull() {
            PortafolioDTO result = service.getPortafolioByUserId(null);

            assertNull(result);
            verifyNoInteractions(portafolioRepository);
        }
    }

    // =========================================================================
    // getPage
    // =========================================================================
    @Nested
    @DisplayName("getPage")
    class GetPageTests {

        @Test
        @DisplayName("Éxito: retorna página de DTOs con parámetros válidos")
        void exitoConParametrosValidos() {
            Page<Portafolio> pageEntidad = new PageImpl<>(List.of(portafolioBase), pageable, 1);
            when(portafolioRepository.findBySearchText(eq(1L), eq("portafolio"), any(Pageable.class)))
                    .thenReturn(pageEntidad);

            Page<PortafolioDTO> result = service.getPage(1L, "portafolio", 0, 10);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("Mi Portafolio", result.getContent().get(0).getTitle());
        }

        @Test
        @DisplayName("Éxito: retorna página vacía cuando no hay resultados")
        void exitoConPaginaVacia() {
            Page<Portafolio> pageVacia = new PageImpl<>(List.of(), pageable, 0);
            when(portafolioRepository.findBySearchText(isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(pageVacia);

            Page<PortafolioDTO> result = service.getPage(null, null, 0, 10);

            assertNotNull(result);
            assertTrue(result.getContent().isEmpty());
        }

        @Test
        @DisplayName("Parámetros nulos: page y size null usan valores por defecto (0 y 10)")
        void pageYSizeNulosUsanDefecto() {
            Page<Portafolio> pageEntidad = new PageImpl<>(List.of(portafolioBase), pageable, 1);
            when(portafolioRepository.findBySearchText(isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(pageEntidad);

            Page<PortafolioDTO> result = service.getPage(null, null, null, null);

            assertNotNull(result);
            verify(portafolioRepository).findBySearchText(isNull(), isNull(),
                    argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 10));
        }

        @Test
        @DisplayName("Parámetro fuera de rango: page negativo se corrige a 0")
        void pageNegativaSeCorrigeACero() {
            Page<Portafolio> pageEntidad = new PageImpl<>(List.of(portafolioBase), pageable, 1);
            when(portafolioRepository.findBySearchText(any(), any(), any(Pageable.class)))
                    .thenReturn(pageEntidad);

            service.getPage(null, null, -3, 10);

            verify(portafolioRepository).findBySearchText(any(), any(),
                    argThat(p -> p.getPageNumber() == 0));
        }

        @Test
        @DisplayName("Parámetro fuera de rango: size=0 se corrige a 10")
        void sizeCeroSeCorrigeADiez() {
            Page<Portafolio> pageEntidad = new PageImpl<>(List.of(portafolioBase), pageable, 1);
            when(portafolioRepository.findBySearchText(any(), any(), any(Pageable.class)))
                    .thenReturn(pageEntidad);

            service.getPage(null, null, 0, 0);

            verify(portafolioRepository).findBySearchText(any(), any(),
                    argThat(p -> p.getPageSize() == 10));
        }

        @Test
        @DisplayName("Éxito: los DTOs de la página contienen los datos correctos de la entidad")
        void dtosDeLaPaginaContienenDatosCorrectos() {
            Portafolio p2 = new Portafolio();
            p2.setId(20L);
            p2.setTitle("Segundo Portafolio");
            p2.setUser(userBase);
            p2.setDescription(new ArrayList<>());

            Page<Portafolio> pageEntidad = new PageImpl<>(List.of(portafolioBase, p2), pageable, 2);
            when(portafolioRepository.findBySearchText(any(), any(), any(Pageable.class)))
                    .thenReturn(pageEntidad);

            Page<PortafolioDTO> result = service.getPage(null, null, 0, 10);

            assertEquals(2, result.getTotalElements());
            assertEquals(10L, result.getContent().get(0).getId());
            assertEquals(20L, result.getContent().get(1).getId());
        }
    }

    // =========================================================================
    // savePortafolio
    // =========================================================================
    @Nested
    @DisplayName("savePortafolio")
    class SavePortafolioTests {

        @Test
        @DisplayName("Éxito: guarda y retorna PortafolioDTO cuando los datos son válidos")
        void exitoGuardaPortafolio() {
            when(portafolioRepository.save(any(Portafolio.class))).thenReturn(portafolioBase);

            PortafolioDTO result = service.savePortafolio(dtoBases);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Mi Portafolio", result.getTitle());
            verify(portafolioRepository).save(any(Portafolio.class));
        }

        @Test
        @DisplayName("Éxito: crea portafolio sin ID (nuevo registro)")
        void exitoCreaPortafolioNuevo() {
            dtoBases.setId(null);
            Portafolio guardado = new Portafolio();
            guardado.setId(99L);
            guardado.setTitle("Mi Portafolio");
            guardado.setUser(userBase);
            guardado.setDescription(new ArrayList<>());

            when(portafolioRepository.save(any(Portafolio.class))).thenReturn(guardado);

            PortafolioDTO result = service.savePortafolio(dtoBases);

            assertNotNull(result);
            assertEquals(99L, result.getId());
        }

        @Test
        @DisplayName("Error: lanza IllegalArgumentException cuando el DTO es null")
        void errorDTONulo() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.savePortafolio(null));

            assertEquals("Datos no válidos para guardar el portafolio.", ex.getMessage());
            verifyNoInteractions(portafolioRepository);
        }

        @Test
        @DisplayName("Error: lanza IllegalArgumentException cuando el user del DTO es null")
        void errorUserNuloEnDTO() {
            dtoBases.setUser(null);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.savePortafolio(dtoBases));

            assertEquals("Datos no válidos para guardar el portafolio.", ex.getMessage());
            verifyNoInteractions(portafolioRepository);
        }

        @Test
        @DisplayName("Error: lanza excepción cuando el repositorio falla al guardar")
        void errorRepositorioFallaAlGuardar() {
            when(portafolioRepository.save(any(Portafolio.class)))
                    .thenThrow(new RuntimeException("Error de base de datos"));

            assertThrows(RuntimeException.class, () -> service.savePortafolio(dtoBases));
        }
    }

    // =========================================================================
    // deletePortafolio
    // =========================================================================
    @Nested
    @DisplayName("deletePortafolio")
    class DeletePortafolioTests {

        @Test
        @DisplayName("Éxito: elimina el portafolio cuando el id existe")
        void exitoEliminaPortafolio() {
            when(portafolioRepository.existsById(10L)).thenReturn(true);
            doNothing().when(portafolioRepository).deleteById(10L);

            assertDoesNotThrow(() -> service.deletePortafolio(10L));

            verify(portafolioRepository).existsById(10L);
            verify(portafolioRepository).deleteById(10L);
        }

        @Test
        @DisplayName("Error: lanza IllegalArgumentException cuando el id no existe en la base de datos")
        void errorPortafolioNoExiste() {
            when(portafolioRepository.existsById(99L)).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.deletePortafolio(99L));

            assertTrue(ex.getMessage().contains("99"));
            verify(portafolioRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Parámetro nulo: lanza IllegalArgumentException cuando el id es null")
        void idNuloLanzaExcepcion() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.deletePortafolio(null));

            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("Error: no invoca deleteById cuando el portafolio no existe")
        void noInvocaDeleteCuandoNoExiste() {
            when(portafolioRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> service.deletePortafolio(5L));

            verify(portafolioRepository, never()).deleteById(anyLong());
        }
    }
}
