package com.mabc.back_cv.web.services.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import org.springframework.data.domain.Page;

/**
 * Interfaz de servicio para la gestión de portafolios.
 * Define las operaciones CRUD disponibles para portafolios de usuarios.
 */
public interface PortafolioService {

    /**
     * Crea o actualiza un portafolio.
     *
     * @param portafolio DTO del portafolio a guardar.
     * @return PortafolioDTO guardado.
     */
    public PortafolioDTO savePortafolio(PortafolioDTO portafolio);

    /**
     * Obtiene un portafolio por su identificador.
     *
     * @param id Identificador del portafolio.
     * @return PortafolioDTO encontrado o null si no existe.
     */
    public PortafolioDTO getPortafolioById(Long id);

    /**
     * Obtiene el portafolio asociado a un usuario.
     *
     * @param userId Identificador del usuario.
     * @return PortafolioDTO encontrado o null si no existe.
     */
    public PortafolioDTO getPortafolioByUserId(Long userId);

    /**
     * Obtiene una página de portafolios filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario (opcional).
     * @param searchText Texto de búsqueda (opcional).
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de PortafolioDTO.
     */
    public Page<PortafolioDTO> getPage(Long userId, String searchText, Integer page, Integer size);

    /**
     * Elimina un portafolio por su identificador.
     *
     * @param id Identificador del portafolio a eliminar.
     */
    public void deletePortafolio(Long id);
}