package com.mabc.back_cv.web.services.curso;

import com.mabc.back_cv.web.dto.CursoDTO;

import org.springframework.data.domain.Page;

/**
 * Interfaz de servicio para la gestión de cursos.
 * Define las operaciones CRUD disponibles para cursos.
 */
public interface CursoService {

    /**
     * Obtiene una página de cursos por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página.
     * @param size   Tamaño de página.
     * @return Página de CursoDTO.
     */
    public Page<CursoDTO> findByUserId(Long userId, Integer page, Integer size);

    /**
     * Obtiene una página de cursos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario.
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de CursoDTO.
     */
    public Page<CursoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    /**
     * Obtiene un curso por su identificador.
     *
     * @param id Identificador del curso.
     * @return CursoDTO encontrado o null si no existe.
     */
    public CursoDTO findById(Long id);

    /**
     * Crea o actualiza un curso.
     *
     * @param cursoDTO DTO del curso a guardar.
     * @return CursoDTO guardado.
     */
    public CursoDTO save(CursoDTO cursoDTO);

    /**
     * Elimina un curso por su identificador.
     *
     * @param id Identificador del curso a eliminar.
     */
    public void delete(Long id);

}
