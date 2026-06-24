package com.mabc.back_cv.permisoPantalla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.PantallaRepository;
import com.mabc.back_cv.web.repositories.PermisoPantallaRepository;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaServiceImpl;

import com.mabc.back_cv.common.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PermisoPantallaServiceImpl")
public class PermisoPantallaServiceImplTest {

    @Mock
    private PermisoPantallaRepository permisoPantallaRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PantallaRepository pantallaRepository;
    
    @Mock
    private Utils utils;


    @InjectMocks
    private PermisoPantallaServiceImpl permisoPantallaService;

    private Pageable pageable;

    private void createPageable(Integer page, Integer size, String sortBy){
        pageable = PageRequest.of(page, size, Sort.by(sortBy));
    }

    private PermisoPantalla buildPermisoPantalla(Long id, Long rolId, Long pantallaId, Boolean activo) {
        Rol rol = new Rol();
        rol.setId(rolId);

        Pantalla pantalla = new Pantalla();
        pantalla.setId(pantallaId);

        PermisoPantalla permiso = new PermisoPantalla();
        permiso.setId(id);
        permiso.setRol(rol);
        permiso.setPantalla(pantalla);
        permiso.setAccion_consultar(true);
        permiso.setAccion_crear(true);
        permiso.setAccion_editar(true);
        permiso.setAccion_eliminar(true);
        permiso.setListar(true);
        permiso.setActivo(activo);
        return permiso;
    }

    private PermisoPantallaDTO buildPermisoPantallaDTO(Long id, Long rolId, Long pantallaId, Boolean activo) {
        Rol rol = new Rol();
        rol.setId(rolId);

        Pantalla pantalla = new Pantalla();
        pantalla.setId(pantallaId);

        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(id);
        dto.setRol(rol);
        dto.setPantalla(pantalla);
        dto.setAccion_consultar(true);
        dto.setAccion_crear(true);
        dto.setAccion_editar(true);
        dto.setAccion_eliminar(true);
        dto.setActivo(activo);
        return dto;
    }

    @Test
    @DisplayName("Obtener permisos pantalla por rol - éxito")
    public void testObtenerPermisosPantallaPorRol_Success() {
        Long idRol = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(1L, idRol, 1L, true);
        List<PermisoPantalla[]> stubRolList = new ArrayList<>();
        stubRolList.add(new PermisoPantalla[] { permiso });
        when(permisoPantallaRepository.findAllPermisosByRolId(idRol)).thenReturn(stubRolList);

        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorRol(idRol);

        assertEquals(1, resultado.size());
        assertEquals(idRol, resultado.get(0).getRol().getId());
    }

    @Test
    @DisplayName("Obtener permisos pantalla por rol - error")
    public void testObtenerPermisosPantallaPorRol_Error() {
        Long idRol = 1L;
        when(permisoPantallaRepository.findAllPermisosByRolId(idRol))
                .thenThrow(new RuntimeException("Error al obtener permisos"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.obtenerPermisosPantallaPorRol(idRol));
    }

    @Test
    @DisplayName("Obtener permisos pantalla por rol - rol nulo")
    public void testObtenerPermisosPantallaPorRol_RolNulo() {
        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorRol(null);

        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Obtener permisos pantalla por rol - rol inexistente")
    public void testObtenerPermisosPantallaPorRol_RolInexistente() {
        Long idRol = 999L;
        when(permisoPantallaRepository.findAllPermisosByRolId(idRol)).thenReturn(Collections.<PermisoPantalla[]>emptyList());

        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorRol(idRol);

        assertEquals(0, resultado.size());
    }





    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - éxito")
    public void testObtenerPermisoPantallaPorRolYPantalla_Success() {
        Long idRol = 1L;
        Long idPantalla = 2L;
        PermisoPantalla permiso = buildPermisoPantalla(1L, idRol, idPantalla, true);
        when(permisoPantallaRepository.findAllByRolIdAndPantallaIdAndActivo(idRol, idPantalla, true))
                .thenReturn(List.of(permiso));

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, null);

        assertEquals(idRol, resultado.getRol().getId());
        assertEquals(idPantalla, resultado.getPantalla().getId());
    }

    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - error")
    public void testObtenerPermisoPantallaPorRolYPantalla_Error() {
        Long idRol = 1L;
        Long idPantalla = 2L;
        when(permisoPantallaRepository.findAllByRolIdAndPantallaIdAndActivo(idRol, idPantalla, true))
                .thenThrow(new RuntimeException("Error en consulta por rol y pantalla"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, null));
    }

    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - id inexistente")
    public void testObtenerPermisoPantallaPorRolYPantalla_Inexistente() {
        Long idRol = 1L;
        Long idPantalla = 2L;
        when(permisoPantallaRepository.findAllByRolIdAndPantallaIdAndActivo(idRol, idPantalla, true))
                .thenReturn(Collections.emptyList());

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, true);

        assertNull(resultado);
    }


    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - id Rol nulo")
    public void testObtenerPermisoPantallaPorRolYPantalla_RolNulo() {
        Long idRol = null;
        Long idPantalla = 2L;

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, true);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - id Pantalla nulo")
    public void testObtenerPermisoPantallaPorRolYPantalla_PantallaNula() {
        Long idRol = 1L;
        Long idPantalla = null;

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, true);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Obtener permiso pantalla por rol y pantalla - id Rol nulo y id Pantalla nulo")
    public void testObtenerPermisoPantallaPorRolYPantalla_RolYPantallaNulos() {
        Long idRol = null;
        Long idPantalla = null;

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(idRol, idPantalla, true);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Grabar permisos pantalla - éxito")
    public void testGrabarPermisosPantalla_Success() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(1L, 1L, 2L, true);
        when(rolRepository.findById(1L)).thenReturn(java.util.Optional.of(dto.getRol()));
        when(pantallaRepository.findById(2L)).thenReturn(java.util.Optional.of(dto.getPantalla()));

        int resultado = permisoPantallaService.grabarPermisosPantallaPorRol(List.of(dto));

        assertEquals(1, resultado);
        verify(permisoPantallaRepository, times(1)).save(any(PermisoPantalla.class));
    }

    @Test
    @DisplayName("Grabar permisos pantalla - lista nula")
    public void testGrabarPermisosPantalla_NullList() {
        int resultado = permisoPantallaService.grabarPermisosPantallaPorRol(null);

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Grabar permisos pantalla - lista vacía")
    public void testGrabarPermisosPantalla_EmptyList() {
        int resultado = permisoPantallaService.grabarPermisosPantallaPorRol(Collections.emptyList());

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Grabar permisos pantalla - error por rol inválido")
    public void testGrabarPermisosPantalla_RolInvalido() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(1L);
        dto.setRol(null);
        dto.setPantalla(new Pantalla());

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantallaPorRol(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por pantalla inválida")
    public void testGrabarPermisosPantalla_PantallaInvalida() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(1L);
        dto.setRol(new Rol());
        dto.setPantalla(null);

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantallaPorRol(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por pantalla inexistente")
    public void testGrabarPermisosPantalla_PantallaInexistente() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(1L, 1L, 2L, true);
        when(rolRepository.findById(1L)).thenReturn(java.util.Optional.of(dto.getRol()));
        when(pantallaRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantallaPorRol(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por Rol inexistente")
    public void testGrabarPermisosPantalla_RolInexistente() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(1L, 1L, 2L, true);
        when(rolRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        when(pantallaRepository.findById(2L)).thenReturn(java.util.Optional.of(dto.getPantalla()));

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantallaPorRol(List.of(dto)));
    }


    @Test
    @DisplayName("Grabar permisos pantalla - DTO nulo en la lista")
    public void testGrabarPermisosPantalla_dtoNulo() {
        List<PermisoPantallaDTO> dtoList = new ArrayList<PermisoPantallaDTO>();
        dtoList.add(null);

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantallaPorRol(dtoList));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por rol - éxito")
    public void testEliminarPermisosPantallaPorRolId_Success() {
        Long idRol = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(1L, idRol, 2L, true);
        when(permisoPantallaRepository.findByRolId(idRol)).thenReturn(List.of(permiso));

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorRolId(idRol);

        assertEquals(true, resultado);
        verify(permisoPantallaRepository, times(1)).deleteAll(List.of(permiso));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por rol - id inexistente")
    public void testEliminarPermisosPantallaPorRolId_Inexistente() {
        Long idRol = 999L;
        when(permisoPantallaRepository.findByRolId(idRol)).thenReturn(Collections.emptyList());

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorRolId(idRol);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por rol - error")
    public void testEliminarPermisosPantallaPorRolId_Error() {
        Long idRol = 1L;
        when(permisoPantallaRepository.findByRolId(idRol)).thenThrow(new RuntimeException("Error al eliminar por rol"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.eliminarPermisosPantallaPorRolId(idRol));
    }



    @Test
    @DisplayName("Desactivar permiso pantalla por id - éxito")
    public void testdesactivarPermisosPantallaPorRolId_Success() {
        Long id = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(id)).thenReturn(1);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorRolId(id);

        assertEquals(true, resultado);
    }

    @Test
    @DisplayName("Desactivar permiso pantalla por id - id inexistente")
    public void testdesactivarPermisosPantallaPorRolId_Inexistente() {
        Long id = 999L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(id)).thenReturn(0);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorRolId(id);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Desactivar permiso pantalla por id - error")
    public void testdesactivarPermisosPantallaPorRolId_Error() {
        Long id = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(id))
                .thenThrow(new RuntimeException("Error al desactivar por id"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.desactivarPermisosPantallaPorRolId(id));
    }


}
