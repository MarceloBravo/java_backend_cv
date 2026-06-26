package com.mabc.back_cv.web.services.userPresentation;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import org.springframework.data.domain.Page;

/**
 * Interfaz de servicio para la gestión de presentaciones de usuario.
 * Define las operaciones disponibles para las presentaciones de usuario.
 */
public interface UserPresentationService{

    /**
     * Obtiene una página de presentaciones de usuario con filtros opcionales.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param userId     Identificador del usuario opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de UserPresentationDTO.
     */
    public Page<UserPresentationDTO> getAll(String searchText, Long userId, Integer page, Integer size);

    /**
     * Obtiene una presentación de usuario por su identificador.
     *
     * @param id Identificador de la presentación.
     * @return UserPresentationDTO encontrado o null si no existe.
     */
    public UserPresentationDTO findById(Long id);

    /**
     * Crea o actualiza una presentación de usuario.
     *
     * @param userPresentation DTO de la presentación a guardar.
     * @return UserPresentationDTO guardado.
     */
    public UserPresentationDTO save(UserPresentationDTO userPresentation);

    /**
     * Elimina una presentación de usuario por su identificador.
     *
     * @param id Identificador de la presentación a eliminar.
     */
    public void delete(Long id);
}