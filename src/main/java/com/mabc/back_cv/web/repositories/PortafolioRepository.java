package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Portafolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {

    public Portafolio findByUserId(Long userId);

    public Portafolio findByTitle(String title);

    @Query("""
                SELECT p
                    FROM Portafolio p
                WHERE (:userId IS NULL OR p.user.id = :userId)
                AND (:searchText IS NULL OR
                    (
                        LOWER(p.paragraph) LIKE LOWER(CONCAT('%', :searchText, '%'))
                        OR LOWER(p.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
                        OR LOWER(p.mouseMoveTitle) LIKE LOWER(CONCAT('%', :searchText, '%'))
                        OR LOWER(p.mouseMoveDescription) LIKE LOWER(CONCAT('%', :searchText, '%'))
                        OR LOWER(p.link) LIKE LOWER(CONCAT('%', :searchText, '%'))
                    )
                )
            """)
    public Page<Portafolio> findBySearchText(@Param("userId") Long userId, @Param("searchText") String searchText,
            Pageable pageable);

}