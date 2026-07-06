package com.mabc.back_cv.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mabc.back_cv.web.controllers.MenuController;
import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.services.Menu.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para el controlador de menús {@link MenuController}.
 * Utiliza MockMvc en modo standalone para evitar levantar el contexto de
 * seguridad
 * y centrarse exclusivamente en la verificación de endpoints y sus respuestas.
 */
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllMenus_ShouldReturnListOfMenus() throws Exception {
        MenuDTO menu1 = new MenuDTO(1L, "Inicio", "/home", "home-icon", 1, null, true, null);
        MenuDTO menu2 = new MenuDTO(2L, "Config", "/config", "cog-icon", 2, null, true, null);
        List<MenuDTO> menus = Arrays.asList(menu1, menu2);

        when(menuService.getAllMenus()).thenReturn(menus);

        mockMvc.perform(get("/api/menus/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Inicio")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Config")));

        verify(menuService, times(1)).getAllMenus();
    }

    @Test
    void getMainMenusByRol_ShouldReturnList() throws Exception {
        MenuDTO menu1 = new MenuDTO(1L, "Inicio", "/home", "home-icon", 1, null, true, null);
        when(menuService.getMainMenusByRol(1L)).thenReturn(List.of(menu1));

        mockMvc.perform(get("/api/menus/main/{rolId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Inicio")));

        verify(menuService, times(1)).getMainMenusByRol(1L);
    }

    @Test
    void getSubMenusByMenuAndRol_ShouldReturnList() throws Exception {
        MenuDTO menu1 = new MenuDTO(2L, "SubMenu", "/sub", "sub-icon", 1, 1L, true, null);
        when(menuService.getSubMenusByMenuAndRol(1L, 3L)).thenReturn(List.of(menu1));

        mockMvc.perform(get("/api/menus/sub/{menuId}/{rolId}", 1L, 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("SubMenu")));

        verify(menuService, times(1)).getSubMenusByMenuAndRol(1L, 3L);
    }

    @Test
    void getActiveMenus_ShouldReturnList() throws Exception {
        MenuDTO menu1 = new MenuDTO(1L, "Activo", "/act", "icon", 1, null, true, null);
        when(menuService.getActiveMenus()).thenReturn(List.of(menu1));

        mockMvc.perform(get("/api/menus/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Activo")));

        verify(menuService, times(1)).getActiveMenus();
    }

    @Test
    void getNoRecursiveMenusOptions_ShouldReturnList() throws Exception {
        MenuDTO menu1 = new MenuDTO(2L, "No Recursive Option", "/opt", "icon", 2, null, true, null);
        when(menuService.getNoRecursiveMenusOptions(1L)).thenReturn(List.of(menu1));

        mockMvc.perform(get("/api/menus/no-recursive/{menuId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("No Recursive Option")));

        verify(menuService, times(1)).getNoRecursiveMenusOptions(1L);
    }

    @Test
    void getMenuById_ShouldReturnMenu() throws Exception {
        MenuDTO menu = new MenuDTO(1L, "Inicio", "/home", "home-icon", 1, null, true, null);
        when(menuService.getMenuById(1L)).thenReturn(menu);

        mockMvc.perform(get("/api/menus/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Inicio")));

        verify(menuService, times(1)).getMenuById(1L);
    }

    @Test
    void saveMenu_Success_ShouldReturnSavedMenu() throws Exception {
        MenuDTO toSave = new MenuDTO(null, "Nuevo", "/new", "new-icon", 3, null, true, null);
        MenuDTO saved = new MenuDTO(3L, "Nuevo", "/new", "new-icon", 3, null, true, null);

        when(menuService.saveMenu(any(MenuDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/menus/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Nuevo")));

        verify(menuService, times(1)).saveMenu(any(MenuDTO.class));
    }

    @Test
    void saveMenu_Failure_ShouldReturnBadRequest() throws Exception {
        MenuDTO toSave = new MenuDTO(null, "Nuevo", "/new", "new-icon", 3, 1L, true, null);

        when(menuService.saveMenu(any(MenuDTO.class))).thenThrow(new RuntimeException("Recursividad detectada"));

        mockMvc.perform(post("/api/menus/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        verify(menuService, times(1)).saveMenu(any(MenuDTO.class));
    }

    @Test
    void deleteMenu_Success_ShouldReturnOkMessage() throws Exception {
        doNothing().when(menuService).deleteMenu(1L);

        mockMvc.perform(delete("/api/menus/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Menu eliminado correctamente"));

        verify(menuService, times(1)).deleteMenu(1L);
    }

    @Test
    void deleteMenu_Failure_ShouldReturnBadRequestMessage() throws Exception {
        doThrow(new RuntimeException("Error DB")).when(menuService).deleteMenu(1L);

        mockMvc.perform(delete("/api/menus/delete/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al eliminar el menú"));

        verify(menuService, times(1)).deleteMenu(1L);
    }
}
