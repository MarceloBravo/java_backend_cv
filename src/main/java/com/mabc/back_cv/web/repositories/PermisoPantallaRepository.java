package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.PermisoPantalla;
import org.springframework.data.jpa.repository.JpaRepository;
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
    @Query("""
        SELECT pp FROM PermisoPantalla pp
        JOIN pp.rol r
        JOIN FETCH pp.pantalla p
        JOIN FETCH p.menu m
        WHERE r.id = :rolId
          AND m.url = :url
          AND pp.activo = true
          AND p.activo = true
          AND m.activo = true
    """)
    List<PermisoPantalla> findAllActiveByRolIdAndMenuUrl(@Param("rolId") Long rolId, @Param("url") String url);
}
