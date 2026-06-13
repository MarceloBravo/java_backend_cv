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

@RestController
@RequestMapping("/tecnologias")
public class TecnologiaController {

    @Autowired
    private TecnologiaService service;

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

    @PostMapping("/save")
    public ResponseEntity<TecnologiaDTO> save(@Valid @RequestBody TecnologiaDTO tecnologiaRequestDTO){
        try{    
            TecnologiaDTO tecnologiaDTO = service.save(tecnologiaRequestDTO);
            return ResponseEntity.ok(tecnologiaDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

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