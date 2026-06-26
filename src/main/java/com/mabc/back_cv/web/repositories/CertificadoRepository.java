package com.mabc.back_cv.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.mabc.back_cv.web.entities.Certificado;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    @Query(value = "SELECT * FROM certificados WHERE user_id = :userId", nativeQuery = true)
    public Page<Certificado> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM certificados WHERE :userId IS NULL OR user_id = :userId", nativeQuery = true)
    public Page<Certificado> getAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM certificados c
            JOIN users u ON c.user_id = u.id
            WHERE (:searchText IS NULL OR
                LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.url_imagen) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.url_certificado) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.texto_mouse) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(c.texto_mouse_descripcion) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :searchText, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%'))
            )
            AND (:userId IS NULL OR c.user_id = :userId)
            """, nativeQuery = true)
    public Page<Certificado> findBySearchText(@Param("userId") Long userId, @Param("searchText") String searchText, Pageable pageable);

}
