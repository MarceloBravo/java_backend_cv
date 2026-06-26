package com.mabc.back_cv.web.services.trabajo;

import com.mabc.back_cv.web.dto.TrabajoDTO;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.TecnologiaDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de trabajos o experiencias laborales.
 * Define las operaciones CRUD disponibles para trabajos.
 */
public interface TrabajoService{
    
    /**
     * Obtiene la lista de trabajos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario.
     * @param searchText Texto de búsqueda.
     * @return Lista de TrabajoDTO.
     */
    List<TrabajoDTO> getAll(Long userId, String searchText);

    /**
     * Obtiene una página de trabajos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario.
     * @param searchText Texto de búsqueda.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de TrabajoDTO.
     */
    Page<TrabajoDTO> getAll(Long userId, String searchText, Integer page, Integer size);

    /**
     * Obtiene un trabajo por su identificador.
     *
     * @param id Identificador del trabajo.
     * @return TrabajoDTO encontrado o null si no existe.
     */
    TrabajoDTO getById(Long id);
    
    /**
     * Crea o actualiza un trabajo.
     *
     * @param trabajo DTO del trabajo a guardar.
     * @return TrabajoDTO guardado.
     */
    TrabajoDTO save(TrabajoDTO trabajo);

    /**
     * Elimina un trabajo por su identificador.
     *
     * @param id Identificador del trabajo a eliminar.
     */
    void deleteById(Long id);

}