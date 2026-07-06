package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.services.curso.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de cursos.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar cursos.
 */
@RestController
@RequestMapping("/api/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * Obtiene una página de cursos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario (opcional).
     * @param searchText Texto de búsqueda (opcional).
     * @param page       Número de página (opcional).
     * @param size       Tamaño de página (opcional).
     * @return ResponseEntity con la página de CursoDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<CursoDTO>> getAll(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        try {
            Page<CursoDTO> resultado = cursoService.findBySearchText(userId, searchText, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene un curso por su identificador.
     *
     * @param id Identificador único del curso.
     * @return ResponseEntity con el CursoDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> getById(@PathVariable Long id) {
        try {
            CursoDTO cursoDTO = cursoService.findById(id);
            if (cursoDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cursoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene una página de cursos por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página (opcional).
     * @param size   Tamaño de página (opcional).
     * @return ResponseEntity con la página de CursoDTO o error 500.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CursoDTO>> getAllByUserId(
        @PathVariable(name = "userId") Long userId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        try {
            Page<CursoDTO> resultado = cursoService.findByUserId(userId, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza un curso.
     *
     * @param cursoRequestDTO DTO con los datos del curso a guardar.
     * @return ResponseEntity con el CursoDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<CursoDTO> save(@RequestBody CursoDTO cursoRequestDTO) {
        try {
            CursoDTO cursoDTOGuardado = cursoService.save(cursoRequestDTO);
            return ResponseEntity.ok(cursoDTOGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un curso por su identificador.
     *
     * @param id Identificador único del curso a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            cursoService.delete(id);
            return ResponseEntity.ok("Curso eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el curso");
        }
    }

}
