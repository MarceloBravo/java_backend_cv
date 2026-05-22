package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


public interface AuthorizationRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
}