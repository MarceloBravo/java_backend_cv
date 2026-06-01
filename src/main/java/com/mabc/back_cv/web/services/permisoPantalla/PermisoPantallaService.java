package com.mabc.back_cv.web.services.permisoPantalla;

import java.util.List;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;


public interface PermisoPantallaService {

    public List<PermisoPantallaDTO> obtenerPermisosPantallaPorRol(Long id_rol);

    public List<PermisoPantallaDTO> obtenerPermisosPantallaPorPantalla(Long id_pantalla);

    public PermisoPantallaDTO obtenerPermisoPantallaPorId(Long id);

    public PermisoPantallaDTO obtenerPermisoPantallaPorRolYPantalla(Long id_rol, Long id_pantalla, Boolean activo);

    public int grabarPermisosPantalla(List<PermisoPantallaDTO> permisosPantallaDTO);

    public int eliminarPermisosPantalla(List<PermisoPantallaDTO> permisosPantallaDTO);

    public boolean eliminarPermisosPantallaPorId(Long id);

    public boolean eliminarPermisosPantallaPorRolId(Long id_rol);

    public boolean eliminarPermisosPantallaPorPantallaId(Long id_pantalla);

    public boolean desactivarPermisosPantallaPorId(Long id);
    
    public boolean desactivarPermisosPantallaPorRolId(Long id_rol);
    
    public boolean desactivarPermisosPantallaPorPantallaId(Long id_pantalla);

}