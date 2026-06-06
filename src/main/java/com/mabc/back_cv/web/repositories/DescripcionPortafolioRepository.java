package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.DescripcionPortafolio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para acceder a los datos de la entidad
 * {@link DescripcionPortafolio}.
 * Permite gestionar las operaciones de base de datos relacionadas con las
 * descripciones
 * de portafolio.
 */
@Repository
public interface DescripcionPortafolioRepository extends JpaRepository<DescripcionPortafolio, Long> {

    Page<DescripcionPortafolio> findByParrafoContainingIgnoreCase(String terminoBuscado, Pageable pageable);

    Optional<DescripcionPortafolio> findById(Long id);

}