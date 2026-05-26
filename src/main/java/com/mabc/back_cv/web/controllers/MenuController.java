package com.mabc.back_cv.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.services.Menu.MenuService;

import java.util.List;

/**
 * Controlador REST que gestiona las operaciones HTTP relacionadas con los menús
 * del sistema.
 * Proporciona endpoints para listar, buscar, guardar y eliminar menús,
 * además de filtrar menús por roles y dependencias (menús padres/hijos).
 */
@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    /**
     * Constructor que inyecta la dependencia del servicio de menús.
     *
     * @param menuService Servicio que gestiona la lógica de negocio de los menús.
     */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Obtiene todos los menús registrados en el sistema.
     *
     * @return ResponseEntity con la lista de MenuDTO de todos los menús.
     */
    @GetMapping("/all")
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    /**
     * Obtiene los menús principales para un rol específico.
     *
     * @param rolId Identificador único del rol.
     * @return ResponseEntity con la lista de MenuDTO de menús principales.
     */
    @GetMapping("/main/{rolId}")
    public ResponseEntity<List<MenuDTO>> getMainMenusByRol(@PathVariable Long rolId) {
        return ResponseEntity.ok(menuService.getMainMenusByRol(rolId));
    }

    /**
     * Obtiene los submenús asociados a un menú padre y un rol específico.
     *
     * @param menuId Identificador del menú padre.
     * @param rolId  Identificador del rol del usuario.
     * @return ResponseEntity con la lista de MenuDTO de submenús.
     */
    @GetMapping("/sub/{menuId}/{rolId}")
    public ResponseEntity<List<MenuDTO>> getSubMenusByMenuAndRol(@PathVariable Long menuId, @PathVariable Long rolId) {
        return ResponseEntity.ok(menuService.getSubMenusByMenuAndRol(menuId, rolId));
    }

    /**
     * Obtiene la lista de todos los menús activos del sistema.
     *
     * @return ResponseEntity con la lista de MenuDTO de menús activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<MenuDTO>> getActiveMenus() {
        return ResponseEntity.ok(menuService.getActiveMenus());
    }

    /**
     * Obtiene la lista de menús que no generan recursividad cíclica si fuesen
     * elegidos como padre del menú indicado.
     *
     * @param menuId Identificador del menú de referencia.
     * @return ResponseEntity con la lista de MenuDTO de menús no recursivos.
     */
    @GetMapping("/no-recursive/{menuId}")
    public ResponseEntity<List<MenuDTO>> getNoRecursiveMenusOptions(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getNoRecursiveMenusOptions(menuId));
    }

    /**
     * Busca y obtiene un menú específico mediante su identificador único.
     *
     * @param id Identificador único del menú.
     * @return ResponseEntity con el MenuDTO del menú encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    /**
     * Guarda o actualiza un menú en el sistema.
     * Valida que no se creen relaciones recursivas de paternidad.
     *
     * @param menuDTO DTO con la información del menú a guardar o actualizar.
     * @return ResponseEntity con el MenuDTO guardado, o status bad request si
     *         ocurre un error de recursividad o datos no válidos.
     */
    @PostMapping("/save")
    public ResponseEntity<MenuDTO> saveMenu(@RequestBody MenuDTO menuDTO) {
        try {
            return ResponseEntity.ok(menuService.saveMenu(menuDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Elimina un menú del sistema mediante su identificador único.
     *
     * @param id Identificador único del menú a eliminar.
     * @return ResponseEntity con un mensaje indicando el resultado de la
     *         eliminación.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenu(id);
            return ResponseEntity.ok("Menu eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el menú");
        }
    }
}
