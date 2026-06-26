package com.mabc.back_cv.web.services.certificado;

import com.mabc.back_cv.web.dto.CertificadoDTO;

import org.springframework.data.domain.Page;

/**
 * Interfaz de servicio para la gestión de certificados.
 * Define las operaciones CRUD disponibles para certificados.
 */
public interface CertificadoService {

    /**
     * Obtiene una página de certificados filtrados por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página.
     * @param size   Tamaño de página.
     * @return Página de CertificadoDTO.
     */
    public Page<CertificadoDTO> findByUserId(Long userId, Integer page, Integer size);

    /**
     * Obtiene una página de certificados filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario.
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página.
     * @param size       Tamaño de página.
     * @return Página de CertificadoDTO.
     */
    public Page<CertificadoDTO> findBySearchText(Long userId, String searchText, Integer page, Integer size);

    /**
     * Obtiene un certificado por su identificador.
     *
     * @param id Identificador del certificado.
     * @return CertificadoDTO encontrado o null si no existe.
     */
    public CertificadoDTO findById(Long id);

    /**
     * Crea o actualiza un certificado.
     *
     * @param certificadoDTO DTO del certificado a guardar.
     * @return CertificadoDTO guardado.
     */
    public CertificadoDTO save(CertificadoDTO certificadoDTO);

    /**
     * Elimina un certificado por su identificador.
     *
     * @param id Identificador del certificado a eliminar.
     */
    public void delete(Long id);

}
