package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link PermisoPantalla}.
 * Permite gestionar los permisos asociados a las pantallas y menús del sistema.
 */
public interface PermisoPantallaRepository extends JpaRepository<PermisoPantalla, Long> {

    /**
     * Busca y obtiene la lista de permisos de pantalla activos asociados a un rol específico
     * y a una URL de menú determinada. Realiza una carga optimizada (Fetch Join) de la pantalla y el menú.
     *
     * @param rolId Identificador único del rol.
     * @param url   URL del menú consultado.
     * @return Una lista de {@link PermisoPantalla} que cumplen con los criterios de búsqueda.
     */
    @Query(value = """
        SELECT 
	        pp.*
        FROM permisos_pantallas pp 
            JOIN roles r ON pp.rol_id = r.id 
            JOIN pantallas p ON pp.pantalla_id = p.id 
            JOIN menus m on p.menu_id = m.id 
        WHERE r.id = :rolId
            AND m.url = :url 
            AND p.activo = true
            AND m.activo = true
        AND (:activo IS NULL OR pp.activo = :activo);
    """, nativeQuery = true)
    List<PermisoPantalla> findAllByRolIdAndMenuUrlAndActive(@Param("rolId") Long rolId, @Param("url") String url, @Param("activo") Boolean activo);

    /**
     * Busca permisos de pantalla por rol, pantalla y estado activo.
     *
     * @param rolId Identificador único del rol.
     * @param pantallaId Identificador de la pantalla.
     * @param activo Estado de actividad del permiso.
     * @return Lista de permisos de pantalla que cumplen con los criterios.
     */
    List<PermisoPantalla> findAllByRolIdAndPantallaIdAndActivo(@Param("rolId") Long rolId, @Param("pantallaId") Long pantallaId, @Param("activo") Boolean activo);

    /**
     * Busca y obtiene la lista de permisos de pantalla activos asociados a un rol específico. Realiza una carga optimizada (Fetch Join) de la pantalla y el menú.
     * @param rolId Identificador único del rol.
     * @return Una lista de {@link PermisoPantallaDTO} que cumplen con los criterios
     */
    @Query(value = """
        SELECT 
            pp.id,
            r.id AS id_rol,
            p.id AS id_pantalla, 
            p.nombre_pantalla, 
            pp.accion_consultar, 
            pp.accion_crear,
            pp.accion_editar,
            pp.accion_eliminar,
            pp.activo 
        FROM pantallas p 
        LEFT JOIN permisos_pantallas pp ON p.id = pp.pantalla_id 
        LEFT JOIN roles r ON pp.rol_id = r.id 
        WHERE r.activo = true 
        AND p.activo = true 
        AND r.id = :rolId 
        ORDER BY p.nombre_pantalla ASC;
        """, nativeQuery = true)
    List<PermisoPantalla[]> findAllPermisosByRolId(@Param("rolId") Long rolId);


    @Query(value = """
        SELECT 
            pp.id,
            r.id AS id_rol,
            p.id AS id_pantalla, 
            p.nombre_pantalla, 
            pp.accion_consultar, 
            pp.accion_crear,
            pp.accion_editar,
            pp.accion_eliminar,
            pp.activo 
        FROM pantallas p 
        LEFT JOIN permisos_pantallas pp ON p.id = pp.pantalla_id 
        LEFT JOIN roles r ON pp.rol_id = r.id 
        WHERE r.activo = true 
        AND p.activo = true 
        AND p.id = :pantallaId
        ORDER BY p.nombre_pantalla ASC;
        """, nativeQuery = true)
    List<PermisoPantalla[]> findAllPermisosByPantallaId(@Param("pantallaId") Long pantallaId);

    /**
     * Find permisos by rol id using Spring Data derived query.
     */
    List<PermisoPantalla> findByRolId(@Param("rolId") Long rolId);

    /**
     * Find permisos by pantalla id using Spring Data derived query.
     */
    List<PermisoPantalla> findByPantallaId(@Param("pantallaId") Long pantallaId);


    @Modifying
    @Query(value = "UPDATE PermisoPantallas SET activo = false WHERE id = :id RETURNING *", nativeQuery = true)
    int deactivatePermisoPantallaById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE PermisoPantallas SET activo = false WHERE rol_id = :rolId", nativeQuery = true)
    int deactivatePermisoPantallaByRolId(@Param("rolId") Long rolId);

    @Modifying
    @Query(value = "UPDATE PermisoPantallas SET activo = false WHERE pantalla_id = :pantallaId", nativeQuery = true)
    int deactivatePermisoPantallaByPantallaId(@Param("pantallaId") Long pantallaId);

}
