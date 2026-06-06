package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.services.portafolio.PortafolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

/**
 * Controlador REST para administrar los portafolios de los usuarios.
 * Proporciona endpoints para obtener, buscar, guardar y eliminar portafolios.
 */
@RestController
@RequestMapping("/portafolio")
public class PortafolioController {

    @Autowired
    private PortafolioService portafolioService;

    /**
     * Obtiene un portafolio por su ID.
     * 
     * @param id ID del portafolio a obtener.
     * @return ResponseEntity con el PortafolioDTO o estado 404 si no se
     */
    @GetMapping("/{id}")
    public ResponseEntity<PortafolioDTO> getPortafolioById(@PathVariable Long id) {
        try {
            PortafolioDTO portafolio = portafolioService.getPortafolioById(id);
            if (portafolio != null) {
                return ResponseEntity.ok(portafolio);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el portafolio asociado a un usuario por su ID.
     * 
     * @param userId ID del usuario cuyo portafolio se desea obtener.
     * @return ResponseEntity con el PortafolioDTO o estado 404 si no se encuentra
     *         un portafolio para ese usuario.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PortafolioDTO> getPortafolioByUserId(@PathVariable Long userId) {
        try {
            PortafolioDTO portafolio = portafolioService.getPortafolioByUserId(userId);
            if (portafolio != null) {
                return ResponseEntity.ok(portafolio);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca portafolios según criterios de filtrado y paginación.
     * 
     * @param userId     ID del usuario para filtrar portafolios (opcional).
     * @param searchText texto de búsqueda para filtrar portafolios por título,
     *                   párrafo, textoMouseMove, descripciónMouseMove o link
     *                   (opcional).
     * @param page       número de página para paginación (opcional, por defecto 0).
     * @param size       tamaño de la página para paginación (opcional, por defecto
     *                   10).
     * @return ResponseEntity con la página de resultados o estado 500 en caso de
     *         error.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PortafolioDTO>> searchPortafolios(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            return ResponseEntity.ok(portafolioService.getPage(userId, searchText, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Guarda un portafolio. Si el DTO contiene un ID, se actualizará el portafolio
     * existente; de lo contrario, se creará uno nuevo.
     * 
     * @param portafolioDTO DTO del portafolio a guardar.
     * @return ResponseEntity con el PortafolioDTO guardado
     */
    @PostMapping("/save")
    public ResponseEntity<PortafolioDTO> savePortafolio(@RequestBody @Valid PortafolioDTO portafolioDTO) {
        try {
            PortafolioDTO savedPortafolio = portafolioService.savePortafolio(portafolioDTO);
            return ResponseEntity.ok(savedPortafolio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un portafolio por su ID.
     * 
     * @param id ID del portafolio a eliminar.
     * @return ResponseEntity con mensaje de confirmación o estado 404 si no se
     *         encuentra el portafolio.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePortafolio(@PathVariable Long id) {
        try {
            portafolioService.deletePortafolio(id);
            return ResponseEntity.ok("Portafolio eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}