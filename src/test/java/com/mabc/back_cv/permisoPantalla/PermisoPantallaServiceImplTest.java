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

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PermisoPantallaServiceImpl")
public class PermisoPantallaServiceImplTest {

    @Mock
    private PermisoPantallaRepository permisoPantallaRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PantallaRepository pantallaRepository;

    @InjectMocks
    private PermisoPantallaServiceImpl permisoPantallaService;

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
    @DisplayName("Obtener permisos pantalla por pantalla - éxito")
    public void testObtenerPermisosPantallaPorPantalla_Success() {
        Long idPantalla = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(1L, 1L, idPantalla, true);
        List<PermisoPantalla[]> stubPantallaList = new ArrayList<>();
        stubPantallaList.add(new PermisoPantalla[] { permiso });
        when(permisoPantallaRepository.findAllPermisosByPantallaId(idPantalla)).thenReturn(stubPantallaList);

        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorPantalla(idPantalla);

        assertEquals(1, resultado.size());
        assertEquals(idPantalla, resultado.get(0).getPantalla().getId());
    }

    @Test
    @DisplayName("Obtener permisos pantalla por pantalla - error")
    public void testObtenerPermisosPantallaPorPantalla_Error() {
        Long idPantalla = 1L;
        when(permisoPantallaRepository.findAllPermisosByPantallaId(idPantalla))
            .thenThrow(new RuntimeException("Error al obtener permisos"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.obtenerPermisosPantallaPorPantalla(idPantalla));
    }

    @Test
    @DisplayName("Obtener permisos pantalla por pantalla - pantalla nula")
    public void testObtenerPermisosPantallaPorPantalla_PantallaNula() {
        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorPantalla(null);

        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Obtener permisos pantalla por pantalla - pantalla inexistente")
    public void testObtenerPermisosPantallaPorPantalla_PantallaInexistente() {
        Long idPantalla = 999L;
        when(permisoPantallaRepository.findAllPermisosByPantallaId(idPantalla)).thenReturn(Collections.<PermisoPantalla[]>emptyList());

        List<PermisoPantallaDTO> resultado = permisoPantallaService.obtenerPermisosPantallaPorPantalla(idPantalla);

        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Obtener permiso pantalla por id - éxito")
    public void testObtenerPermisoPantallaPorId_Success() {
        Long id = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(id, 1L, 1L, true);
        when(permisoPantallaRepository.findById(id)).thenReturn(java.util.Optional.of(permiso));

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorId(id);

        assertEquals(id, resultado.getId());
    }

    @Test
    @DisplayName("Obtener permiso pantalla por id - error")
    public void testObtenerPermisoPantallaPorId_Error() {
        Long id = 1L;
        when(permisoPantallaRepository.findById(id))
                .thenThrow(new RuntimeException("Error en consulta por id"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.obtenerPermisoPantallaPorId(id));
    }

    @Test
    @DisplayName("Obtener permiso pantalla por id - id inexistente")
    public void testObtenerPermisoPantallaPorId_Inexistente() {
        Long id = 999L;
        when(permisoPantallaRepository.findById(id)).thenReturn(java.util.Optional.empty());

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorId(id);

        assertNull(resultado);
    }
    
    @Test
    @DisplayName("Obtener permiso pantalla por id - id nulo")
    public void testObtenerPermisoPantallaPorId_nulo() {
        Long id = null;

        PermisoPantallaDTO resultado = permisoPantallaService.obtenerPermisoPantallaPorId(id);

        assertNull(resultado);
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

        int resultado = permisoPantallaService.grabarPermisosPantalla(List.of(dto));

        assertEquals(1, resultado);
        verify(permisoPantallaRepository, times(1)).save(any(PermisoPantalla.class));
    }

    @Test
    @DisplayName("Grabar permisos pantalla - lista nula")
    public void testGrabarPermisosPantalla_NullList() {
        int resultado = permisoPantallaService.grabarPermisosPantalla(null);

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Grabar permisos pantalla - lista vacía")
    public void testGrabarPermisosPantalla_EmptyList() {
        int resultado = permisoPantallaService.grabarPermisosPantalla(Collections.emptyList());

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Grabar permisos pantalla - error por rol inválido")
    public void testGrabarPermisosPantalla_RolInvalido() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(1L);
        dto.setRol(null);
        dto.setPantalla(new Pantalla());

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantalla(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por pantalla inválida")
    public void testGrabarPermisosPantalla_PantallaInvalida() {
        PermisoPantallaDTO dto = new PermisoPantallaDTO();
        dto.setId(1L);
        dto.setRol(new Rol());
        dto.setPantalla(null);

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantalla(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por pantalla inexistente")
    public void testGrabarPermisosPantalla_PantallaInexistente() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(1L, 1L, 2L, true);
        when(rolRepository.findById(1L)).thenReturn(java.util.Optional.of(dto.getRol()));
        when(pantallaRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantalla(List.of(dto)));
    }
    
    @Test
    @DisplayName("Grabar permisos pantalla - error por Rol inexistente")
    public void testGrabarPermisosPantalla_RolInexistente() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(1L, 1L, 2L, true);
        when(rolRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        when(pantallaRepository.findById(2L)).thenReturn(java.util.Optional.of(dto.getPantalla()));

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantalla(List.of(dto)));
    }


    @Test
    @DisplayName("Grabar permisos pantalla - DTO nulo en la lista")
    public void testGrabarPermisosPantalla_dtoNulo() {
        List<PermisoPantallaDTO> dtoList = new ArrayList<PermisoPantallaDTO>();
        dtoList.add(null);

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.grabarPermisosPantalla(dtoList));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla - éxito")
    public void testEliminarPermisosPantalla_Success() {
        Long id = 1L;
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(id, 1L, 2L, true);
        PermisoPantalla permiso = buildPermisoPantalla(id, 1L, 2L, true);
        when(permisoPantallaRepository.findById(id)).thenReturn(java.util.Optional.of(permiso));

        int resultado = permisoPantallaService.eliminarPermisosPantalla(List.of(dto));

        assertEquals(1, resultado);
        verify(permisoPantallaRepository, times(1)).delete(permiso);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla - lista nula")
    public void testEliminarPermisosPantalla_NullList() {
        int resultado = permisoPantallaService.eliminarPermisosPantalla(null);

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla - lista vacía")
    public void testEliminarPermisosPantalla_EmptyList() {
        int resultado = permisoPantallaService.eliminarPermisosPantalla(Collections.emptyList());

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla - error por id inválido")
    public void testEliminarPermisosPantalla_Error() {
        PermisoPantallaDTO dto = buildPermisoPantallaDTO(null, 1L, 2L, true);

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.eliminarPermisosPantalla(List.of(dto)));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por DTO nulo en la lista")
    public void testEliminarPermisosPantalla_DTONulo() {
        List<PermisoPantallaDTO> dtoList = new ArrayList<PermisoPantallaDTO>();
        dtoList.add(null);

        assertThrows(RuntimeException.class, () -> permisoPantallaService.eliminarPermisosPantalla(dtoList));
    }
    
    @Test
    @DisplayName("Eliminar permisos pantalla con permiso no encontrado")
    public void testEliminarPermisosPantalla_PermisoInexistente() {
        List<PermisoPantallaDTO> dtoList = new ArrayList<PermisoPantallaDTO>();
        dtoList.add(buildPermisoPantallaDTO(1L, 1L, 999L, true));

        when(permisoPantallaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> permisoPantallaService.eliminarPermisosPantalla(dtoList));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por id - éxito")
    public void testEliminarPermisosPantallaPorId_Success() {
        Long id = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(id, 1L, 2L, true);
        when(permisoPantallaRepository.findById(id)).thenReturn(java.util.Optional.of(permiso));

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorId(id);

        assertEquals(true, resultado);
        verify(permisoPantallaRepository, times(1)).delete(permiso);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por id - id inexistente")
    public void testEliminarPermisosPantallaPorId_Inexistente() {
        Long id = 999L;
        when(permisoPantallaRepository.findById(id)).thenReturn(java.util.Optional.empty());

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorId(id);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por id - error")
    public void testEliminarPermisosPantallaPorId_Error() {
        Long id = 1L;
        when(permisoPantallaRepository.findById(id)).thenThrow(new RuntimeException("Error al buscar id"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.eliminarPermisosPantallaPorId(id));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por id - id nulo")
    public void testEliminarPermisosPantallaPorId_Nulo() {
        Long id = null;
        assertFalse(permisoPantallaService.eliminarPermisosPantallaPorId(id));
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
    @DisplayName("Eliminar permisos pantalla por pantalla - éxito")
    public void testEliminarPermisosPantallaPorPantallaId_Success() {
        Long idPantalla = 1L;
        PermisoPantalla permiso = buildPermisoPantalla(1L, 2L, idPantalla, true);
        when(permisoPantallaRepository.findByPantallaId(idPantalla)).thenReturn(List.of(permiso));

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorPantallaId(idPantalla);

        assertEquals(true, resultado);
        verify(permisoPantallaRepository, times(1)).deleteAll(List.of(permiso));
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por pantalla - id inexistente")
    public void testEliminarPermisosPantallaPorPantallaId_Inexistente() {
        Long idPantalla = 999L;
        when(permisoPantallaRepository.findByPantallaId(idPantalla)).thenReturn(Collections.emptyList());

        boolean resultado = permisoPantallaService.eliminarPermisosPantallaPorPantallaId(idPantalla);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Eliminar permisos pantalla por pantalla - error")
    public void testEliminarPermisosPantallaPorPantallaId_Error() {
        Long idPantalla = 1L;
        when(permisoPantallaRepository.findByPantallaId(idPantalla)).thenThrow(new RuntimeException("Error al eliminar por pantalla"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.eliminarPermisosPantallaPorPantallaId(idPantalla));
    }

    @Test
    @DisplayName("Desactivar permiso pantalla por id - éxito")
    public void testDesactivarPermisosPantallaPorId_Success() {
        Long id = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaById(id)).thenReturn(1);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorId(id);

        assertEquals(true, resultado);
    }

    @Test
    @DisplayName("Desactivar permiso pantalla por id - id inexistente")
    public void testDesactivarPermisosPantallaPorId_Inexistente() {
        Long id = 999L;
        when(permisoPantallaRepository.deactivatePermisoPantallaById(id)).thenReturn(0);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorId(id);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Desactivar permiso pantalla por id - error")
    public void testDesactivarPermisosPantallaPorId_Error() {
        Long id = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaById(id))
                .thenThrow(new RuntimeException("Error al desactivar por id"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.desactivarPermisosPantallaPorId(id));
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por rol - éxito")
    public void testDesactivarPermisosPantallaPorRolId_Success() {
        Long idRol = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(idRol)).thenReturn(1);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorRolId(idRol);

        assertEquals(true, resultado);
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por rol - id inexistente")
    public void testDesactivarPermisosPantallaPorRolId_Inexistente() {
        Long idRol = 999L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(idRol)).thenReturn(0);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorRolId(idRol);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por rol - error")
    public void testDesactivarPermisosPantallaPorRolId_Error() {
        Long idRol = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByRolId(idRol))
                .thenThrow(new RuntimeException("Error al desactivar por rol"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.desactivarPermisosPantallaPorRolId(idRol));
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por pantalla - éxito")
    public void testDesactivarPermisosPantallaPorPantallaId_Success() {
        Long idPantalla = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByPantallaId(idPantalla)).thenReturn(1);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorPantallaId(idPantalla);

        assertEquals(true, resultado);
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por pantalla - id inexistente")
    public void testDesactivarPermisosPantallaPorPantallaId_Inexistente() {
        Long idPantalla = 999L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByPantallaId(idPantalla)).thenReturn(0);

        boolean resultado = permisoPantallaService.desactivarPermisosPantallaPorPantallaId(idPantalla);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Desactivar permisos pantalla por pantalla - error")
    public void testDesactivarPermisosPantallaPorPantallaId_Error() {
        Long idPantalla = 1L;
        when(permisoPantallaRepository.deactivatePermisoPantallaByPantallaId(idPantalla))
                .thenThrow(new RuntimeException("Error al desactivar por pantalla"));

        assertThrows(RuntimeException.class, () -> permisoPantallaService.desactivarPermisosPantallaPorPantallaId(idPantalla));
    }
}
