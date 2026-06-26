package com.mabc.back_cv.web.services.contenidoCurso;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.ContenidoCurso;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de contenido de cursos.
 * Define las operaciones CRUD disponibles para el contenido de cursos.
 */
public interface ContenidoCursoService{

    /**
     * Obtiene la lista de contenidos de curso filtrados por texto y estado activo.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param activo     Estado activo opcional.
     * @return Lista de ContenidoCursoDTO.
     */
    public List<ContenidoCursoDTO> findAllList(String searchText, Boolean activo);

    /**
     * Obtiene una página de contenidos de curso con filtros y paginación.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @param activo     Estado activo opcional.
     * @return Página de ContenidoCursoDTO.
     */
    public Page<ContenidoCursoDTO> findAllPage(String searchText, Integer page, Integer size, Boolean activo);

    /**
     * Obtiene un contenido de curso por su identificador.
     *
     * @param id Identificador del contenido.
     * @return ContenidoCursoDTO encontrado o null si no existe.
     */
    public ContenidoCursoDTO getById(Long id);

    /**
     * Crea o actualiza un contenido de curso.
     *
     * @param contenidoCurso DTO del contenido a guardar.
     * @return ContenidoCursoDTO guardado.
     */
    public ContenidoCursoDTO save(ContenidoCursoDTO contenidoCurso);

    /**
     * Elimina un contenido de curso por su identificador.
     *
     * @param id Identificador del contenido a eliminar.
     */
    public void delete(Long id);
    
}