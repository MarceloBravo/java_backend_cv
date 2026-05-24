package com.mabc.back_cv.web.services.Menu;

import java.util.List;
import com.mabc.back_cv.web.dto.MenuDTO;

public interface MenuService {
    public List<MenuDTO> getAllMenus();

    public List<MenuDTO> getMainMenusByRol(Long rolId);

    public List<MenuDTO> getSubMenusByMenuAndRol(Long menuId, Long rolId);

    public List<MenuDTO> getActiveMenus();

    public List<MenuDTO> getNoRecursiveMenusOptions(Long menuId);

    public MenuDTO getMenuById(Long id);

    public MenuDTO saveMenu(MenuDTO menuDTO);

    public void deleteMenu(Long id);
}
