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

import java.util.List;

@RestController
@RequestMapping("/trabajos")
public class TrabajoController{

    @Autowired
    private TrabajoService trabajoService;

    @GetMapping("/all")
    public ResponseEntity<List<TrabajoDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "userId", defaultValue="") Long userId,
    ){
        try{
            Page<TrabajoDTO> resultado = trabajoService.getAll(userId, searchText);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/page")
    public ResponseEntity<Page<TrabajoDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "userId", defaultValue="") Long userId,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "size", defaultValue="10") Integer size
    ){
        try{
            Page<TrabajoDTO> resultado = trabajoService.getAll(userId, searchText, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

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

    @PostMapping("/save")
    public ResponseEntity<TrabajoDTO> save(@RequestBody TrabajoDTO trabajoRequestDTO){
        try{
            TrabajoDTO trabajoDTO = trabajoService.save(trabajoRequestDTO);
            return ResponseEntity.ok(trabajoDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

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