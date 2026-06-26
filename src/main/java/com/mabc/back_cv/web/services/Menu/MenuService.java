package com.mabc.back_cv.web.services.Menu;

import java.util.List;
import com.mabc.back_cv.web.dto.MenuDTO;

/**
 * Interfaz de servicio para la gestión de menús del sistema.
 * Define las operaciones disponibles para la administración de menús.
 */
public interface MenuService {
    /**
     * Obtiene todos los menús registrados en el sistema.
     *
     * @return Lista de todos los MenuDTO.
     */
    public List<MenuDTO> getAllMenus();

    /**
     * Obtiene los menús principales para un rol específico.
     *
     * @param rolId Identificador del rol.
     * @return Lista de MenuDTO de menús principales.
     */
    public List<MenuDTO> getMainMenusByRol(Long rolId);

    /**
     * Obtiene los submenús asociados a un menú padre y un rol específico.
     *
     * @param menuId Identificador del menú padre.
     * @param rolId  Identificador del rol.
     * @return Lista de MenuDTO de submenús.
     */
    public List<MenuDTO> getSubMenusByMenuAndRol(Long menuId, Long rolId);

    /**
     * Obtiene la lista de todos los menús activos.
     *
     * @return Lista de MenuDTO activos.
     */
    public List<MenuDTO> getActiveMenus();

    /**
     * Obtiene la lista de menús que no generan recursividad si se eligen como padre del menú indicado.
     *
     * @param menuId Identificador del menú de referencia.
     * @return Lista de MenuDTO de opciones no recursivas.
     */
    public List<MenuDTO> getNoRecursiveMenusOptions(Long menuId);

    /**
     * Obtiene un menú por su identificador.
     *
     * @param id Identificador del menú.
     * @return MenuDTO encontrado o null si no existe.
     */
    public MenuDTO getMenuById(Long id);

    /**
     * Crea o actualiza un menú.
     *
     * @param menuDTO DTO del menú a guardar.
     * @return MenuDTO guardado.
     */
    public MenuDTO saveMenu(MenuDTO menuDTO);

    /**
     * Elimina un menú por su identificador.
     *
     * @param id Identificador del menú a eliminar.
     */
    public void deleteMenu(Long id);
}
