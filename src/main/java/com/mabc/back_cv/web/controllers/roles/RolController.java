package com.mabc.back_cv.web.controllers.roles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con los roles.
 * Mapea las solicitudes que inician con /api/roles.
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {
    
    /**
     * Muestra un mensaje de bienvenida al dashboard de roles.
     *
     * @return Una respuesta HTTP con el mensaje de bienvenida.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Bienvenido al dashboard de roles");
    }
}