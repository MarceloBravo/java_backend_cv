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

    /**
     * Obtiene la lista completa de todos los roles registrados.
     *
     * @return ResponseEntity con la lista de RolDTO o error 400.
     */
    @GetMapping("/all")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene un rol por su identificador.
     *
     * @param id Identificador único del rol.
     * @return ResponseEntity con el RolDTO encontrado o error 400.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRolById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene una página de roles filtrados por nombre y estado activo.
     *
     * @param nombre Nombre del rol para filtrar (opcional).
     * @param activo Estado activo del rol (por defecto true).
     * @param page   Número de página (por defecto 0).
     * @param rows   Cantidad de registros por página (por defecto 10).
     * @return ResponseEntity con la página de RolDTO o error 400.
     */
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

    /**
     * Obtiene la lista de roles activos.
     *
     * @return ResponseEntity con la lista de RolDTO activos o error 400.
     */
    @GetMapping("/active")
    public ResponseEntity<List<RolDTO>> getActiveRoles() {
        try {
            return ResponseEntity.ok(service.getActiveRoles());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea o actualiza un rol.
     *
     * @param rolDTO DTO con los datos del rol a guardar.
     * @return ResponseEntity con el RolDTO guardado o error 400.
     */
    @PostMapping("/save")
    public ResponseEntity<RolDTO> saveRol(@RequestBody RolDTO rolDTO) {
        try {
            return ResponseEntity.ok(service.save(rolDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un rol por su identificador.
     *
     * @param id Identificador único del rol a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
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