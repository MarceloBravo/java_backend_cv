package com.mabc.back_cv.web.services.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Clase de utilidad para el servicio de portafolio que proporciona métodos
 * estáticos para crear objetos Pageable y convertir entre entidades Portafolio
 * y DTOs PortafolioDTO. Esta clase ayuda a centralizar la lógica de conversión
 * y creación de objetos relacionados con los portafolios, facilitando su uso en
 * el servicio y controlador.
 * Proporciona métodos para:
 * - Crear un objeto Pageable a partir de parámetros de página y tamaño.
 * - Convertir una entidad Portafolio a un DTO PortafolioDTO.
 * - Convertir un DTO PortafolioDTO a una entidad Portafolio.
 */
@Component
public class PortafolioUtils {

    /**
     * Crea un objeto Pageable a partir de los parámetros de página y tamaño. Si los
     * parámetros son null o inválidos, se asignan valores predeterminados (página 0
     * y tamaño 10).
     * 
     * @param page número de página para la paginación (opcional).
     * @param size tamaño de la página para la paginación (opcional).
     * @return Pageable correspondiente a los parámetros proporcionados.
     * @throws IllegalArgumentException si los parámetros son inválidos.
     */
    public static Pageable createPageable(Integer page, Integer size) {
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0) ? 10 : size;
        return PageRequest.of(page, size);
    }

    /**
     * Convierte una entidad Portafolio a un DTO PortafolioDTO.
     * 
     * @param portafolio entidad Portafolio a convertir.
     * @return PortafolioDTO correspondiente a la entidad proporcionada o null si la
     *         entidad es null.
     */
    public static PortafolioDTO convertToDTO(Portafolio portafolio) {
        if (portafolio == null) {
            return null;
        }
        return new PortafolioDTO(
                portafolio.getId(),
                portafolio.getTitle(),
                portafolio.getImage(),
                portafolio.getVideo(),
                portafolio.getMouseMoveTitle(),
                portafolio.getMouseMoveDescription(),
                portafolio.getParagraph(),
                portafolio.getLink(),
                portafolio.getUser());
    }

    /**
     * Convierte un DTO PortafolioDTO a una entidad Portafolio. Si el DTO es null,
     * se devuelve null. Si el DTO tiene un ID, se asigna a la entidad; de lo
     * contrario, se deja como null para que se genere automáticamente al guardar.
     * 
     * @param portafolioDTO DTO PortafolioDTO a convertir.
     * @return Portafolio correspondiente al DTO proporcionado o null si el DTO es
     *         null.
     */
    public static Portafolio convertToEntity(PortafolioDTO portafolioDTO) {
        if (portafolioDTO == null) {
            return null;
        }
        Portafolio portafolio = new Portafolio();
        if (portafolioDTO.getId() != null) {
            portafolio.setId(portafolioDTO.getId());
        }
        portafolio.setTitle(portafolioDTO.getTitle());
        portafolio.setImage(portafolioDTO.getImage());
        portafolio.setVideo(portafolioDTO.getVideo());
        portafolio.setMouseMoveTitle(portafolioDTO.getMouseMoveTitle());
        portafolio.setMouseMoveDescription(portafolioDTO.getMouseMoveDescription());
        portafolio.setParagraph(portafolioDTO.getParagraph());
        portafolio.setLink(portafolioDTO.getLink());
        portafolio.setUser(portafolioDTO.getUser());

        return portafolio;
    }
}