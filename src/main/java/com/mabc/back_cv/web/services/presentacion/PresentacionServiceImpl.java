package com.mabc.back_cv.web.services.presentacion;

import com.mabc.back_cv.web.entities.Presentacion;
import com.mabc.back_cv.web.repositories.PresentacionRepository;
import com.mabc.back_cv.web.dto.PresentacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.mabc.back_cv.web.services.presentacion.PresentacionMapper.dtoToEntity;
import static com.mabc.back_cv.web.services.presentacion.PresentacionMapper.entityToDTO;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación de {@link PresentacionService} que gestiona las operaciones
 * de negocio relacionadas con las presentaciones del CV.
 *
 * <p>Utiliza {@link PresentacionRepository} para el acceso a datos y
 * {@link PresentacionMapper} para la conversión entre entidades y DTOs.</p>
 *
 * @author mabc
 * @see PresentacionService
 * @see PresentacionMapper
 */
@Service
public class PresentacionServiceImpl implements PresentacionService {

    /** Repositorio JPA para el acceso a datos de {@link Presentacion}. */
    @Autowired
    private PresentacionRepository presentacionRepository;
    

    /**
     * {@inheritDoc}
     *
     * <p>Los valores de {@code page} y {@code size} se normalizan automáticamente:
     * si son {@code null} o negativos, se utilizan {@code 0} y {@code 10} respectivamente.</p>
     */
    @Override
    public Page<PresentacionDTO> getPresentaciones(Integer page, Integer size) {
        Pageable pageable = Utils.createPageable(page, size);
        Page<Presentacion> presentaciones = presentacionRepository.findAll(pageable);
        return presentaciones.map(PresentacionMapper::entityToDTO);
    }

    /**
     * {@inheritDoc}
     *
     * <p>La búsqueda se realiza con {@code LIKE} insensible a mayúsculas/minúsculas sobre
     * el campo párrafo. Los valores de {@code page} y {@code size} se normalizan
     * automáticamente si son {@code null} o negativos.</p>
     */
    @Override
    public Page<PresentacionDTO> getPresentaciones(String parrafo, Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        Page<Presentacion> presentaciones = presentacionRepository.findByParrafoContainingIgnoreCase(parrafo, pageable);
        return presentaciones.map(PresentacionMapper::entityToDTO);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Combina el filtro por {@code userId} y la búsqueda de texto en el párrafo
     * (insensible a mayúsculas/minúsculas). Los valores de {@code page} y {@code size}
     * se normalizan automáticamente si son {@code null} o negativos.</p>
     */
    @Override
    public Page<PresentacionDTO> getPresentaciones(Long userId, String parrafo, Integer page, Integer size){
        Pageable pageable = Utils.createPageable(page, size);
        Page<Presentacion> presentaciones = presentacionRepository.findByUserIdAndParrafoContainingIgnoreCase(userId, parrafo, pageable);
        return presentaciones.map(PresentacionMapper::entityToDTO);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Devuelve {@code null} si {@code userId} es {@code null} o negativo,
     * o si no existe una presentación asociada a dicho usuario.</p>
     */
    @Override
    public PresentacionDTO getPresentacionByUserId(Long userId){
        if(userId == null || userId < 0) {
            return null;
        }
        return PresentacionMapper.entityToDTO(presentacionRepository.findByUserId(userId));
    }

    /**
     * {@inheritDoc}
     *
     * <p>El párrafo del DTO es saneado por {@link PresentacionMapper#dtoToEntity(PresentacionDTO)}
     * antes de la persistencia (eliminación de espacios redundantes).</p>
     *
     * @throws IllegalArgumentException si la conversión del DTO produce una entidad {@code null}.
     */
    @Override
    public PresentacionDTO savePresentacion(PresentacionDTO presentacion) {
        Presentacion presentacionEntity = PresentacionMapper.dtoToEntity(presentacion);
        if(presentacionEntity == null){
            throw new IllegalArgumentException("La presentación no puede ser nula.");
        }
        return PresentacionMapper.entityToDTO(presentacionRepository.save(presentacionEntity));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Busca la presentación por {@code userId} antes de eliminarla. Si no existe,
     * lanza una {@link IllegalArgumentException} con el identificador recibido.</p>
     *
     * @throws IllegalArgumentException si no existe presentación para el {@code userId} indicado.
     */
    @Override
    public void deletePresentacion(Long userId) {
        Presentacion presentacion = presentacionRepository.findByUserId(userId);
        if (presentacion == null) {
            throw new IllegalArgumentException("No se encontró una presentación para el userId: " + userId);
        }
        presentacionRepository.delete(presentacion);
    }

}
