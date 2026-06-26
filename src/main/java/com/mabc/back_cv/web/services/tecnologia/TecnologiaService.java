package com.mabc.back_cv.web.services.tecnologia;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.TecnologiaDTO;

/**
 * Interfaz de servicio para la gestión de tecnologías.
 * Define las operaciones CRUD disponibles para tecnologías.
 */
public interface TecnologiaService {

    /**
     * Obtiene una página de tecnologías filtradas por texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de TecnologiaDTO.
     */
    Page<TecnologiaDTO> findAll(String searchText, Integer page, Integer size);
    
    /**
     * Obtiene la lista completa de tecnologías filtradas por texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @return Lista de TecnologiaDTO.
     */
    List<TecnologiaDTO> findAll(String searchText);

    /**
     * Obtiene una tecnología por su identificador.
     *
     * @param id Identificador de la tecnología.
     * @return TecnologiaDTO encontrado o null si no existe.
     */
    TecnologiaDTO getById(Long id);

    /**
     * Crea o actualiza una tecnología.
     *
     * @param tecnologiaDTO DTO de la tecnología a guardar.
     * @return TecnologiaDTO guardado.
     */
    TecnologiaDTO save(TecnologiaDTO tecnologiaDTO);

    /**
     * Elimina una tecnología por su identificador.
     *
     * @param id Identificador de la tecnología a eliminar.
     */
    void deleteById(Long id);
    
}