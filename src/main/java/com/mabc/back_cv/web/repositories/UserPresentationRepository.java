package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.UserPresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface UserPresentationRepository extends JpaRepository<UserPresentation, Long> {

    Optional<UserPresentation> findById(Long id);

    @Query(value = """
        SELECT * FROM uer_presentation up 
        JOIN users u  
        WHERE :userId IS NULL OR up.user_id = :userId
        AND (:searchText IS NULL OR up.parrafo LIKE %:searchText% OR u.nombre LIKE %:searchText% OR u.apellido LIKE %:searchText%)
        ORDER BY up.posicion ASC
    """, nativeQuery = true)
    Page<UserPresentation> findAll(@Param("searchText") String searchText, @Param("userId") Long userId, Pageable pageable);
}