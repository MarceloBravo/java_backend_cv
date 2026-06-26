package com.mabc.back_cv.web.services.educacion;

import com.mabc.back_cv.web.dto.EducacionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mabc.back_cv.web.entities.Educacion;


/**
 * Interfaz de servicio para la gestión de educación.
 * Define las operaciones CRUD disponibles para registros educativos.
 */
public interface EducacionService{

    /**
     * Obtiene una página de registros educativos por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página.
     * @param size   Tamaño de página.
     * @return Página de EducacionDTO.
     */
    public Page<EducacionDTO> findByUserId(Long userId, Integer page, Integer size);

    /**
     * Obtiene una página de registros educativos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario.
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de EducacionDTO.
     */
    public Page<EducacionDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    /**
     * Obtiene un registro educativo por su identificador.
     *
     * @param id Identificador del registro.
     * @return EducacionDTO encontrado o null si no existe.
     */
    public EducacionDTO findById(Long id);

    /**
     * Crea o actualiza un registro educativo.
     *
     * @param educacion DTO del registro a guardar.
     * @return EducacionDTO guardado.
     */
    public EducacionDTO save(EducacionDTO educacion);

    /**
     * Elimina un registro educativo por su identificador.
     *
     * @param id Identificador del registro a eliminar.
     */
    public void delete(Long id);

}