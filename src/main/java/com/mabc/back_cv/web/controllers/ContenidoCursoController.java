package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.services.contenidoCurso.ContenidoCursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/contenido-curso")
public class ContenidoCursoController{

    @Autowired
    private ContenidoCursoService service;

    @GetMapping("/all")
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
    
    @GetMapping("/page")
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

    @PostMapping("/save")
    public ResponseEntity<ContenidoCursoDTO> save(@RequestBody @Valid ContenidoCursoDTO contenidoCursoDTO){
        try{
            ContenidoCursoDTO result = service.save(contenidoCursoDTO);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
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