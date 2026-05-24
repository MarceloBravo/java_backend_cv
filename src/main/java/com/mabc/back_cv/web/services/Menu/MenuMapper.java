package com.mabc.back_cv.web.services.Menu;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.dto.MenuDTO;
import com.mabc.back_cv.web.entities.Menu;

@Component
public class MenuMapper {

    /**
     * Convierte un objeto de transferencia de datos MenuDTO en una entidad Menu.
     *
     * @param menuDTO El DTO del menú.
     * @return Entidad Menu configurada con los datos del DTO.
     */
    public Menu convertToMenuEntity(MenuDTO menuDTO) {
        Menu menu = new Menu();
        if (menuDTO.getId() != null) {
            menu.setId(menuDTO.getId());
        }
        menu.setNombre(menuDTO.getNombre());
        menu.setUrl(menuDTO.getUrl());
        menu.setIcono(menuDTO.getIcono());
        menu.setOrden(menuDTO.getOrden());
        menu.setMenu_padre_id(menuDTO.getMenuPadreId());
        menu.setActivo(menuDTO.getActivo());
        return menu;
    }

    /**
     * Convierte una entidad Menu en un objeto de transferencia de datos MenuDTO.
     *
     * @param menu La entidad Menu a convertir.
     * @return Objeto MenuDTO configurado con los datos de la entidad, o null si la
     *         entidad es nula.
     */
    public MenuDTO convertToDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setNombre(menu.getNombre());
        dto.setUrl(menu.getUrl());
        dto.setIcono(menu.getIcono());
        dto.setOrden(menu.getOrden());
        dto.setMenuPadreId(menu.getMenu_padre_id());
        dto.setActivo(menu.getActivo());
        return dto;
    }

}
