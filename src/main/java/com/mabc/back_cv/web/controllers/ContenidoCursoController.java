package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.services.contenidoCurso.ContenidoCursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para la gestión del contenido de cursos.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar contenido de cursos.
 */
@RestController
@RequestMapping("/contenido-curso")
public class ContenidoCursoController{

    @Autowired
    private ContenidoCursoService service;

    /**
     * Obtiene la lista de contenidos de curso filtrados por texto y estado activo.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param activo     Estado activo opcional para filtrar.
     * @return ResponseEntity con la lista de ContenidoCursoDTO o error 500.
     */
    @GetMapping("/list")
    public ResponseEntity<List<ContenidoCursoDTO>> getPage(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Boolean activo
    ){
        try{
            List<ContenidoCursoDTO> result = service.findAllList(searchText, activo);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Obtiene una página de contenidos de curso filtrados por texto, estado activo y paginación.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página (opcional).
     * @param size       Tamaño de página (opcional).
     * @param activo     Estado activo opcional.
     * @return ResponseEntity con la página de ContenidoCursoDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<ContenidoCursoDTO>> getPage(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) Boolean activo
    ){
        try{
            Page<ContenidoCursoDTO> result = service.findAllPage(searchText, page, size, activo);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Obtiene un contenido de curso por su identificador.
     *
     * @param id Identificador único del contenido.
     * @return ResponseEntity con el ContenidoCursoDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContenidoCursoDTO> getById(@PathVariable Long id){
        try{
            ContenidoCursoDTO result = service.getById(id);
            if(result == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza un contenido de curso.
     *
     * @param contenidoCursoDTO DTO con los datos del contenido a guardar.
     * @return ResponseEntity con el ContenidoCursoDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<ContenidoCursoDTO> save(@RequestBody @Valid ContenidoCursoDTO contenidoCursoDTO){
        try{
            ContenidoCursoDTO result = service.save(contenidoCursoDTO);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Elimina un contenido de curso por su identificador.
     *
     * @param id Identificador único del contenido a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            service.delete(id);
            return ResponseEntity.ok("Educacion eliminada correctamente");
        }catch(Exception e){
            String message = e.getMessage().contains("inexistente") ? e.getMessage() : "Error al eliminar la educacion";
            return ResponseEntity.badRequest().body(message);
        }
    }

}