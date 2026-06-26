package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.services.certificado.CertificadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de certificados.
 * Proporciona endpoints para listar, paginar, obtener, guardar y eliminar certificados.
 */
@RestController
@RequestMapping("/certificado")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    /**
     * Obtiene una página de certificados filtrados por usuario y texto de búsqueda.
     *
     * @param userId     Identificador del usuario (opcional).
     * @param searchText Texto de búsqueda (opcional).
     * @param page       Número de página (opcional).
     * @param size       Tamaño de página (opcional).
     * @return ResponseEntity con la página de CertificadoDTO o error 500.
     */
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

    /**
     * Obtiene un certificado por su identificador.
     *
     * @param id Identificador único del certificado.
     * @return ResponseEntity con el CertificadoDTO encontrado, 404 si no existe o error 500.
     */
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

    /**
     * Obtiene una página de certificados por usuario.
     *
     * @param userId Identificador del usuario.
     * @param page   Número de página (opcional).
     * @param size   Tamaño de página (opcional).
     * @return ResponseEntity con la página de CertificadoDTO o error 500.
     */
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

    /**
     * Crea o actualiza un certificado.
     *
     * @param certificadoRequestDTO DTO con los datos del certificado a guardar.
     * @return ResponseEntity con el CertificadoDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<CertificadoDTO> save(@RequestBody CertificadoDTO certificadoRequestDTO) {
        try {
            CertificadoDTO certificadoDTOGuardado = certificadoService.save(certificadoRequestDTO);
            return ResponseEntity.ok(certificadoDTOGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un certificado por su identificador.
     *
     * @param id Identificador único del certificado a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
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
