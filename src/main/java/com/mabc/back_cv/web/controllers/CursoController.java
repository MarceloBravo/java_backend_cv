package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.services.curso.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

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

    @PostMapping("/save")
    public ResponseEntity<CursoDTO> save(@RequestBody CursoDTO cursoRequestDTO) {
        try {
            CursoDTO cursoDTOGuardado = cursoService.save(cursoRequestDTO);
            return ResponseEntity.ok(cursoDTOGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

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
