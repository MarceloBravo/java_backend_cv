package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Trabajo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long>{

    @Query(value = """
        SELECT * FROM Trabajo t
        JOIN users u ON t.user_id = u.id
        LEFT JOIN Tecnologia tec ON t.tecnologia_id = tec.id
        WHERE (:searchText IS NULL OR
            LOWER(t.company) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.position) LIKE LOWER(CONCAT('%', :searchText, '%'))  
            OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.startDate) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.endDate) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.current) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.type) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.path_image) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.logo_svg) LIKE LOWER(CONCAT('%', :searchText, '%'))
            )
            AND (:userId IS NULL OR t.user_id = :userId)
    """, nativeQuery = true)
    List<Trabajo> findAllList(@Param("userId") Long userId, @Param("searchText") String searchText);

    @Query(value = """
        SELECT * FROM Trabajo t 
        LEFT JOIN Tecnologia tec ON t.tecnologia_id = tec.id 
        WHERE (:userId IS NULL OR t.user_id = :userId)
        AND (:searchText IS NULL OR ( 
            LOWER(t.company) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.position) LIKE LOWER(CONCAT('%', :searchText, '%'))  
            OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchText, '%')) 
            OR LOWER(t.startDate) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.endDate) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(t.current) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.type) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.path_image) LIKE LOWER(CONCAT('%', :searchText, '%'))
            OR LOWER(te.logo_svg) LIKE LOWER(CONCAT('%', :searchText, '%'))
        ))
        """, nativeQuery = true)
    Page<Trabajo> findAllPage(@Param("userId") Long userId, @Param("searchText") String searchText, Pageable pageable);

    
}