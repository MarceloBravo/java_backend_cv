package com.mabc.back_cv.web.services.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.repositories.PortafolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de portafolio que maneja la lógica de negocio
 * relacionada con los portafolios de los usuarios.
 * Proporciona métodos para obtener, buscar, guardar y eliminar portafolios.
 */
@Service
public class PortafolioServiceImpl implements PortafolioService {

    @Autowired
    private PortafolioRepository portafolioRepository;
    

    /**
     * Obtiene un portafolio por su ID.
     * 
     * @param id ID del portafolio a obtener.
     * @return PortafolioDTO correspondiente al ID proporcionado o null si no se
     *         encuentra
     */
    @Override
    public PortafolioDTO getPortafolioById(Long id) {
        if (id == null) {
            return null;
        }
        return PortafolioMapper.convertToDTO(portafolioRepository.findById(id).orElse(null));
    }

    /**
     * Obtiene una página de portafolios filtrados por ID de usuario y texto de
     * búsqueda.
     * 
     * @param userId     ID del usuario para filtrar los portafolios (opcional).
     * @param searchText texto de búsqueda para filtrar los portafolios (opcional).
     * @param page       número de página para la paginación (opcional).
     * @param size       tamaño de la página para la paginación (opcional).
     * @return Página de PortafolioDTOs que cumplen con los criterios de búsqueda y
     *         paginación.
     */
    @Override
    public Page<PortafolioDTO> getPage(Long userId, String searchText, Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        return portafolioRepository.findBySearchText(userId, searchText, pageable).map(PortafolioMapper::convertToDTO);
    }

    /**
     * Obtiene el portafolio asociado a un usuario por su ID.
     * 
     * @param userId ID del usuario cuyo portafolio se desea obtener.
     * @return PortafolioDTO correspondiente al usuario proporcionado o null
     */
    @Override
    public PortafolioDTO getPortafolioByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return PortafolioMapper.convertToDTO(portafolioRepository.findByUserId(userId));
    }

    /**
     * Guarda un portafolio en la base de datos. Si el portafolio tiene un ID, se
     * actualizará el portafolio existente; de lo contrario, se creará uno nuevo.
     * 
     * @param portafolio DTO del portafolio a guardar.
     * @return PortafolioDTO guardado con su ID asignado o null si el DTO
     *         proporcionado es null.
     */
    @Override
    public PortafolioDTO savePortafolio(PortafolioDTO portafolio) {
        Portafolio portafolioEntity = PortafolioMapper.convertToEntity(portafolio);
        if (portafolioEntity == null || portafolioEntity.getUser() == null) {
            throw new IllegalArgumentException("Datos no válidos para guardar el portafolio.");
        }
        Portafolio savedPortafolio = portafolioRepository.save(portafolioEntity);
        return PortafolioMapper.convertToDTO(savedPortafolio);
    }

    /**
     * Elimina un portafolio por su ID. Si el ID es null o no existe un portafolio
     * con ese ID, se lanzará una IllegalArgumentException.
     * 
     * @param id ID del portafolio a eliminar.
     */
    @Override
    public void deletePortafolio(Long id) {
        if (id == null || !portafolioRepository.existsById(id)) {
            throw new IllegalArgumentException("Portafolio con id " + id + " no existe.");
        }
        portafolioRepository.deleteById(id);
    }

}