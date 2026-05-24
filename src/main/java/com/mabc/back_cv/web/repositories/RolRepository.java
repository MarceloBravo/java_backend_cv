package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link Rol}.
 * Permite gestionar las operaciones de base de datos relacionadas con los roles de usuario.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre exacto.
     *
     * @param nombre Nombre del rol a buscar (ej. "ADMIN", "USER").
     * @return Un {@link Optional} que contiene el rol encontrado, o vacío si no existe.
     */
    Optional<Rol> findByNombre(String nombre);
}
