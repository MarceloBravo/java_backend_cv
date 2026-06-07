package com.mabc.back_cv.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Educacion;

@Repository
public class EducacionRepository extends JpaRepository<Educacion, Long>{

    @Query(value = "SELECT * FROM educacion WHERE :userId IS NULL OR user_id = :userId", nativeQuery = true)
    public Page<Educacion> gelAllByUserId(@Param Long userId, @Param Pageable pageable);

    @Query(value = "SELECT * FROM educacion WHERE user_id = :userId", nativeQuery = true)
    public Page<Educacion> findByUserId(@Param Long userId, @Param Pageable pageable);

    @Query(value = """
            SELECT * 
            FROM educacion e
            JOIN users u ON e.user_id = u.id 
            WHERE (:searchText IS NULL OR
                LOWER(e.institution) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.short_title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.description) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.year_from) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.year_to) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.duration) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.image) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.url) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(e.styles) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.fono) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.direccion) LIKE LOWER(CONCAT('%', :searchText, '%'))       
                OR LOWER(u.ciudad) LIKE LOWER(CONCAT('%', :searchText, '%'))       
                OR LOWER(u.idioma) LIKE LOWER(CONCAT('%', :searchText, '%'))       
            )
            AND (:userId IS NULL OR e.user_id = :userId)
            """, nativeQuery = true)
    public Page<Educacion> findBySearchText(@Param Long userId, @Param String searchText, @Param Pageable pageable);

}