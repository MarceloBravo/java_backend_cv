package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.services.educacion.EducacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/educacion")
public class EducacionController{

    @Autowired
    private EducacionService educacionService;

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


    @PostMapping("/save")
    public ResponseEntity<EducacionDTO> save(@RequestBody EducacionDTO educacionRequestDTO){
        try{
            EducacionDTO educacionDTOGuardada = educacionService.save(educacionRequestDTO);
            return ResponseEntity.ok(educacionDTOGuardada);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

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