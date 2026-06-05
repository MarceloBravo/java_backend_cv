package com.mabc.back_cv.web.services.Menu;

import org.springframework.stereotype.Service;

import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.entities.Menu;
import com.mabc.back_cv.web.repositories.MenuRepository;
import java.util.List;

/**
 * Servicio encargado de gestionar la lógica de negocio asociada a los menús.
 * Proporciona servicios para consultar menús por rol, filtrar recursividad,
 * buscar, crear, actualizar y eliminar menús.
 */
@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    /**
     * Constructor que inyecta la dependencia del repositorio de menús.
     *
     * @param menuRepository Repositorio que maneja el acceso a datos para la
     *                       entidad Menu.
     */
    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    /**
     * Obtiene todos los menús registrados en el sistema ordenados por su campo
     * orden.
     * 
     * @return Lista de todos los menús.
     */
    public List<MenuDTO> getAllMenus() {
        return menuRepository.findAllMenus().stream()
                .map(m -> new MenuDTO(m.getId(), m.getNombre(), m.getUrl(), m.getIcono(), m.getOrden(),
                        m.getMenu_padre_id(), m.getActivo(), null))
                .toList();
    }

    /**
     * Obtiene los menús principales (sin padre o sin permisos específicos) para un
     * rol dado.
     * 
     * @param rolId Identificador del rol del usuario.
     * @return Lista de menús principales.
     */
    public List<MenuDTO> getMainMenusByRol(Long rolId) {
        return menuRepository.findMainMenusByRolId(rolId).stream()
                .map(m -> new MenuDTO(m.getId(), m.getNombre(), m.getUrl(), m.getIcono(), m.getOrden(),
                        m.getMenu_padre_id(), m.getActivo(), null))
                .toList();
    }

    /**
     * Obtiene los submenús asociados a un menú padre y un rol específico.
     * 
     * @param menuId Identificador del menú padre.
     * @param rolId  Identificador del rol del usuario.
     * @return Lista de submenús.
     */
    public List<MenuDTO> getSubMenusByMenuAndRol(Long menuId, Long rolId) {
        return menuRepository.findSubMenusByMenuAndRolId(menuId, rolId).stream()
                .map(m -> new MenuDTO(m.getId(), m.getNombre(), m.getUrl(), m.getIcono(), m.getOrden(),
                        m.getMenu_padre_id(), m.getActivo(), null))
                .toList();
    }

    /**
     * Obtiene todos los menús que se encuentran marcados como activos.
     * 
     * @return Lista de menús activos.
     */
    public List<MenuDTO> getActiveMenus() {
        return menuRepository.findByActivoTrue().stream()
                .map(m -> new MenuDTO(m.getId(), m.getNombre(), m.getUrl(), m.getIcono(), m.getOrden(),
                        m.getMenu_padre_id(), m.getActivo(), null))
                .toList();
    }

    /**
     * Obtiene una lista de menús que no generan ciclos de recursividad si se
     * seleccionaran como padre del menú indicado.
     *
     * @param menuId Identificador del menú actual para el cual se evalúan las
     *               opciones viables.
     * @return Lista de MenuDTO de opciones de menús no recursivos.
     */
    public List<MenuDTO> getNoRecursiveMenusOptions(Long menuId) {
        return menuRepository.getNoRecursiveMenusOptions(menuId).stream()
                .map(m -> new MenuDTO(m.getId(), m.getNombre(), m.getUrl(), m.getIcono(), m.getOrden(),
                        m.getMenu_padre_id(), m.getActivo(), null))
                .toList();
    }

    /**
     * Busca y obtiene un menú por su identificador único.
     *
     * @param id Identificador único del menú.
     * @return DTO del menú si se encuentra, o null si no existe o el id es nulo.
     */
    public MenuDTO getMenuById(Long id) {
        if (id == null)
            return null;
        return menuMapper.convertToDTO(menuRepository.findById(id).orElse(null));
    }

    /**
     * Guarda o actualiza un menú en el sistema.
     * Valida que no exista una referencia recursiva antes de guardar.
     *
     * @param menuDTO DTO con la información del menú a guardar.
     * @return MenuDTO correspondiente al menú guardado.
     * @throws RuntimeException si la estructura jerárquica del menú genera
     *                          recursividad o si los datos no son válidos.
     */
    public MenuDTO saveMenu(MenuDTO menuDTO) {
        if (menuDTO == null ||
                (menuDTO.getMenuPadreId() != null && !isMenuPadreExists(menuDTO.getMenuPadreId()))) {
            return null;
        }

        if (menuDTO.getId() != null && hasRecursiveParent(menuDTO.getId())) {
            throw new RuntimeException("Error al guardar el menú: el menú padre seleccionado genera recursividad");
        }

        Menu menuToSave = menuMapper.convertToMenuEntity(menuDTO);
        if (menuToSave == null)
            throw new RuntimeException("Error al guardar el menú: datos no validos");

        Menu savedMenu = menuRepository.save(menuToSave);
        return menuMapper.convertToDTO(savedMenu);
    }

    /**
     * Comprueba si existe un menú padre registrado con el ID proporcionado.
     *
     * @param menuPadreId Identificador del menú padre a verificar.
     * @return true si el menú padre existe en el repositorio; false de lo
     *         contrario.
     */
    private boolean isMenuPadreExists(Long menuPadreId) {
        if (menuPadreId == null) {
            return false;
        }
        return menuRepository.existsById(menuPadreId);
    }

    /**
     * Verifica de forma recursiva si un menú posee una referencia cíclica
     * hacia un padre que a su vez sea descendiente suyo.
     *
     * @param menuId Identificador del menú a evaluar.
     * @return true si existe recursividad, false de lo contrario.
     */
    private boolean hasRecursiveParent(Long menuId) {
        Byte result = menuRepository.hasRecursiveParent(menuId);
        return result != null && result > 0;
    }

    /**
     * Elimina un menú del sistema basándose en su identificador único.
     *
     * @param id Identificador único del menú a eliminar.
     */
    public void deleteMenu(Long id) {
        if (id == null || !menuRepository.existsById(id)) {
            return;
        }
        menuRepository.deleteById(id);
    }
}
