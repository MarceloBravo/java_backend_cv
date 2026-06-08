package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.UserPresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserPresentationRepository extends JpaRepository<UserPresentationRepository, Long> {

    @Query(value = """
        SELECT * FROM uer_presentation up 
        JOIN users u  
        WHERE :userId IS NULL OR up.user_id = :userId
        AND (:searchText IS NULL OR up.parrafo LIKE %:searchText% OR u.nombre LIKE %:searchText% OR u.apellido LIKE %:searchText%)
        ORDER BY up.posicion ASC
    """, nativeQuery = true)
    Page<UserPresentationDTO> findAll(@Param("searchText") String searchText, @Param("userId") Long userId, Paeable pageable);
}