package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Tecnologia;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnologiaRepository extends JpaRepository<Tecnologia, Long> {

    @Query(value = """ 
        SELECT * FROM tecnologias t 
        WHERE (:searchText IS NULL OR ( 
            LOWER(t.nombre) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.type) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.path_image) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.logo_svg) LIKE LOWER(CONCAT('%', :searchText, '%')) 
        ))
    """, nativeQuery = true)
    Page<Tecnologia> findAllPage(@Param("searchText") String searchText, Pageable pageable);
    
    @Query(value = """ 
        SELECT * FROM tecnologias t 
        WHERE (:searchText IS NULL OR ( 
            LOWER(t.nombre) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.type) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.path_image) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.logo_svg) LIKE LOWER(CONCAT('%', :searchText, '%')) 
        ))
    """, nativeQuery = true)
    List<Tecnologia> findAllList(@Param("searchText") String searchText);

}