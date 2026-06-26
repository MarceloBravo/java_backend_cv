package com.mabc.back_cv.web.services.Rol;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.RolDTO;

/**
 * Interfaz de servicio para la gestión de roles.
 * Define las operaciones CRUD disponibles para roles.
 */
public interface RolService {

    /**
     * Obtiene un rol por su identificador.
     *
     * @param id Identificador del rol.
     * @return RolDTO encontrado o null si no existe.
     */
    RolDTO findById(Long id);

    /**
     * Busca roles por nombre y estado activo con paginación.
     *
     * @param nombre Nombre del rol para filtrar.
     * @param activo Estado activo del rol.
     * @param page   Número de página.
     * @param rows   Cantidad de registros por página.
     * @return Página de RolDTO.
     */
    Page<RolDTO> searchBy(String nombre, Boolean activo, int page, int rows);

    /**
     * Obtiene la lista completa de todos los roles.
     *
     * @return Lista de RolDTO.
     */
    List<RolDTO> getAll();

    /**
     * Obtiene una página de todos los roles.
     *
     * @param page Número de página.
     * @param rows Cantidad de registros por página.
     * @return Página de RolDTO.
     */
    Page<RolDTO> getAll(int page, int rows);

    /**
     * Obtiene la lista de roles activos.
     *
     * @return Lista de RolDTO activos.
     */
    List<RolDTO> getActiveRoles();

    /**
     * Crea o actualiza un rol.
     *
     * @param rol DTO del rol a guardar.
     * @return RolDTO guardado.
     */
    RolDTO save(RolDTO rol);

    /**
     * Elimina un rol por su identificador.
     *
     * @param id Identificador del rol a eliminar.
     */
    void delete(Long id);
}
