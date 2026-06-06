package com.mabc.back_cv.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.services.usuarios.UsuariosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para obtener, crear, actualizar y eliminar usuarios.
 * 
 * @author MaBC
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuariosService usuarioService;

    /**
     * Obtiene la lista de todos los usuarios con filtro opcional.
     * 
     * @param filter Parámetro opcional para filtrar usuarios por nombre o email
     * @return ResponseEntity con la lista de UsuarioDTO o error en caso de
     *         excepción
     */
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioDTO>> getAll(
            @RequestParam(value = "filter", required = false) String filter) {
        try {
            List<UsuarioDTO> list = usuarioService.getAllUsuarios(filter);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param id Identificador único del usuario
     * @return ResponseEntity con el UsuarioDTO o error en caso de excepción
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        try {
            UsuarioDTO usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene una página de usuarios con filtro, número de página y tamaño.
     * 
     * @param filter Parámetro opcional para filtrar usuarios
     * @param page   Número de página (por defecto 0)
     * @param size   Cantidad de registros por página (por defecto 10)
     * @return ResponseEntity con una Page de UsuarioDTO o error en caso de
     *         excepción
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UsuarioDTO>> getAllPage(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Long page,
            @RequestParam(required = false) Long size) {
        try {
            Page<UsuarioDTO> pageUsuario = usuarioService.getAllUsuariosPage(filter, page, size);
            return ResponseEntity.ok(pageUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea o actualiza un usuario.
     * 
     * @param usuarioDTO Datos del usuario a guardar
     * @return ResponseEntity con el UsuarioDTO guardado o error en caso de
     *         excepción
     */
    @PostMapping("/save")
    public ResponseEntity<UsuarioDTO> saveUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioSaved = usuarioService.saveUsuario(usuarioDTO);
            return ResponseEntity.ok(usuarioSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id Identificador único del usuario a eliminar
     * @return ResponseEntity con mensaje de confirmación o error en caso de
     *         excepción
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
