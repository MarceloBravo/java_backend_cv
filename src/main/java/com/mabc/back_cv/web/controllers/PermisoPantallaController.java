package com.mabc.back_cv.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.services.permisoPantalla.PermisoPantallaService;


/**
 * Controlador REST para la gestión de permisos de pantalla.
 * 
 * Proporciona endpoints para obtener, crear, eliminar y desactivar permisos
 * de pantalla asociados a roles y pantallas en el sistema.
 */
@RestController
@RequestMapping("/api/permisos-pantalla")
public class PermisoPantallaController {

    private final PermisoPantallaService permisoPantallaService;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param permisoPantallaService Servicio de permisos de pantalla.
     */
    public PermisoPantallaController(PermisoPantallaService permisoPantallaService) {
        this.permisoPantallaService = permisoPantallaService;
    }


    /**
     * Obtiene la lista de permisos de pantalla asociados a un rol específico.
     *
     * @param id_rol Identificador del rol.
     * @return ResponseEntity con la lista de permisos de pantalla (200 OK) o error interno (500).
     */
    @GetMapping("/rol/{id_rol}")
    public ResponseEntity<List<PermisoPantallaDTO>> obtenerPermisosPantallaPorRol(@PathVariable Long id_rol) {
        try {
            List<PermisoPantallaDTO> permisos = permisoPantallaService.obtenerPermisosPantallaPorRol(id_rol);
            return ResponseEntity.ok(permisos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene un permiso de pantalla específico asociado a un rol y una pantalla.
     *
     * @param id_rol Identificador del rol.
     * @param id_pantalla Identificador de la pantalla.
     * @return ResponseEntity con el permiso encontrado (200 OK), not found (404) si no existe, o error interno (500).
     */
    @GetMapping("/rol/{id_rol}/pantalla/{id_pantalla}")
    public ResponseEntity<PermisoPantallaDTO> obtenerPermisoPantallaPorRolYPantalla(@PathVariable Long id_rol, @PathVariable Long id_pantalla) {
        try {
            PermisoPantallaDTO permiso = permisoPantallaService.obtenerPermisoPantallaPorRolYPantalla(id_rol, id_pantalla, true);
            if (permiso != null) {
                return ResponseEntity.ok(permiso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Almacena una lista de permisos de pantalla para un rol.
     *
     * @param permisosPantallaDTO Lista de DTOs de permisos de pantalla a guardar.
     * @return ResponseEntity con la cantidad de permisos procesados (200 OK) o error interno (500).
     */
    @PostMapping("/grabar")
    public ResponseEntity<Integer> grabarPermisosPantallaPorRol(@RequestBody List<PermisoPantallaDTO> permisosPantallaDTO) {
        try {
            int resultado = permisoPantallaService.grabarPermisosPantallaPorRol(permisosPantallaDTO);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina todos los permisos de pantalla asociados a un rol específico.
     *
     * @param id_rol Identificador del rol.
     * @return ResponseEntity con mensaje de éxito (200 OK), not found (404) si no hay permisos, o error interno (500).
     */
    @DeleteMapping("/eliminar/rol/{id_rol}")
    public ResponseEntity<String> eliminarPermisosPantallaPorRolId(@PathVariable Long id_rol) {
        try {
            boolean eliminado = permisoPantallaService.eliminarPermisosPantallaPorRolId(id_rol);
            if (eliminado) {
                return ResponseEntity.ok("PermisosPantalla eliminados correctamente por rol");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }

    /**
     * Desactiva todos los permisos de pantalla asociados a un rol específico.
     *
     * @param id_rol Identificador del rol.
     * @return ResponseEntity con mensaje de éxito (200 OK), not found (404) si no hay permisos, o error interno (500).
     */
    @PutMapping("/desactivar/rol/{id_rol}")
    public ResponseEntity<String> desactivarPermisosPantallaPorRolId(@PathVariable Long id_rol) {
        try {
            boolean desactivado = permisoPantallaService.desactivarPermisosPantallaPorRolId(id_rol);
            if (desactivado) {
                return ResponseEntity.ok("PermisosPantalla desactivados correctamente por rol");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }


}