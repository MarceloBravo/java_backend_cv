package com.mabc.back_cv.web.repositories;

import com.mabc.back_cv.web.entities.Rol;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link Rol}.
 * Permite gestionar las operaciones de base de datos relacionadas con los roles
 * de usuario.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su ID.
     *
     * @param id ID del rol a buscar.
     * @return Un {@link Optional} que contiene el rol encontrado, o vacío si no
     *         existe.
     */
    Optional<Rol> findById(Long id);

    /**
     * Busca un rol por su nombre exacto.
     *
     * @param nombre Nombre del rol a buscar (ej. "ADMIN", "USER").
     * @return Un {@link Optional} que contiene el rol encontrado, o vacío si no
     *         existe.
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Busca roles por nombre con coincidencia parcial (LIKE).
     *
     * @param nombre   Parte del nombre del rol a buscar.
     * @param pageable Información de paginación.
     * @return Una {@link Page} con todos los roles cuyos nombres contienen el texto
     *         proporcionado.
     */
    @Query(value = "SELECT * FROM rols WHERE nombre LIKE %:nombre%", nativeQuery = true)
    Page<Rol> searchByNombre(@Param("nombre") String nombre, Pageable pageable);

    @Query(value = "SELECT * FROM rols WHERE nombre LIKE %:nombre% AND activo = :activo", nativeQuery = true)
    Page<Rol> searchByNombreAndEstado(
            @Param("nombre") String nombre,
            @Param("activo") Boolean activo,
            Pageable pageable);

    @Query(value = "SELECT * FROM roles WHERE activo = true", nativeQuery = true)
    List<Rol> findByActiveState();

    /**
     * Busca todos los roles.
     *
     * @param pageable Información de paginación.
     * @return Una {@link Page} con todos los roles.
     */
    /**
     * Busca todos los roles.
     *
     * @return Una {@link List} con todos los roles.
     */
    List<Rol> findAll();

    /**
     * Busca todos los roles con paginación.
     *
     * @param pageable Información de paginación.
     * @return Una {@link Page} con todos los roles.
     */
    Page<Rol> findAll(Pageable pageable);

    void deleteById(Long id);
}
