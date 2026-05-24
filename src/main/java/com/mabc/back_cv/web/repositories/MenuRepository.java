package com.mabc.back_cv.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.mabc.back_cv.web.entities.Menu;

/**
 * Repositorio JPA para la entidad Menu.
 * Proporciona métodos para interactuar con la base de datos, incluyendo consultas
 * nativas personalizadas para gestionar jerarquías, roles, permisos y validaciones de recursividad.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Consulta y devuelve todos los menús de la base de datos ordenados por su campo orden.
     *
     * @return Lista de menús ordenados por orden ascendente.
     */
    @Query(value = "SELECT * FROM menus ORDER BY orden ASC", nativeQuery = true)
    List<Menu> findAllMenus();

    /**
     * Obtiene todos los menús que tienen su estado activo en true.
     *
     * @return Lista de menús activos ordenados por orden ascendente.
     */
    @Query(value = "SELECT * FROM menus WHERE activo = true ORDER BY orden ASC", nativeQuery = true)
    List<Menu> findByActivoTrue();

    /**
     * Obtiene los menús principales para un rol específico.
     * Estos menús son activos y o bien no tienen un menú padre, o bien no requieren
     * un permiso específico para dicho rol.
     *
     * @param rolId Identificador único del rol.
     * @return Lista de menús principales ordenados por orden ascendente.
     */
    @Query(value = """
            SELECT * FROM menus m
            WHERE m.activo = true
                AND (m.menu_padre_id IS NULL
                OR NOT EXISTS (
                    SELECT 1 FROM permisos p
                    WHERE p.menu_id = m.id
                    AND p.rol_id = ?1
                    )
                )
            ORDER BY m.orden ASC
            """, nativeQuery = true)
    List<Menu> findMainMenusByRolId(@Param("rolId") Long rolId);

    /**
     * Obtiene los submenús activos para un menú padre determinado y un rol de usuario específico.
     *
     * @param menuId Identificador único del menú padre.
     * @param rolId  Identificador único del rol.
     * @return Lista de submenús ordenados por orden ascendente.
     */
    @Query(value = """
            SELECT m.* FROM menus m
            JOIN permisos p ON p.menu_id = m.id
            WHERE m.activo = true
                AND p.rol_id = :rolId
                AND m.menu_padre_id = :menuId
            ORDER BY m.orden ASC
            """, nativeQuery = true)
    List<Menu> findSubMenusByMenuAndRolId(@Param("menuId") Long menuId, @Param("rolId") Long rolId);

    /**
     * Realiza una consulta recursiva para verificar si el menú indicado forma un ciclo
     * jerárquico de paternidad en la base de datos.
     *
     * @param menuId Identificador del menú a verificar.
     * @return 1 si existe una relación recursiva circular de paternidad, o null de lo contrario.
     */
    @Query(value = """
            WITH RECURSIVE menu_recursivo AS (
                SELECT menu_padre_id, id FROM menus WHERE id = :menuId
            UNION ALL
                SELECT m.menu_padre_id, m.id FROM menus m
                INNER JOIN menu_recursivo mr ON m.id = mr.menu_padre_id
            )
            SELECT 1 FROM menus m
            WHERE m.id IN (SELECT menu_padre_id FROM menu_recursivo)
            LIMIT 1
            """, nativeQuery = true)
    Byte hasRecursiveParent(@Param("menuId") Long menuId);

    /**
     * Consulta y retorna la lista de menús activos que no generarían recursividad
     * cíclica si fuesen elegidos como padre del menú indicado.
     *
     * @param menuId Identificador del menú de referencia.
     * @return Lista de menús aptos para ser asignados como menú padre.
     */
    @Query(value = """
            SELECT *
            FROM menus m
            WHERE m.activo = true
                AND m.id <> :menuId
                AND m.id NOT IN (
                    WITH RECURSIVE menu_recursivo AS (
                        SELECT menu_padre_id, id FROM menus WHERE id = :menuId
                    UNION ALL
                        SELECT m.menu_padre_id, m.id FROM menus m
                    INNER JOIN menu_recursivo mr ON m.id = mr.menu_padre_id
                )
                SELECT menu_padre_id FROM menu_recursivo
            )
            ORDER BY m.orden ASC
            """, nativeQuery = true)
    List<Menu> getNoRecursiveMenusOptions(@Param("menuId") Long menuId);
}
