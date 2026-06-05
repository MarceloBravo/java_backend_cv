package com.mabc.back_cv.web.services.presentacion;

import com.mabc.back_cv.web.dto.PresentacionDTO;
import org.springframework.data.domain.Page;

/**
 * Contrato de servicio para la gestión de presentaciones del CV.
 *
 * <p>Define las operaciones de consulta, persistencia y eliminación de
 * {@link PresentacionDTO presentaciones}, con soporte de paginación y
 * filtrado por texto o usuario.</p>
 *
 * @author mabc
 */
public interface PresentacionService {

    /**
     * Recupera todas las presentaciones de forma paginada, sin aplicar filtros.
     *
     * @param page número de página (basado en cero); se usa {@code 0} si es {@code null} o negativo.
     * @param size cantidad de elementos por página; se usa {@code 10} si es {@code null} o negativo.
     * @return página de {@link PresentacionDTO} con los resultados encontrados.
     */
    public Page<PresentacionDTO> getPresentaciones(Long page, Long size);

    /**
     * Recupera las presentaciones cuyo párrafo contenga el texto indicado (búsqueda insensible
     * a mayúsculas/minúsculas), de forma paginada.
     *
     * @param parrafo texto a buscar dentro del campo párrafo.
     * @param page    número de página (basado en cero); se usa {@code 0} si es {@code null} o negativo.
     * @param size    cantidad de elementos por página; se usa {@code 10} si es {@code null} o negativo.
     * @return página de {@link PresentacionDTO} que coinciden con el filtro de texto.
     */
    public Page<PresentacionDTO> getPresentaciones(String parrafo, Long page, Long size);

    /**
     * Recupera las presentaciones de un usuario específico cuyo párrafo contenga el texto indicado
     * (búsqueda insensible a mayúsculas/minúsculas), de forma paginada.
     *
     * @param userId  identificador del usuario propietario de la presentación.
     * @param parrafo texto a buscar dentro del campo párrafo.
     * @param page    número de página (basado en cero); se usa {@code 0} si es {@code null} o negativo.
     * @param size    cantidad de elementos por página; se usa {@code 10} si es {@code null} o negativo.
     * @return página de {@link PresentacionDTO} que coinciden con ambos filtros.
     */
    public Page<PresentacionDTO> getPresentaciones(Long userId, String parrafo, Long page, Long size);

    /**
     * Obtiene la presentación asociada al usuario indicado.
     *
     * @param userId identificador del usuario cuya presentación se desea recuperar.
     * @return el {@link PresentacionDTO} correspondiente, o {@code null} si no existe
     *         o el {@code userId} es inválido.
     */
    public PresentacionDTO getPresentacionByUserId(Long userId);

    /**
     * Crea o actualiza una presentación.
     *
     * <p>Si el DTO contiene un {@code id} válido (mayor que cero), se actualiza el registro
     * existente; en caso contrario, se crea uno nuevo.</p>
     *
     * @param presentacion datos de la presentación a persistir; no puede ser {@code null}.
     * @return el {@link PresentacionDTO} guardado con los datos actualizados (incluyendo el
     *         {@code id} asignado en caso de inserción).
     * @throws IllegalArgumentException si {@code presentacion} es {@code null}.
     */
    public PresentacionDTO savePresentacion(PresentacionDTO presentacion);

    /**
     * Elimina la presentación asociada al identificador indicado.
     *
     * @param userId identificador de la presentación a eliminar.
     * @throws IllegalArgumentException si no existe ninguna presentación con ese identificador.
     */
    public void deletePresentacion(Long userId);
}