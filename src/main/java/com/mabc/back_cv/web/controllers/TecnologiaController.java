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

import com.mabc.back_cv.web.dto.TecnologiaDTO;
import com.mabc.back_cv.web.services.tecnologia.TecnologiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para la gestión de tecnologías.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar tecnologías.
 */
@RestController
@RequestMapping("/api/tecnologias")
public class TecnologiaController {

    @Autowired
    private TecnologiaService service;

    /**
     * Obtiene una página de tecnologías filtradas por texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @param page       Número de página (por defecto 0).
     * @param size       Tamaño de página (por defecto 10).
     * @return ResponseEntity con la página de TecnologiaDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<TecnologiaDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "size", defaultValue="10") Integer size
    ){
        try{
            Page<TecnologiaDTO> resultado = service.findAll(searchText, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene la lista completa de tecnologías filtradas por texto de búsqueda.
     *
     * @param searchText Texto de búsqueda opcional.
     * @return ResponseEntity con la lista de TecnologiaDTO o error 500.
     */
    @GetMapping("/list")
    public ResponseEntity<List<TecnologiaDTO>> getList(
        @RequestParam(value = "searchText", defaultValue="") String searchText
    ){
        try{
            List<TecnologiaDTO> resultado = service.findAll(searchText);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene una tecnología por su identificador.
     *
     * @param id Identificador único de la tecnología.
     * @return ResponseEntity con el TecnologiaDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TecnologiaDTO> getById(@PathVariable Long id){
        try{
            TecnologiaDTO tecnologiaDTO = service.getById(id);
            if(tecnologiaDTO == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(tecnologiaDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza una tecnología.
     *
     * @param tecnologiaRequestDTO DTO con los datos de la tecnología a guardar.
     * @return ResponseEntity con el TecnologiaDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<TecnologiaDTO> save(@Valid @RequestBody TecnologiaDTO tecnologiaRequestDTO){
        try{    
            TecnologiaDTO tecnologiaDTO = service.save(tecnologiaRequestDTO);
            return ResponseEntity.ok(tecnologiaDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina una tecnología por su identificador.
     *
     * @param id Identificador único de la tecnología a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            service.deleteById(id);
            return ResponseEntity.ok("Tecnologia eliminada correctamente");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar la tecnologia");
        }
    }
    
}