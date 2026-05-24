package com.mabc.back_cv.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private Long id;
    private String nombre;
    private String url;
    private String icono;
    private Integer orden;
    private Long menuPadreId;
    private Boolean activo;
    private List<MenuDTO> subMenus;

    public MenuDTO() {
    }

    public MenuDTO(Long id, String nombre, String url, String icono, Integer orden, Long menuPadreId, Boolean activo,
            List<MenuDTO> subMenus) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.icono = icono;
        this.orden = orden;
        this.menuPadreId = menuPadreId;
        this.activo = activo;
        this.subMenus = subMenus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getMenuPadreId() {
        return menuPadreId;
    }

    public void setMenuPadreId(Long menuPadreId) {
        this.menuPadreId = menuPadreId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<MenuDTO> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<MenuDTO> subMenus) {
        this.subMenus = subMenus;
    }
}
