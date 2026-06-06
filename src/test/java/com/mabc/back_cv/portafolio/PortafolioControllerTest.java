package com.mabc.back_cv.portafolio;

import com.mabc.back_cv.web.controllers.PortafolioController;
import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.portafolio.PortafolioService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de PortafolioController")
class PortafolioControllerTest {

    @Mock
    private PortafolioService portafolioService;

    @InjectMocks
    private PortafolioController portafolioController;

    private User userBase;
    private PortafolioDTO dtoBases;
    private Page<PortafolioDTO> pageBase;

    @BeforeEach
    void setUp() {
        userBase = new User();
        userBase.setId(1L);
        userBase.setNombre("Juan");
        userBase.setApellido("Pérez");
        userBase.setEmail("juan@example.com");
        userBase.setPassword("pass123");
        userBase.setActivo(true);

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

        pageBase = new PageImpl<>(List.of(dtoBases), PageRequest.of(0, 10), 1);
    }

    // =========================================================================
    // getPortafolioById
    // =========================================================================
    @Nested
    @DisplayName("GET /{id} — getPortafolioById")
    class GetPortafolioByIdTests {

        @Test
        @DisplayName("Éxito: retorna 200 OK con el PortafolioDTO cuando el id existe")
        void exitoRetorna200() {
            when(portafolioService.getPortafolioById(10L)).thenReturn(dtoBases);

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioById(10L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(10L, response.getBody().getId());
            verify(portafolioService).getPortafolioById(10L);
        }

        @Test
        @DisplayName("Error: retorna 404 NOT FOUND cuando el id no existe")
        void errorRetorna404() {
            when(portafolioService.getPortafolioById(99L)).thenReturn(null);

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @DisplayName("Error: retorna 500 INTERNAL_SERVER_ERROR cuando el servicio lanza excepción")
        void errorServicioLanzaExcepcion() {
            when(portafolioService.getPortafolioById(anyLong()))
                    .thenThrow(new RuntimeException("Error inesperado"));

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioById(10L);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // getPortafolioByUserId
    // =========================================================================
    @Nested
    @DisplayName("GET /user/{userId} — getPortafolioByUserId")
    class GetPortafolioByUserIdTests {

        @Test
        @DisplayName("Éxito: retorna 200 OK con el PortafolioDTO del usuario")
        void exitoRetorna200() {
            when(portafolioService.getPortafolioByUserId(1L)).thenReturn(dtoBases);

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioByUserId(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(10L, response.getBody().getId());
            verify(portafolioService).getPortafolioByUserId(1L);
        }

        @Test
        @DisplayName("Error: retorna 404 NOT FOUND cuando el usuario no tiene portafolio")
        void errorUsuarioSinPortafolioRetorna404() {
            when(portafolioService.getPortafolioByUserId(99L)).thenReturn(null);

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioByUserId(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("Error: retorna 500 INTERNAL_SERVER_ERROR cuando el servicio lanza excepción")
        void errorServicioLanzaExcepcion() {
            when(portafolioService.getPortafolioByUserId(anyLong()))
                    .thenThrow(new RuntimeException("Error de BD"));

            ResponseEntity<PortafolioDTO> response = portafolioController.getPortafolioByUserId(1L);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // searchPortafolios
    // =========================================================================
    @Nested
    @DisplayName("GET /search — searchPortafolios")
    class SearchPortafoliosTests {

        @Test
        @DisplayName("Éxito: retorna 200 OK con la página de resultados")
        void exitoRetorna200() {
            when(portafolioService.getPage(1L, "portafolio", 0, 10)).thenReturn(pageBase);

            ResponseEntity<Page<PortafolioDTO>> response =
                    portafolioController.searchPortafolios(1L, "portafolio", 0, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getTotalElements());
        }

        @Test
        @DisplayName("Éxito: retorna 200 OK con página vacía cuando no hay resultados")
        void exitoConPaginaVacia() {
            Page<PortafolioDTO> pageVacia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
            when(portafolioService.getPage(null, null, 0, 10)).thenReturn(pageVacia);

            ResponseEntity<Page<PortafolioDTO>> response =
                    portafolioController.searchPortafolios(null, null, 0, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().getContent().isEmpty());
        }

        @Test
        @DisplayName("Parámetros nulos: userId y searchText null se delegan al servicio sin error")
        void parametrosNulosSeDelegan() {
            when(portafolioService.getPage(null, null, 0, 10)).thenReturn(pageBase);

            ResponseEntity<Page<PortafolioDTO>> response =
                    portafolioController.searchPortafolios(null, null, 0, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(portafolioService).getPage(null, null, 0, 10);
        }

        @Test
        @DisplayName("Parámetros fuera de rango: page y size negativos se delegan al servicio (que los corrige)")
        void parametrosFueraDeRangoSeDelegan() {
            when(portafolioService.getPage(null, null, -1, -5)).thenReturn(pageBase);

            ResponseEntity<Page<PortafolioDTO>> response =
                    portafolioController.searchPortafolios(null, null, -1, -5);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Error: retorna 500 INTERNAL_SERVER_ERROR cuando el servicio lanza excepción")
        void errorServicioLanzaExcepcion() {
            when(portafolioService.getPage(any(), any(), anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Error de búsqueda"));

            ResponseEntity<Page<PortafolioDTO>> response =
                    portafolioController.searchPortafolios(null, null, 0, 10);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // savePortafolio
    // =========================================================================
    @Nested
    @DisplayName("POST /save — savePortafolio")
    class SavePortafolioTests {

        @Test
        @DisplayName("Éxito: retorna 200 OK con el PortafolioDTO guardado")
        void exitoRetorna200() {
            when(portafolioService.savePortafolio(dtoBases)).thenReturn(dtoBases);

            ResponseEntity<PortafolioDTO> response = portafolioController.savePortafolio(dtoBases);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(10L, response.getBody().getId());
            verify(portafolioService).savePortafolio(dtoBases);
        }

        @Test
        @DisplayName("Error: retorna 500 cuando el servicio lanza IllegalArgumentException por datos inválidos")
        void errorDatosInvalidosRetorna500() {
            when(portafolioService.savePortafolio(any()))
                    .thenThrow(new IllegalArgumentException("Datos no válidos para guardar el portafolio."));

            ResponseEntity<PortafolioDTO> response = portafolioController.savePortafolio(dtoBases);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        @DisplayName("Parámetro nulo: retorna 500 cuando el body es null y el servicio lanza excepción")
        void bodyNuloRetorna500() {
            when(portafolioService.savePortafolio(null))
                    .thenThrow(new IllegalArgumentException("Datos no válidos para guardar el portafolio."));

            ResponseEntity<PortafolioDTO> response = portafolioController.savePortafolio(null);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        @DisplayName("Error: retorna 500 cuando el repositorio lanza RuntimeException")
        void errorRepositorioLanzaExcepcion() {
            when(portafolioService.savePortafolio(any()))
                    .thenThrow(new RuntimeException("Error de base de datos"));

            ResponseEntity<PortafolioDTO> response = portafolioController.savePortafolio(dtoBases);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // deletePortafolio
    // =========================================================================
    @Nested
    @DisplayName("DELETE /delete/{id} — deletePortafolio")
    class DeletePortafolioTests {

        @Test
        @DisplayName("Éxito: retorna 200 OK con mensaje de confirmación")
        void exitoRetorna200() {
            doNothing().when(portafolioService).deletePortafolio(10L);

            ResponseEntity<String> response = portafolioController.deletePortafolio(10L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Portafolio eliminado correctamente", response.getBody());
            verify(portafolioService).deletePortafolio(10L);
        }

        @Test
        @DisplayName("Error: retorna 404 NOT FOUND con mensaje cuando el id no existe")
        void errorPortafolioNoExisteRetorna404() {
            doThrow(new IllegalArgumentException("Portafolio con id 99 no existe."))
                    .when(portafolioService).deletePortafolio(99L);

            ResponseEntity<String> response = portafolioController.deletePortafolio(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Portafolio con id 99 no existe.", response.getBody());
        }

        @Test
        @DisplayName("Error: retorna 500 cuando el repositorio lanza RuntimeException")
        void errorRepositorioLanzaExcepcion() {
            doThrow(new RuntimeException("Error de base de datos"))
                    .when(portafolioService).deletePortafolio(10L);

            ResponseEntity<String> response = portafolioController.deletePortafolio(10L);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        @DisplayName("Parámetro nulo: retorna 404 cuando el id es null y el servicio lanza IllegalArgumentException")
        void idNuloRetorna404() {
            doThrow(new IllegalArgumentException("Portafolio con id null no existe."))
                    .when(portafolioService).deletePortafolio(null);

            ResponseEntity<String> response = portafolioController.deletePortafolio(null);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
