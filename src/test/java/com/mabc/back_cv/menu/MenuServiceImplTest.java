package com.mabc.back_cv.menu;

import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.repositories.MenuRepository;
import com.mabc.back_cv.web.services.Menu.MenuMapper;
import com.mabc.back_cv.web.services.Menu.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Pruebas unitarias para la implementación del servicio {@link MenuServiceImpl}.
 * Valida la lógica de negocio del servicio, incluyendo la prevención de ciclos recursivos
 * y las conversiones de DTO a entidad y viceversa mediante su mapper inyectado.
 */
@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuMapper menuMapper;

    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    @DisplayName("Obtener todos los menús devuelve lista de DTOs")
    void getAllMenus_ShouldReturnListOfMenuDTOs() {
        Menu menu1 = new Menu(1L, "Inicio", "/home", "home-icon", 1, null, true);
        Menu menu2 = new Menu(2L, "Config", "/config", "cog-icon", 2, null, true);
        when(menuRepository.findAllMenus()).thenReturn(Arrays.asList(menu1, menu2));

        List<MenuDTO> result = menuService.getAllMenus();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inicio", result.get(0).getNombre());
        assertEquals("Config", result.get(1).getNombre());
        verify(menuRepository, times(1)).findAllMenus();
    }

    @Test
    @DisplayName("Obtener menús principales por rol devuelve lista")
    void getMainMenusByRol_ShouldReturnList() {
        Menu menu = new Menu(1L, "Inicio", "/home", "home-icon", 1, null, true);
        when(menuRepository.findMainMenusByRolId(1L)).thenReturn(Collections.singletonList(menu));

        List<MenuDTO> result = menuService.getMainMenusByRol(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inicio", result.get(0).getNombre());
        verify(menuRepository, times(1)).findMainMenusByRolId(1L);
    }

    @Test
    @DisplayName("Obtener submenús por menú y rol devuelve lista")
    void getSubMenusByMenuAndRol_ShouldReturnList() {
        Menu menu = new Menu(2L, "SubMenu", "/sub", "sub-icon", 1, 1L, true);
        when(menuRepository.findSubMenusByMenuAndRolId(1L, 3L)).thenReturn(Collections.singletonList(menu));

        List<MenuDTO> result = menuService.getSubMenusByMenuAndRol(1L, 3L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SubMenu", result.get(0).getNombre());
        verify(menuRepository, times(1)).findSubMenusByMenuAndRolId(1L, 3L);
    }

    @Test
    @DisplayName("Obtener menús activos devuelve lista")
    void getActiveMenus_ShouldReturnList() {
        Menu menu = new Menu(1L, "Activo", "/act", "icon", 1, null, true);
        when(menuRepository.findByActivoTrue()).thenReturn(Collections.singletonList(menu));

        List<MenuDTO> result = menuService.getActiveMenus();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Activo", result.get(0).getNombre());
        verify(menuRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("Obtener opciones sin recursividad devuelve lista")
    void getNoRecursiveMenusOptions_ShouldReturnList() {
        Menu menu = new Menu(2L, "No Recursive Option", "/opt", "icon", 2, null, true);
        when(menuRepository.getNoRecursiveMenusOptions(1L)).thenReturn(Collections.singletonList(menu));

        List<MenuDTO> result = menuService.getNoRecursiveMenusOptions(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("No Recursive Option", result.get(0).getNombre());
        verify(menuRepository, times(1)).getNoRecursiveMenusOptions(1L);
    }

    @Test
    @DisplayName("Obtener menú por id nulo devuelve null")
    void getMenuById_NullId_ShouldReturnNull() {
        MenuDTO result = menuService.getMenuById(null);
        assertNull(result);
        verifyNoInteractions(menuRepository, menuMapper);
    }

    @Test
    @DisplayName("Obtener menú por id válido devuelve DTO")
    void getMenuById_ValidId_ShouldReturnMenuDTO() {
        Menu menu = new Menu(1L, "Inicio", "/home", "home-icon", 1, null, true);
        MenuDTO dto = new MenuDTO(1L, "Inicio", "/home", "home-icon", 1, null, true, null);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuMapper.convertToDTO(menu)).thenReturn(dto);

        MenuDTO result = menuService.getMenuById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Inicio", result.getNombre());
        verify(menuRepository, times(1)).findById(1L);
        verify(menuMapper, times(1)).convertToDTO(menu);
    }

    @Test
    @DisplayName("Guardar menú nulo devuelve null")
    void saveMenu_NullDTO_ShouldReturnNull() {
        MenuDTO result = menuService.saveMenu(null);
        assertNull(result);
        verifyNoInteractions(menuRepository, menuMapper);
    }

    @Test
    @DisplayName("Guardar menú con padre inexistente devuelve null")
    void saveMenu_NonExistentParent_ShouldReturnNull() {
        MenuDTO dto = new MenuDTO(null, "Nuevo", "/new", "icon", 1, 999L, true, null);
        when(menuRepository.existsById(999L)).thenReturn(false);

        MenuDTO result = menuService.saveMenu(dto);

        assertNull(result);
        verify(menuRepository, times(1)).existsById(999L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    @DisplayName("Guardar menú con padre recursivo lanza excepción")
    void saveMenu_RecursiveParent_ShouldThrowException() {
        MenuDTO dto = new MenuDTO(1L, "Recursivo", "/rec", "icon", 1, 2L, true, null);
        when(menuRepository.existsById(2L)).thenReturn(true);
        when(menuRepository.hasRecursiveParent(1L)).thenReturn((byte) 1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuService.saveMenu(dto));

        assertEquals("Error al guardar el menú: el menú padre seleccionado genera recursividad", exception.getMessage());
        verify(menuRepository, times(1)).existsById(2L);
        verify(menuRepository, times(1)).hasRecursiveParent(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    @DisplayName("Guardar menú con mapeo inválido lanza excepción")
    void saveMenu_InvalidEntityMapping_ShouldThrowException() {
        MenuDTO dto = new MenuDTO(null, "Invalido", "/inv", "icon", 1, null, true, null);
        when(menuMapper.convertToMenuEntity(dto)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuService.saveMenu(dto));

        assertEquals("Error al guardar el menú: datos no validos", exception.getMessage());
        verify(menuMapper, times(1)).convertToMenuEntity(dto);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    @DisplayName("Guardar menú exitoso devuelve el DTO guardado")
    void saveMenu_Success_ShouldReturnSavedMenuDTO() {
        MenuDTO dto = new MenuDTO(null, "Nuevo", "/new", "icon", 1, 2L, true, null);
        Menu menuToSave = new Menu(null, "Nuevo", "/new", "icon", 1, 2L, true);
        Menu savedMenu = new Menu(3L, "Nuevo", "/new", "icon", 1, 2L, true);
        MenuDTO savedDto = new MenuDTO(3L, "Nuevo", "/new", "icon", 1, 2L, true, null);

        when(menuRepository.existsById(2L)).thenReturn(true);
        when(menuMapper.convertToMenuEntity(dto)).thenReturn(menuToSave);
        when(menuRepository.save(menuToSave)).thenReturn(savedMenu);
        when(menuMapper.convertToDTO(savedMenu)).thenReturn(savedDto);

        MenuDTO result = menuService.saveMenu(dto);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Nuevo", result.getNombre());
        verify(menuRepository, times(1)).existsById(2L);
        verify(menuMapper, times(1)).convertToMenuEntity(dto);
        verify(menuRepository, times(1)).save(menuToSave);
        verify(menuMapper, times(1)).convertToDTO(savedMenu);
    }

    @Test
    @DisplayName("Guardar menú con id y padre no recursivo debe guardarse correctamente")
    void saveMenu_WithIdAndNonRecursiveParent_ShouldSaveSuccessfully() {
        MenuDTO dto = new MenuDTO(5L, "Actualizado", "/update", "icon", 1, 2L, true, null);
        Menu menuToSave = new Menu(5L, "Actualizado", "/update", "icon", 1, 2L, true);
        Menu savedMenu = new Menu(5L, "Actualizado", "/update", "icon", 1, 2L, true);
        MenuDTO savedDto = new MenuDTO(5L, "Actualizado", "/update", "icon", 1, 2L, true, null);

        when(menuRepository.existsById(2L)).thenReturn(true);
        when(menuRepository.hasRecursiveParent(5L)).thenReturn((byte) 0);
        when(menuMapper.convertToMenuEntity(dto)).thenReturn(menuToSave);
        when(menuRepository.save(menuToSave)).thenReturn(savedMenu);
        when(menuMapper.convertToDTO(savedMenu)).thenReturn(savedDto);

        MenuDTO result = menuService.saveMenu(dto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Actualizado", result.getNombre());
        verify(menuRepository, times(1)).existsById(2L);
        verify(menuRepository, times(1)).hasRecursiveParent(5L);
        verify(menuRepository, times(1)).save(menuToSave);
        verify(menuMapper, times(1)).convertToDTO(savedMenu);
    }

    @Test
    @DisplayName("Guardar menú con hasRecursiveParent null debe tratarse como no recursivo")
    void saveMenu_HasRecursiveParentNull_ShouldSaveSuccessfully() {
        MenuDTO dto = new MenuDTO(6L, "ActualizadoNull", "/updatenull", "icon", 1, 2L, true, null);
        Menu menuToSave = new Menu(6L, "ActualizadoNull", "/updatenull", "icon", 1, 2L, true);
        Menu savedMenu = new Menu(6L, "ActualizadoNull", "/updatenull", "icon", 1, 2L, true);
        MenuDTO savedDto = new MenuDTO(6L, "ActualizadoNull", "/updatenull", "icon", 1, 2L, true, null);

        when(menuRepository.existsById(2L)).thenReturn(true);
        when(menuRepository.hasRecursiveParent(6L)).thenReturn(null);
        when(menuMapper.convertToMenuEntity(dto)).thenReturn(menuToSave);
        when(menuRepository.save(menuToSave)).thenReturn(savedMenu);
        when(menuMapper.convertToDTO(savedMenu)).thenReturn(savedDto);

        MenuDTO result = menuService.saveMenu(dto);

        assertNotNull(result);
        assertEquals(6L, result.getId());
        assertEquals("ActualizadoNull", result.getNombre());
        verify(menuRepository, times(1)).existsById(2L);
        verify(menuRepository, times(1)).hasRecursiveParent(6L);
        verify(menuRepository, times(1)).save(menuToSave);
        verify(menuMapper, times(1)).convertToDTO(savedMenu);
    }

    @Test
    @DisplayName("Verificar si un menu padre null devuelve false")
    void isMenuPadreExists_NullParentId_ShouldReturnFalse() throws Exception {
        Method method = MenuServiceImpl.class.getDeclaredMethod("isMenuPadreExists", Long.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(menuService, new Object[] { null });

        assertFalse(result);
        verify(menuRepository, never()).existsById(anyLong());
    }

    @Test
    @DisplayName("Eliminar menú con id nulo no debe llamar al repositorio")
    void deleteMenu_NullId_ShouldDoNothing() {
        menuService.deleteMenu(null);
        verify(menuRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Eliminar menú con id válido llama al repositorio")
    void deleteMenu_ValidId_ShouldCallRepository() {
        menuService.deleteMenu(1L);
        verify(menuRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Prevención de ciclos recursivos en saveMenu")
    void saveMenu_PreventRecursiveCycle_ShouldThrowException() {
        MenuDTO dto = new MenuDTO(1L, "Recursivo", "/rec", "icon", 1, 2L, true, null);
        when(menuRepository.existsById(2L)).thenReturn(true);
        when(menuRepository.hasRecursiveParent(1L)).thenReturn((byte) 1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuService.saveMenu(dto));

        assertEquals("Error al guardar el menú: el menú padre seleccionado genera recursividad", exception.getMessage());
        verify(menuRepository, times(1)).existsById(2L);
        verify(menuRepository, times(1)).hasRecursiveParent(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }
}
