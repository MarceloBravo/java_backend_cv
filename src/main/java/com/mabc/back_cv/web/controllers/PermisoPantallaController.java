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


@RestController
@RequestMapping("/permisos-pantalla")
public class PermisoPantallaController {

    private final PermisoPantallaService permisoPantallaService;

    public PermisoPantallaController(PermisoPantallaService permisoPantallaService) {
        this.permisoPantallaService = permisoPantallaService;
    }


    @GetMapping("/rol/{id_rol}")
    public ResponseEntity<List<PermisoPantallaDTO>> obtenerPermisosPantallaPorRol(@PathVariable Long id_rol) {
        try {
            List<PermisoPantallaDTO> permisos = permisoPantallaService.obtenerPermisosPantallaPorRol(id_rol);
            return ResponseEntity.ok(permisos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
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

    @PostMapping("/grabar")
    public ResponseEntity<Integer> grabarPermisosPantallaPorRol(@RequestBody List<PermisoPantallaDTO> permisosPantallaDTO) {
        try {
            int resultado = permisoPantallaService.grabarPermisosPantallaPorRol(permisosPantallaDTO);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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