package com.mabc.back_cv.pantalla;

import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.dto.PantallaDTO;
import com.mabc.back_cv.web.services.pantalla.PantallaService;
import com.mabc.back_cv.web.controllers.PantallaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PantallaController")
public class PantallaControllerTest {

    private Pantalla pantalla1;
    private Pantalla pantalla2;

    @Mock
    private PantallaService pantallaService;

    @InjectMocks
    private PantallaController pantallaController;

    @BeforeEach
    public void setUp() {
        // Configurar datos de prueba
        pantalla1 = new Pantalla();
        pantalla1.setId(1L);
        pantalla1.setNombre_pantalla("Pantalla 1");
        pantalla1.setAccion_crear(true);
        pantalla1.setAccion_editar(true);
        pantalla1.setAccion_eliminar(true);
        pantalla1.setAccion_consultar(true);

        pantalla2 = new Pantalla();
        pantalla2.setId(2L);
        pantalla2.setNombre_pantalla("Pantalla 2");
        pantalla2.setAccion_crear(true);
        pantalla2.setAccion_editar(true);
        pantalla2.setAccion_eliminar(true);
        pantalla2.setAccion_consultar(true);
    }

    @Test
    @DisplayName("Test para obtener todas las pantallas")
    public void testGetAllPantallas() {

        List<PantallaDTO> pantallasDTOs = Arrays.asList(
            new PantallaDTO(1L, "Pantalla 1", null, true, true, true, true, true, false),
            new PantallaDTO(2L, "Pantalla 2", null, true, true, true, true, true, false)
        );
        when(pantallaService.getAllPantallas()).thenReturn(pantallasDTOs);

        // Ejecutar el método a probar
        ResponseEntity<List<PantallaDTO>> resultado = pantallaController.getAllPantallas();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().size());
        assertEquals("Pantalla 1", resultado.getBody().get(0).getNombre());
        assertEquals("Pantalla 2", resultado.getBody().get(1).getNombre());
    }

    @Test
    @DisplayName("Test para capturar los errores al obtener todas las pantallas")
    public void testGetAllPantallasError() {
        RuntimeException exception = new RuntimeException("Error al obtener las pantallas");
        when(pantallaService.getAllPantallas()).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<List<PantallaDTO>> resultado = pantallaController.getAllPantallas();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para obtener todas las pantallas cuando no hay pantallas registradas")
    public void testGetAllPantallasWhenNoPantallasRegistered() {

        when(pantallaService.getAllPantallas()).thenReturn(Collections.emptyList());

        // Ejecutar el método a probar
        ResponseEntity<List<PantallaDTO>> resultado = pantallaController.getAllPantallas();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().size());
    }

    @Test
    @DisplayName("Test para capturar los errores alobtener todas las pantallas cuando no hay pantallas registradas")
    public void testGetAllPantallasWhenNoPantallasRegisteredError() {
        RuntimeException exception = new RuntimeException("Error al obtener las pantallas");
        when(pantallaService.getAllPantallas()).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<List<PantallaDTO>> resultado = pantallaController.getAllPantallas();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para buscar pantallas con criterios de búsqueda")
    public void testSearchPantallas() {
        // Configurar el comportamiento del mock del servicio   
        List<PantallaDTO> pantallasDTOs = Arrays.asList(
            new PantallaDTO(1L, "Pantalla 1", null, true, true, true, true, true, false),
            new PantallaDTO(2L, "Pantalla 2", null, true, true, true, true, true, false)
        );
        when(pantallaService.searchPantallas("Pantalla", null, 0, 10, "id")).thenReturn(new PageImpl<>(pantallasDTOs));

        // Ejecutar el método a probar        
        ResponseEntity<Page<PantallaDTO>> resultado = pantallaController.searchPantallas("Pantalla", null, 0, 10, "id");
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().getContent().size());
        assertEquals("Pantalla 1", resultado.getBody().getContent().get(0).getNombre());
        assertEquals("Pantalla 2", resultado.getBody().getContent().get(1).getNombre());
    }

    @Test
    @DisplayName("Test para capturar los errores al buscar pantallas con criterios de búsqueda")
    public void testSearchPantallasError() {
        RuntimeException exception = new RuntimeException("Error al buscar pantallas");
        when(pantallaService.searchPantallas("Pantalla", null, 0, 10, "id")).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<Page<PantallaDTO>> resultado = pantallaController.searchPantallas("Pantalla", null, 0, 10, "id");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para buscar pantallas con criterios de búsqueda NO válidos")
    public void testSearchPantallasWhenNoResultsFound() {
        when(pantallaService.searchPantallas("Pantalla", null, 0, 10, "id")).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Ejecutar el método a probar
        ResponseEntity<Page<PantallaDTO>> resultado = pantallaController.searchPantallas("Pantalla", null, 0, 10, "id");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().getContent().size());
    }

    @Test
    @DisplayName("Test para capturar errores al buscar pantallas con criterios de búsqueda NO válidos")
    public void testSearchPantallasWhenNoResultsFoundError() {
        RuntimeException exception = new RuntimeException("Error al buscar pantallas");
        when(pantallaService.searchPantallas("Pantalla", null, 0, 10, "id")).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<Page<PantallaDTO>> resultado = pantallaController.searchPantallas("Pantalla", null, 0, 10, "id");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para obtener una pantalla por ID")
    public void testGetPantallaById() {
        PantallaDTO pantallaDTO = new PantallaDTO(1L, "Pantalla 1", null, true, true, true, true, true, false);
        when(pantallaService.getPantallaById(1L)).thenReturn(pantallaDTO);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.getPantallaById(1L);

        assertNotNull(resultado);
        assertEquals("Pantalla 1", resultado.getBody().getNombre());
    }

    @Test
    @DisplayName("Test para capturar los erroers al obtener una pantalla por ID")
    public void testGetPantallaByIdError() {
        RuntimeException exception = new RuntimeException("Error al obtener la pantalla");
        when(pantallaService.getPantallaById(1L)).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.getPantallaById(1L);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para obtener una pantalla por ID cuando la pantalla no existe")
    public void testGetPantallaByIdWhenPantallaDoesNotExist() {
        when(pantallaService.getPantallaById(999L)).thenReturn(null);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.getPantallaById(999L);

        assertNull(resultado.getBody());
        assertEquals(404, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para capturar los errores al obtener una pantalla por ID cuando la pantalla no existe")
    public void testGetPantallaByIdWhenPantallaDoesNotExistError() {
        RuntimeException exception = new RuntimeException("Error al obtener la pantalla");
        when(pantallaService.getPantallaById(999L)).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.getPantallaById(999L);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para guardar una nueva pantalla")
    public void testSavePantalla() {
        PantallaDTO pantallaNuevaDTO = new PantallaDTO(null, "Pantalla 1", null, true, true, true, true, true, false);
        PantallaDTO pantallaGuardadaDTO = new PantallaDTO(3L, "Pantalla 1", null, true, true, true, true, true, false);

        when(pantallaService.savePantalla(pantallaNuevaDTO)).thenReturn(pantallaGuardadaDTO);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.savePantalla(pantallaNuevaDTO);

        assertNotNull(resultado.getBody());
        assertEquals("Pantalla 1", resultado.getBody().getNombre());
        assertEquals(3L, resultado.getBody().getId());
    }

    @Test
    @DisplayName("Test para capturar los errores al guardar una nueva pantalla")
    public void testSavePantallaError() {
        RuntimeException exception = new RuntimeException("Error al guardar la pantalla");
        PantallaDTO pantallaNuevaDTO = new PantallaDTO(null, "Pantalla 1", null, true, true, true, true, true, false);
        when(pantallaService.savePantalla(pantallaNuevaDTO)).thenThrow(exception);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.savePantalla(pantallaNuevaDTO);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
    }

    @Test
    @DisplayName("Test para actualizar una pantalla")
    public void testUpdatePantalla() {
        PantallaDTO pantallaActualizadaDTO = new PantallaDTO(1L, "Pantalla Actualizada", null, true, true, true, true, true, false);

        when(pantallaService.savePantalla(pantallaActualizadaDTO)).thenReturn(pantallaActualizadaDTO);

        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.savePantalla(pantallaActualizadaDTO);

        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("Pantalla Actualizada", resultado.getBody().getNombre());
    }

    @Test
    @DisplayName("Test para capoturar los errores al  actualizar una pantalla")
    public void testUpdatePantallaError() {
        RuntimeException exception = new RuntimeException("Error al actualizar la pantalla");
        
        PantallaDTO pantallaActualizadaDTO = new PantallaDTO(1L, "Pantalla Actualizada", null, true, true, true, true, true, false);
        when(pantallaService.savePantalla(pantallaActualizadaDTO)).thenThrow(exception);
        // Ejecutar el método a probar
        ResponseEntity<PantallaDTO> resultado = pantallaController.savePantalla(pantallaActualizadaDTO);
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(500, resultado.getStatusCodeValue());
        assertEquals(null, resultado.getBody());
    }

    @Test
    @DisplayName("Test para eliminar una pantalla")
    public void testDeletePantalla() {
        doNothing().when(pantallaService).deletePantalla(1L);

        // Ejecutar el método a probar
        ResponseEntity<String> resultado = pantallaController.deletePantalla(1L);

        // Verificar que se llamó al método deletePantalla con el ID correcto
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Pantalla eliminada correctamente", resultado.getBody());
        verify(pantallaService, times(1)).deletePantalla(1L);
    }


    @Test
    @DisplayName("Test para evaluar un ERROR al eliminar una pantalla")
    public void testDeletePantallaError() {
        RuntimeException exception = new RuntimeException("Error al eliminar la pantalla");
        doThrow(exception).when(pantallaService).deletePantalla(any(Long.class));

        // Ejecutar el método a probar
        ResponseEntity<String> resultado = pantallaController.deletePantalla(1L);

        // Verificar que se llamó al método deletePantalla con el ID correcto
        assertEquals(500, resultado.getStatusCodeValue());
        assertEquals("Error al eliminar la pantalla", resultado.getBody());
        verify(pantallaService, times(1)).deletePantalla(1L);
    }

}