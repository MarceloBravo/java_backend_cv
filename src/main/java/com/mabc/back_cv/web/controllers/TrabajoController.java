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

import com.mabc.back_cv.web.dto.TrabajoDTO;
import com.mabc.back_cv.web.services.trabajo.TrabajoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para la gestión de trabajos o experiencias laborales.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar trabajos.
 */
@RestController
@RequestMapping("/trabajos")
public class TrabajoController{

    @Autowired
    private TrabajoService trabajoService;

    /**
     * Obtiene la lista de trabajos filtrados por usuario y texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param userId     Identificador del usuario (obligatorio).
     * @return ResponseEntity con la lista de TrabajoDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<List<TrabajoDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "userId", defaultValue="") Long userId
    ){
        try{
            if(userId == null){
                return ResponseEntity.badRequest().build();
            }
            List<TrabajoDTO> resultado = trabajoService.getAll(userId, searchText);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Obtiene una página de trabajos filtrados por usuario y texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param userId     Identificador del usuario (obligatorio).
     * @param page       Número de página (por defecto 0).
     * @param size       Tamaño de página (por defecto 10).
     * @return ResponseEntity con la página de TrabajoDTO o error 500.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<TrabajoDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "userId", defaultValue="") Long userId,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "size", defaultValue="10") Integer size
    ){
        try{
            if(userId == null){
                return ResponseEntity.badRequest().build();
            }
            Page<TrabajoDTO> resultado = trabajoService.getAll(userId, searchText, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene un trabajo por su identificador.
     *
     * @param id Identificador único del trabajo.
     * @return ResponseEntity con el TrabajoDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrabajoDTO> getById(@PathVariable Long id){
        try{
            TrabajoDTO trabajoDTO = trabajoService.getById(id);
            if(trabajoDTO == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(trabajoDTO);   
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza un trabajo.
     *
     * @param trabajoRequestDTO DTO con los datos del trabajo a guardar.
     * @return ResponseEntity con el TrabajoDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<TrabajoDTO> save(@RequestBody @Valid TrabajoDTO trabajoRequestDTO){
        try{
            TrabajoDTO trabajoDTO = trabajoService.save(trabajoRequestDTO);
            return ResponseEntity.ok(trabajoDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un trabajo por su identificador.
     *
     * @param id Identificador único del trabajo a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{    
            trabajoService.deleteById(id);
            return ResponseEntity.ok("Trabajo eliminado correctamente");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar el trabajo");
        }
    }

}