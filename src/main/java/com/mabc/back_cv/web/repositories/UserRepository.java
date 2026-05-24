package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link User}.
 * Permite gestionar las operaciones de base de datos relacionadas con los usuarios.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca un usuario por su dirección de correo electrónico (email).
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío en caso contrario.
     */
    Optional<User> findByEmail(String email);
}