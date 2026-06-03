package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link User}.
 * Permite gestionar las operaciones de base de datos relacionadas con los
 * usuarios.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico (email).
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío
     *         en caso contrario.
     */
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("""
            SELECT u FROM User u
            JOIN u.rol r
            WHERE u.activo = :active
                AND (
                    u.nombre LIKE %:filter%
                    OR u.apellido LIKE %:filter%
                    OR u.email LIKE %:filter%
                    OR u.fono LIKE %:filter%
                    OR u.direccion LIKE %:filter%
                    OR u.ciudad LIKE %:filter%
                    OR u.idioma LIKE %:filter%
                    OR r.nombre LIKE %:filter%
                )
            """)
    List<User> findAllFilteres(@Param("filter") String filter, boolean active);

    @Query("""
            SELECT u FROM User u
            JOIN u.rol r
            WHERE u.activo = :active
                AND (
                    u.nombre LIKE %:filter%
                    OR u.apellido LIKE %:filter%
                    OR u.email LIKE %:filter%
                    OR u.fono LIKE %:filter%
                    OR u.direccion LIKE %:filter%
                    OR u.ciudad LIKE %:filter%
                    OR u.idioma LIKE %:filter%
                    OR r.nombre LIKE %:filter%
                )
            """)
    Page<User> findByFilter(@Param("filter") String filter, Pageable pageable);
}