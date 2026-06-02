package com.mabc.back_cv.web.services.permisoPantalla;

import java.util.List;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;


public interface PermisoPantallaService {

    public List<PermisoPantallaDTO> obtenerPermisosPantallaPorRol(Long id_rol);

    public PermisoPantallaDTO obtenerPermisoPantallaPorRolYPantalla(Long id_rol, Long id_pantalla, Boolean activo);

    public int grabarPermisosPantallaPorRol(List<PermisoPantallaDTO> permisosPantallaDTO);

    public boolean eliminarPermisosPantallaPorRolId(Long id_rol);

    public boolean desactivarPermisosPantallaPorRolId(Long id);

}