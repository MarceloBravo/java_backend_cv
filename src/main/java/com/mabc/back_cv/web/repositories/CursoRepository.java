package com.mabc.back_cv.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.mabc.back_cv.web.entities.Curso;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query(value = "SELECT * FROM cursos WHERE usuario_id = :userId", nativeQuery = true)
    public Page<Curso> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM cursos WHERE :userId IS NULL OR usuario_id = :userId", nativeQuery = true)
    public Page<Curso> getAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM cursos c
            JOIN users u ON c.usuario_id = u.id
            WHERE (:searchText IS NULL OR
                LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.instituto) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%'))
            )
            AND (:userId IS NULL OR c.usuario_id = :userId)
            """, nativeQuery = true)
    public Page<Curso> findBySearchText(@Param("userId") Long userId, @Param("searchText") String searchText, Pageable pageable);

}
