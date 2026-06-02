package com.mabc.back_cv.web.services.permisoPantalla;

import java.util.List;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;


/**
 * Interfaz del servicio de permisos de pantalla.
 * 
 * Define los contratos para las operaciones de gestión de permisos
 * de pantalla asociados a roles y pantallas.
 */
public interface PermisoPantallaService {

    /**
     * Obtiene la lista de permisos de pantalla asociados a un rol.
     *
     * @param id_rol Identificador del rol.
     * @return Lista de permisos de pantalla en forma de DTO; lista vacía si el rol es nulo o no existen permisos.
     */
    public List<PermisoPantallaDTO> obtenerPermisosPantallaPorRol(Long id_rol);

    /**
     * Obtiene un permiso de pantalla asociado a un rol y una pantalla.
     *
     * @param id_rol Identificador del rol.
     * @param id_pantalla Identificador de la pantalla.
     * @param activo Indica si el permiso debe estar activo; si es null se considera true.
     * @return DTO del permiso encontrado, o null si no existe.
     */
    public PermisoPantallaDTO obtenerPermisoPantallaPorRolYPantalla(Long id_rol, Long id_pantalla, Boolean activo);

    /**
     * Almacena una lista de permisos de pantalla.
     *
     * @param permisosPantallaDTO Lista de DTOs de permisos de pantalla.
     * @return Cantidad de permisos procesados.
     */
    public int grabarPermisosPantallaPorRol(List<PermisoPantallaDTO> permisosPantallaDTO);

    /**
     * Elimina permisos de pantalla por rol.
     *
     * @param id_rol Identificador del rol.
     * @return true si se eliminaron permisos, false si no se encontraron.
     */
    public boolean eliminarPermisosPantallaPorRolId(Long id_rol);

    /**
     * Desactiva un permiso de pantalla por su identificador.
     *
     * @param id Identificador del permiso de pantalla.
     * @return true si se desactivó el permiso.
     */
    public boolean desactivarPermisosPantallaPorRolId(Long id);

}