package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository 
public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {
    
    public Presentacion findByUserId(Long userId);

    public Page<Presentacion> findByParrafoContainingIgnoreCase(String parrafo, Pageable pageable);

    public Page<Presentacion> findByUserIdAndParrafoContainingIgnoreCase(Long userId, String parrafo, Pageable pageable);
}