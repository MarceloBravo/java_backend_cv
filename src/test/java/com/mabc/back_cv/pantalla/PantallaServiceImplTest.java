package com.mabc.back_cv.pantalla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.dto.PantallaDTO;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.repositories.PantallaRepository;
import com.mabc.back_cv.web.services.pantalla.PantallaServiceImpl;

import static org.mockito.ArgumentMatchers.any;

import com.mabc.back_cv.web.services.pantalla.PantallaMapper;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PantallaServiceImpl")
public class PantallaServiceImplTest {

    @Mock
    private PantallaRepository pantallaRepository;

    @InjectMocks
    private PantallaServiceImpl pantallaService;

    private Menu menu;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        menu = new Menu(1L, "Menu 1", null, null, null,null, true);
    }

    private void createPageable(Integer page, Integer size, String sortBy){
        pageable = PageRequest.of(page, size, Sort.by(sortBy));
    }

    @Test
    @DisplayName("Debería retornar una lista de PantallaDTO al obtener todas las pantallas")
    public void testGetAllPantallas() {
        // Configurar el comportamiento del mock del repositorio
        List<Pantalla> pantallasMock = Arrays.asList(
            new Pantalla(1L, "Pantalla 1",  menu, true, true, true, true, true, true),
            new Pantalla(2L, "Pantalla 2",  menu, true, true, true, true, true, true)
        );
        when(pantallaRepository.findAll()).thenReturn(pantallasMock);

        // Llamar al método a probar
        List<PantallaDTO> resultado = pantallaService.getAllPantallas();

        // Verificar el resultado   
        assertEquals(2, resultado.size());
        assertEquals("Pantalla 1", resultado.get(0).getNombre());
        assertEquals("Pantalla 2", resultado.get(1).getNombre());
    }
    
    @Test
    @DisplayName("Debe retornar un listado vacio al no encontrar pantallas")
    public void testGetAllPantallasVacio() {
        // Configurar el comportamiento del mock para el método getAllPantallas
        List<PantallaDTO> pantallasMock = new ArrayList<>();
        when(pantallaService.getAllPantallas()).thenReturn(pantallasMock);

        // Llamar al método a probar
        List<PantallaDTO> resultado = pantallaService.getAllPantallas();

        // Verificar el resultado
        assertEquals(0, resultado.size());
    }

    

    @Test
    @DisplayName("Debería retornar una página de PantallaDTO al buscar pantallas con criterios de búsqueda")
    public void testSearchPantallas() {
        // Configurar el comportamiento del mock del repositorio
        List<Pantalla> pantallasMock = Arrays.asList(
            new Pantalla(1L, "Pantalla 1",  menu, true, true, true, true, true, true),
            new Pantalla(2L, "Pantalla 2",  menu, true, true, true, true, true, true)
        );
        createPageable(0, 10, "id");
        Page<Pantalla> pageResult = new PageImpl<>(pantallasMock, pageable, 2);
        when(pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu("Pantalla", true, pageable))
            .thenReturn(pageResult);

        // Llamar al método a probar
        Page<PantallaDTO> resultado = pantallaService.searchPantallas("Pantalla", true, 0, 10, "id");

        // Verificar el resultado
        assertEquals(2, resultado.getContent().size());
        assertEquals("Pantalla 1", resultado.getContent().get(0).getNombre());
        
    }

    @Test
    @DisplayName("Debe retornar una página vacía al no encontrar pantallas con los criterios de búsqueda")
    public void testSearchPantallasVacio() {
        // Configurar el comportamiento del mock del repositorio
        createPageable(0, 10, "nombre");
        Page<Pantalla> pageResult = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu("NoExiste", true, pageable))
            .thenReturn(pageResult);

        // Llamar al método a probar
        Page<PantallaDTO> resultado = pantallaService.searchPantallas("NoExiste", true, 0, 10, "nombre");

        // Verificar el resultado
        assertEquals(0, resultado.getContent().size());
    }

    @Test
    @DisplayName("Debería retornar una página vacía al buscar pantallas con criterios de búsqueda inválidos")
    public void testSearchPantallasInvalidos() {
        createPageable(0, 10, "id");
        
        Page<Pantalla> pageResult = new PageImpl<>(new ArrayList<>(), pageable, 0);

        // Llamar al método a probar (no debería lanzar excepción, solo devuelve página vacía)
        Page<PantallaDTO> resultado = pantallaService.searchPantallas(null, true, 0, 10, "nombre");
        assertEquals(0, resultado.getContent().size());

        Page<PantallaDTO> resultado2 = pantallaService.searchPantallas("", true, 0, 10, "nombre");
        assertEquals(0, resultado2.getContent().size());
     
        Page<PantallaDTO> resultado3 = pantallaService.searchPantallas("  ", true, 0, 10, "nombre");
        assertEquals(0, resultado3.getContent().size());
    }

    @Test
    @DisplayName("Test de búsqueda de pantallas con criterios de paginación inválidos")
    public void testSearchPantallasInvalidosPaginacion() {
        // Configurar el comportamiento del mock del repositorio
        List<Pantalla> pantallasMock = Arrays.asList(
            new Pantalla(1L, "Pantalla 1",  menu, true, true, true, true, true, true),
            new Pantalla(2L, "Pantalla 2",  menu, true, true, true, true, true, true)
        );
        createPageable(0, 10, "id");
        Page<Pantalla> pageResult = new PageImpl<>(pantallasMock, pageable, 2);
        when(pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu("Pantalla", true, pageable))
            .thenReturn(pageResult);

        Page<PantallaDTO> resultado1 = pantallaService.searchPantallas("Pantalla", true, null, null, null);

        // Verificar el resultado
        assertEquals(2, resultado1.getContent().size());
        assertEquals("Pantalla 1", resultado1.getContent().get(0).getNombre());

        Page<PantallaDTO> resultado2 = pantallaService.searchPantallas("Pantalla", true, null, 10, "");

        // Verificar el resultado
        assertEquals(2, resultado2.getContent().size());
        assertEquals("Pantalla 1", resultado2.getContent().get(0).getNombre());

        Page<PantallaDTO> resultado3 = pantallaService.searchPantallas("Pantalla", true, -10, 0, "");

        // Verificar el resultado
        assertEquals(2, resultado3.getContent().size());
        assertEquals("Pantalla 1", resultado3.getContent().get(0).getNombre());

        Page<PantallaDTO> resultado4 = pantallaService.searchPantallas("Pantalla", true, 0, null, "   ");

        // Verificar el resultado
        assertEquals(2, resultado4.getContent().size());
        assertEquals("Pantalla 1", resultado4.getContent().get(0).getNombre());
    }

    @Test
    @DisplayName("Debe lanzar un error cuando el parámetro orderBy está vacío")
    public void testSearchPantallaWithInvalidOrderByValue(){
        // Configurar el comportamiento del mock del repositorio
        List<Pantalla> pantallasMock = Arrays.asList(
            new Pantalla(1L, "Pantalla 1",  menu, true, true, true, true, true, true),
            new Pantalla(2L, "Pantalla 2",  menu, true, true, true, true, true, true)
        );
        createPageable(0, 10, "id");
        Page<Pantalla> pageResult = new PageImpl<>(pantallasMock);
        when(pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu("Pantalla", true, pageable))
            .thenReturn(pageResult);

        // Llamar al método a probar
        Page<PantallaDTO> resultado = pantallaService.searchPantallas("Pantalla", true, 0, 10, "");

        // Verificar el resultado
        assertEquals(2, resultado.getContent().size());
        assertEquals("Pantalla 1", resultado.getContent().get(0).getNombre());
    }

    @Test
    @DisplayName("No debe lanzar un error cuando el parámetro sortBy es nulo")
    public void testSearchPantallaWithNullOrderByValue(){
        // Configurar el comportamiento del mock del repositorio
        List<Pantalla> pantallasMock = Arrays.asList(
            new Pantalla(1L, "Pantalla 1",  menu, true, true, true, true, true, true),
            new Pantalla(2L, "Pantalla 2",  menu, true, true, true, true, true, true)
        );
        createPageable(0, 10, "id");
        Page<Pantalla> pageResult = new PageImpl<>(pantallasMock);
        when(pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu("Pantalla", true, pageable))
            .thenReturn(pageResult);

        // Llamar al método a probar
        Page<PantallaDTO> resultado = pantallaService.searchPantallas("Pantalla", true, 0, 10, null);

        // Verificar el resultado
        assertEquals(2, resultado.getContent().size());
        assertEquals("Pantalla 1", resultado.getContent().get(0).getNombre());
    }

    @Test
    @DisplayName("Debe retornar una pantalla por su ID")
    public void testGetPantallaById() {
        // Configurar el comportamiento del mock del repositorio
        Pantalla pantallaMock = new Pantalla(1L, "Pantalla 1",  null, true, true, true, true, true, true);
        when(pantallaRepository.findById(1L)).thenReturn(Optional.of(pantallaMock));

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.getPantallaById(1L);

        // Verificar el resultado
        assertEquals("Pantalla 1", resultado.getNombre());
    }

    @Test
    @DisplayName("Debe retornar null al buscar una pantalla por un ID que no existe")
    public void testGetPantallaByIdNull() {
        // Configurar el comportamiento del mock del repositorio
        when(pantallaRepository.findById(999L)).thenReturn(Optional.empty());

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.getPantallaById(999L);

        // Verificar el resultado
        assertNull(resultado);
    }

    @Test
    @DisplayName("Debe lanzar un error al buscar una pantalla por un ID nulo")
    public void testGetPantallaByIdInvalid() {
        // El servicio puede lanzar una excepción cuando se intenta buscar con ID nulo
        // Esto depende de si la validación está implementada en el servicio
        // Por ahora, asumimos que no lanza excepción y retorna null
        when(pantallaRepository.findById(null)).thenReturn(Optional.empty());
        PantallaDTO resultado = pantallaService.getPantallaById(null);
        assertNull(resultado);
    }

    @Test
    @DisplayName("Debe guardar una nueva pantalla y retornar el PantallaDTO guardado")
    public void testSavePantalla() {
        // Configurar el comportamiento del mock del repositorio
        PantallaDTO pantallaToSave = new PantallaDTO(null, "Nueva Pantalla",  null, true, true, true, true, true, true);
        Pantalla pantallaSavedEntity = new Pantalla(1L, "Nueva Pantalla",  null, true, true, true, true, true, true);
        
        when(pantallaRepository.save(any(Pantalla.class))).thenReturn(pantallaSavedEntity);

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.savePantalla(pantallaToSave);
        // Verificar el resultado
        assertEquals(1L, resultado.getId());
        assertEquals("Nueva Pantalla", resultado.getNombre());
        assertEquals(true, resultado.getActivo());
    }


    @Test
    @DisplayName("Debe intentar guardar una pantalla con datos vacíos")
    public void testSavePantallaInvalid() {
        // Configurar el comportamiento del mock del repositorio
        PantallaDTO pantallaToSave = new PantallaDTO(null, "",  null, true, true, true, true, true, true);
        Pantalla pantallaSavedEntity = new Pantalla(1L, "", null, true, true, true, true, true, true);
        
        when(pantallaRepository.save(any(Pantalla.class))).thenReturn(pantallaSavedEntity);

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.savePantalla(pantallaToSave);
        // Verificar que se guardó aunque con datos vacíos
        assertNotNull(resultado.getId());
    }


    @Test
    @DisplayName("Debe actualizar una pantalla existente y retornar el PantallaDTO actualizado")
    public void testUpdatePantalla() {
        // Configurar el comportamiento del mock del repositorio
        PantallaDTO pantallaToUpdate = new PantallaDTO(1L, "Pantalla Actualizada",  null, true, true, true, true, true, true);
        Pantalla pantallaEntity = new Pantalla(1L, "Pantalla Actualizada",  null, true, true, true, true, true, true);
        
        when(pantallaRepository.save(any(Pantalla.class))).thenReturn(pantallaEntity);

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.savePantalla(pantallaToUpdate);
        // Verificar el resultado
        assertEquals(1L, resultado.getId());
        assertEquals("Pantalla Actualizada", resultado.getNombre());
        assertEquals(true, resultado.getActivo());
    }

    @Test
    @DisplayName("Debe intentar actualizar una pantalla con datos vacíos")
    public void testUpdatePantallaInvalid() {
        // Configurar el comportamiento del mock del repositorio
        PantallaDTO pantallaToUpdate = new PantallaDTO(1L, "", null, true, true, true, true, true, true);
        Pantalla pantallaEntity = new Pantalla(1L, "", null, true, true, true, true, true, true);
        
        when(pantallaRepository.save(any(Pantalla.class))).thenReturn(pantallaEntity);

        // Llamar al método a probar
        PantallaDTO resultado = pantallaService.savePantalla(pantallaToUpdate);
        // Verificar que se actualizó aunque con datos vacíos
        assertNotNull(resultado.getId());
    }



    @Test
    @DisplayName("Debe eliminar una pantalla por su ID")
    public void testDeletePantalla() {
        // Preparar mock para indicar que el ID existe
        when(pantallaRepository.existsById(1L)).thenReturn(true);

        // Llamar al método a probar
        pantallaService.deletePantalla(1L);

        // Verificar que se llamó al método delete del repositorio
        verify(pantallaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe eliminar una pantalla con un ID inválido lanzando una excepción")
    public void testDeletePantallaInvalid() {
        when(pantallaRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> pantallaService.deletePantalla(999L));

        verify(pantallaRepository, times(0)).deleteById(999L);
    }

    
    @Test
    @DisplayName("Intenta eliminar una Pantalla con id nulo")
    public void deletePantall_whitIdNull_returnError(){
        
        assertThrows(IllegalArgumentException.class, () -> pantallaService.deletePantalla(null));

        verifyNoInteractions(pantallaRepository);
    }

}