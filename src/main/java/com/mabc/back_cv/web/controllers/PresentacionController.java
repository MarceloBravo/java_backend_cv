package com.mabc.back_cv.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;

import com.mabc.back_cv.web.dto.PresentacionDTO;
import com.mabc.back_cv.web.services.presentacion.PresentacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Controlador REST para la gestión de presentaciones del CV.
 *
 * <p>Expone los endpoints bajo la ruta base {@code /api/presentacion} y delega
 * la lógica de negocio a {@link PresentacionService}.</p>
 *
 * @author mabc
 */
@RestController
@RequestMapping("/api/presentacion")
public class PresentacionController {

    /** Servicio de presentaciones inyectado por Spring. */
    @Autowired
    private PresentacionService presentacionService;

    /**
     * Obtiene una página de presentaciones, con filtros opcionales por párrafo y/o usuario.
     *
     * <p>El comportamiento varía según los parámetros recibidos:
     * <ul>
     *   <li>Si se proporcionan {@code parrafo} y {@code userId}, filtra por ambos.</li>
     *   <li>Si solo se proporciona {@code parrafo}, filtra únicamente por texto.</li>
     *   <li>Si no se proporciona ninguno, devuelve todas las presentaciones paginadas.</li>
     * </ul>
     * </p>
     *
     * @param parrafo texto de búsqueda dentro del párrafo (opcional).
     * @param userId  identificador del usuario propietario de la presentación (opcional).
     * @param page    número de página (basado en cero); se usa {@code 0} si es {@code null} o negativo.
     * @param size    cantidad de elementos por página; se usa {@code 10} si es {@code null} o negativo.
     * @return {@link ResponseEntity} con la página de {@link PresentacionDTO} encontrados,
     *         o {@code 400 Bad Request} si ocurre un error.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<PresentacionDTO>> getAll(
            @RequestParam(value = "parrafo", required = false) String parrafo,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        try {
            Page<PresentacionDTO> presentaciones;
            if (parrafo != null && userId != null) {
                presentaciones = presentacionService.getPresentaciones(userId, parrafo, page, size);
            } else if (parrafo != null) {
                presentaciones = presentacionService.getPresentaciones(parrafo, page, size);
            } else {
                presentaciones = presentacionService.getPresentaciones(page, size);
            }
            return ResponseEntity.ok(presentaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene la presentación asociada a un usuario específico.
     *
     * @param userId identificador del usuario cuya presentación se desea recuperar.
     * @return {@link ResponseEntity} con el {@link PresentacionDTO} encontrado,
     *         o {@code 400 Bad Request} si no existe o se produce un error.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PresentacionDTO> getByUserId(@PathVariable Long userId) {
        try {
            PresentacionDTO presentacion = presentacionService.getPresentacionByUserId(userId);
            return ResponseEntity.ok(presentacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea o actualiza una presentación.
     *
     * <p>Si el {@link PresentacionDTO} recibido contiene un {@code id} válido (mayor que cero),
     * se actualizará el registro existente; en caso contrario, se creará uno nuevo.</p>
     *
     * @param presentacion datos de la presentación a guardar.
     * @return {@link ResponseEntity} con el {@link PresentacionDTO} guardado (incluyendo el {@code id}
     *         generado), o {@code 400 Bad Request} si los datos son inválidos o se produce un error.
     */
    @PostMapping("/save")
    public ResponseEntity<PresentacionDTO> save(@RequestBody PresentacionDTO presentacion) {
        try {
            PresentacionDTO savedPresentacion = presentacionService.savePresentacion(presentacion);
            return ResponseEntity.ok(savedPresentacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina la presentación identificada por su ID.
     *
     * @param id identificador de la presentación a eliminar.
     * @return {@link ResponseEntity} con estado {@code 200 OK} si se eliminó correctamente,
     *         o {@code 400 Bad Request} si no se encontró la presentación o se produce un error.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            presentacionService.deletePresentacion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}