package com.mabc.back_cv.web.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.services.Rol.RolService;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con los
 * roles.
 * Mapea las solicitudes que inician con /api/roles.
 */
@RestController
@RequestMapping("/roles")
public class RolController {

    private RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRolById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Page<RolDTO>> getRolesByPage(
            @RequestParam(value = "nombre", defaultValue = "") String nombre,
            @RequestParam(value = "active", defaultValue = "true") Boolean activo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "rows", defaultValue = "10") int rows) {
        try {
            return ResponseEntity.ok(service.searchBy(nombre, activo, page, rows));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<RolDTO>> getActiveRoles() {
        try {
            return ResponseEntity.ok(service.getActiveRoles());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<RolDTO> saveRol(@RequestBody RolDTO rolDTO) {
        try {
            return ResponseEntity.ok(service.save(rolDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Rol eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: El rol no pudo ser eliminado: " + e.getMessage());
        }
    }

}