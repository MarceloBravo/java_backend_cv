package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.services.certificado.CertificadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificado")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/all")
    public ResponseEntity<Page<CertificadoDTO>> getAll(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        try {
            Page<CertificadoDTO> resultado = certificadoService.findBySearchText(userId, searchText, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificadoDTO> getById(@PathVariable Long id) {
        try {
            CertificadoDTO certificadoDTO = certificadoService.findById(id);
            if (certificadoDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(certificadoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CertificadoDTO>> getAllByUserId(
        @PathVariable(name = "userId") Long userId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        try {
            Page<CertificadoDTO> resultado = certificadoService.findByUserId(userId, page, size);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<CertificadoDTO> save(@RequestBody CertificadoDTO certificadoRequestDTO) {
        try {
            CertificadoDTO certificadoDTOGuardado = certificadoService.save(certificadoRequestDTO);
            return ResponseEntity.ok(certificadoDTOGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            certificadoService.delete(id);
            return ResponseEntity.ok("Certificado eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el certificado");
        }
    }

}
