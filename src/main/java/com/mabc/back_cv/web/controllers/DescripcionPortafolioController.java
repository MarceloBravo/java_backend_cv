package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.DescripcionPortafolioDTO;
import com.mabc.back_cv.web.services.descripcionPortafolio.DescripcionPortafolioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descripcion-portafolio")
public class DescripcionPortafolioController {

    @Autowired
    private DescripcionPortafolioService descripcionPortafolioService;

    @GetMapping("/all")
    public ResponseEntity<List<DescripcionPortafolioDTO>> getAll() {
        try {
            List<DescripcionPortafolioDTO> list = descripcionPortafolioService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DescripcionPortafolioDTO>> getAll(
            @RequestParam(required = false) String terminoBuscado,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            Page<DescripcionPortafolioDTO> resultado = descripcionPortafolioService.getAll(terminoBuscado, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescripcionPortafolioDTO> getById(@PathVariable Long id) {
        try {
            DescripcionPortafolioDTO descripcionPortafolioDTO = descripcionPortafolioService.getById(id);
            if (descripcionPortafolioDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(descripcionPortafolioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<DescripcionPortafolioDTO> save(
            @RequestBody DescripcionPortafolioDTO descripcionPortafolioRequestDTO) {
        try {
            DescripcionPortafolioDTO descripcionPortafolioDTOGuardada = descripcionPortafolioService
                    .save(descripcionPortafolioRequestDTO);
            return ResponseEntity.ok(descripcionPortafolioDTOGuardada);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            descripcionPortafolioService.delete(id);
            return ResponseEntity.ok("Descripción de portafolio eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la descripción de portafolio");
        }
    }

}