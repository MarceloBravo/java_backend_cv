package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.ContenidoCurso;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ContenidoCursoRepository extends JpaRepository<ContenidoCurso, Long>{

    @Query(value = """
        SELECT cc FROM ContenidoCurso cc 
        WHERE (:activo IS NULL OR cc.activo = :activo) AND  
            (:searchText IS NULL OR
                (
                    LOWER(cc.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR 
                    LOWER(cc.description) LIKE LOWER(CONCAT('%', :searchText, '%'))
                )
            )
    """)
    Page<ContenidoCurso> findAllPage(
        @Param("searchText") String searchText, 
        @Param("activo") Boolean activo, 
        Pageable pageable
    );
    
    @Query(value = """
        SELECT cc FROM ContenidoCurso cc 
        WHERE (:activo IS NULL OR cc.activo = :activo) AND  
            (:searchText IS NULL OR 
                (                
                    LOWER(cc.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR 
                    LOWER(cc.description) LIKE LOWER(CONCAT('%', :searchText, '%'))
                )
            )
    """)
    List<ContenidoCurso> findAllList(@Param("searchText") String searchText, @Param("activo") Boolean activo);
}