package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.PermisoPantalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PermisoPantallaRepository extends JpaRepository<PermisoPantalla, Long> {

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
