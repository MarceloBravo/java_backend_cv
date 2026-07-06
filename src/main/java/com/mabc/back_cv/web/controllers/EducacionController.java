package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.services.educacion.EducacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de educación.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar registros educativos.
 */
@RestController
@RequestMapping("/api/educacion")
public class EducacionController{

    @Autowired
    private EducacionService educacionService;

    /**
     * Obtiene una página de registros educativos filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario (opcional).
     * @param searchText Texto de búsqueda (opcional).
     * @param page       Número de página (opcional).
     * @param size       Tamaño de página (opcional).
     * @return ResponseEntity con la página de EducacionDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<EducacionDTO>> getAll(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        try{
            Page<EducacionDTO> resultado = educacionService.findBySearchText(userId, searchText, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene un registro educativo por su identificador.
     *
     * @param id Identificador único del registro.
     * @return ResponseEntity con el EducacionDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EducacionDTO> getById(@PathVariable Long id){
        try{
            EducacionDTO educacionDTO = educacionService.findById(id);
            if(educacionDTO == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(educacionDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene una página de registros educativos por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página (opcional).
     * @param size   Tamaño de página (opcional).
     * @return ResponseEntity con la página de EducacionDTO o error 500.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<EducacionDTO>> getAllByUserId(
        @PathVariable(name = "userId") Long userId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        try{
            Page<EducacionDTO> resultado = educacionService.findByUserId(userId, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }


    /**
     * Crea o actualiza un registro educativo.
     *
     * @param educacionRequestDTO DTO con los datos educativos a guardar.
     * @return ResponseEntity con el EducacionDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<EducacionDTO> save(@RequestBody EducacionDTO educacionRequestDTO){
        try{
            EducacionDTO educacionDTOGuardada = educacionService.save(educacionRequestDTO);
            return ResponseEntity.ok(educacionDTOGuardada);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un registro educativo por su identificador.
     *
     * @param id Identificador único del registro a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            educacionService.delete(id);
            return ResponseEntity.ok("Educacion eliminada correctamente");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar la educacion");
        }
    }
}