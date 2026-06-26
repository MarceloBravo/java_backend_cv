package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de descripciones de portafolio.
 * Proporciona endpoints para listar, buscar, obtener, guardar y eliminar descripciones de portafolio.
 */
@RestController
@RequestMapping("/descripcion-portafolio")
public class DescripcionPortafolioController {

    @Autowired
    private DescripcionPortafolioService descripcionPortafolioService;

    /**
     * Obtiene la lista completa de descripciones de portafolio.
     *
     * @return ResponseEntity con la lista de DescripcionPortafolioDTO o error 400.
     */
    @GetMapping("/all")
    public ResponseEntity<List<DescripcionPortafolioDTO>> getAll() {
        try {
            List<DescripcionPortafolioDTO> list = descripcionPortafolioService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene una página de descripciones de portafolio filtradas por término de búsqueda.
     *
     * @param terminoBuscado Término de búsqueda opcional.
     * @param page           Número de página (opcional).
     * @param size           Tamaño de página (opcional).
     * @return ResponseEntity con la página de DescripcionPortafolioDTO o error 500.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<DescripcionPortafolioDTO>> getAll(
            @RequestParam(required = false) String terminoBuscado,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            Page<DescripcionPortafolioDTO> resultado = descripcionPortafolioService.getAll(terminoBuscado, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene una descripción de portafolio por su identificador.
     *
     * @param id Identificador único de la descripción.
     * @return ResponseEntity con el DescripcionPortafolioDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DescripcionPortafolioDTO> getById(@PathVariable Long id) {
        try {
            DescripcionPortafolioDTO descripcionPortafolioDTO = descripcionPortafolioService.getById(id);
            if (descripcionPortafolioDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(descripcionPortafolioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza una descripción de portafolio.
     *
     * @param descripcionPortafolioRequestDTO DTO con los datos a guardar.
     * @return ResponseEntity con el DescripcionPortafolioDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<DescripcionPortafolioDTO> save(
            @RequestBody DescripcionPortafolioDTO descripcionPortafolioRequestDTO) {
        try {
            DescripcionPortafolioDTO descripcionPortafolioDTOGuardada = descripcionPortafolioService
                    .save(descripcionPortafolioRequestDTO);
            return ResponseEntity.ok(descripcionPortafolioDTOGuardada);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina una descripción de portafolio por su identificador.
     *
     * @param id Identificador único de la descripción a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            descripcionPortafolioService.delete(id);
            return ResponseEntity.ok("Descripción de portafolio eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la descripción de portafolio");
        }
    }

}