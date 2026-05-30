package com.mabc.back_cv.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import com.mabc.back_cv.web.entities.Pantalla;

@Repository
public interface PantallaRepository extends JpaRepository<Pantalla, Long>{

    Optional<Pantalla> findById(Long id);

    @Query(value = "SELECT * FROM pantallas WHERE menu_id = ?1", nativeQuery = true)
    List<Pantalla> findByMenuId(@Param("menuId") Long menuId);

        /**
         * Busca pantallas por su nombre, URL del archivo de la pantalla o nombre del menú asociado a la pantalla 
         *  utilizando una consulta nativa retornando una página de resultados.
         */
       @Query(value = """
            SELECT * FROM pantallas p
            JOIN menus m ON p.menu_id = m.id 
            WHERE p.activo = :estado
            AND (p.nombre_pantalla LIKE %:terminoBuscado% 
            OR p.url_archivo LIKE %:terminoBuscado% 
            OR m.nombre LIKE %:terminoBuscado%)
             """, nativeQuery = true)
        Page<Pantalla> searchByNombrePantallaUrlArchivoOrMenu(
            @Param("terminoBuscado") String terminoBuscado, 
            @Param("estado") Boolean estado,
            Pageable pageable
        );  


        /**
         * Busca pantallas por su nombre, URL del archivo de la pantalla o nombre del menú asociado a la pantalla 
         *  utilizando una consulta nativa retornando una lista de resultados.
         */
       @Query(value = """
            SELECT * FROM pantallas p
            JOIN menus m ON p.menu_id = m.id 
            WHERE p.activo = :estado
            AND (p.nombre_pantalla LIKE %:terminoBuscado% 
            OR p.url_archivo LIKE %:terminoBuscado% 
            OR m.nombre LIKE %:terminoBuscado%)
             """, nativeQuery = true)
        List<Pantalla> searchByNombrePantallaUrlArchivoOrMenu(
            @Param("terminoBuscado") String terminoBuscado, 
            @Param("estado") Boolean estado
        );  
}