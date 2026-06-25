package com.mabc.back_cv.web.services.presentacion;

import com.mabc.back_cv.web.dto.PresentacionDTO;
import com.mabc.back_cv.web.entities.Presentacion;
import org.springframework.stereotype.Component;

/**
 * Utilidades de conversión para la entidad {@link Presentacion} y su DTO {@link PresentacionDTO}.
 *
 * <p>Proporciona métodos estáticos para transformar objetos entre la capa de persistencia
 * y la capa de transferencia de datos, así como para el saneamiento de los campos de texto.</p>
 *
 * @author mabc
 */
@Component
public class PresentacionMapper {

    /**
     * Sanea el texto del párrafo eliminando espacios en blanco innecesarios.
     *
     * <p>Concretamente:
     * <ul>
     *   <li>Elimina espacios al inicio y al final de la cadena ({@code trim}).</li>
     *   <li>Reemplaza secuencias de espacios múltiples por un único espacio.</li>
     * </ul>
     * </p>
     *
     * @param parrafo texto a sanear; puede ser {@code null}.
     * @return el texto saneado, o {@code null} si el parámetro recibido es {@code null}.
     */
    private static String sanitizeParrafo(String parrafo) {
        if (parrafo == null) {
            return null;
        }
        // Eliminar espacios en blanco al inicio y al final
        parrafo = parrafo.trim();
        // Reemplazar múltiples espacios por uno solo
        parrafo = parrafo.replaceAll("\\s+", " ");
        return parrafo;
    }

    /**
     * Convierte una entidad {@link Presentacion} en su representación DTO {@link PresentacionDTO}.
     *
     * @param presentacion entidad a convertir; puede ser {@code null}.
     * @return el {@link PresentacionDTO} equivalente, o {@code null} si la entidad recibida
     *         es {@code null}.
     */
    public static PresentacionDTO entityToDTO(Presentacion presentacion) {
        if (presentacion == null) {
            return null;
        }
        PresentacionDTO presentacionDTO = new PresentacionDTO();
        presentacionDTO.setId(presentacion.getId());
        presentacionDTO.setParrafo(presentacion.getParrafo());
        presentacionDTO.setUser(presentacion.getUser());
        return presentacionDTO;
    }

    /**
     * Convierte un {@link PresentacionDTO} en la entidad {@link Presentacion} correspondiente.
     *
     * <p>Durante la conversión se aplica el saneamiento del párrafo mediante
     * {@link #sanitizeParrafo(String)}. El campo {@code id} solo se asigna si el DTO
     * contiene un valor mayor que cero (actualizaciones); de lo contrario, se omite
     * para permitir la generación automática del identificador (inserciones).</p>
     *
     * @param presentacionDTO DTO a convertir; puede ser {@code null}.
     * @return la entidad {@link Presentacion} equivalente, o {@code null} si el DTO
     *         recibido es {@code null}.
     */
    public static Presentacion dtoToEntity(PresentacionDTO presentacionDTO) {
        if (presentacionDTO == null) {
            return null;
        }
        Presentacion presentacion = new Presentacion();
        if(presentacionDTO.getId() != null && presentacionDTO.getId() > 0) {
            presentacion.setId(presentacionDTO.getId());
        }
        presentacion.setParrafo(sanitizeParrafo(presentacionDTO.getParrafo()));
        presentacion.setUser(presentacionDTO.getUser());
        return presentacion;
    }
}