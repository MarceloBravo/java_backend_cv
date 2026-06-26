package com.mabc.back_cv.web.services.descripcionPortafolio;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Interfaz de servicio para la gestión de descripciones de portafolio.
 * Define las operaciones CRUD disponibles para las descripciones de portafolio.
 */
public interface DescripcionPortafolioService {

    /**
     * Obtiene la lista completa de todas las descripciones de portafolio.
     *
     * @return Lista de DescripcionPortafolioDTO.
     */
    public List<DescripcionPortafolioDTO> getAll();

    /**
     * Obtiene una página de descripciones de portafolio filtradas por término de búsqueda.
     *
     * @param terminoBuscado Término de búsqueda opcional.
     * @param page           Número de página.
     * @param size           Tamaño de página.
     * @return Página de DescripcionPortafolioDTO.
     */
    Page<DescripcionPortafolioDTO> getAll(String terminoBuscado, Integer page, Integer size);

    /**
     * Obtiene una descripción de portafolio por su identificador.
     *
     * @param id Identificador de la descripción.
     * @return DescripcionPortafolioDTO encontrado o null si no existe.
     */
    DescripcionPortafolioDTO getById(Long id);

    /**
     * Crea o actualiza una descripción de portafolio.
     *
     * @param detallePortafolioDTO DTO de la descripción a guardar.
     * @return DescripcionPortafolioDTO guardado.
     */
    DescripcionPortafolioDTO save(DescripcionPortafolioDTO detallePortafolioDTO);

    /**
     * Elimina una descripción de portafolio por su identificador.
     *
     * @param id Identificador de la descripción a eliminar.
     */
    void delete(Long id);

}
