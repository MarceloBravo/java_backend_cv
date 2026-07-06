package com.mabc.back_cv.web.controllers;

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


import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controlador REST para la gestión de presentaciones de usuario (UserPresentation).
 * Proporciona endpoints para listar, obtener, guardar y eliminar presentaciones de usuario.
 */
@RestController
@RequestMapping("/api/userPresentation")
public class UserPresentationController{

    @Autowired
    private UserPresentationService userPresentationService;

    /**
     * Obtiene una página de presentaciones de usuario con filtros opcionales.
     *
     * @param searchText Texto de búsqueda opcional para filtrar resultados.
     * @param userId     Identificador del usuario para filtrar (opcional).
     * @param page       Número de página (por defecto 0).
     * @param size       Tamaño de página (por defecto 10).
     * @return ResponseEntity con la página de UserPresentationDTO o error 500.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<UserPresentationDTO>> getAll(
        @RequestParam(value = "searchText", defaultValue="") String searchText,
        @RequestParam(value = "userId", defaultValue="") Long userId,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "size", defaultValue="10") Integer size
    ){
        try{
            Page<UserPresentationDTO> resultado = userPresentationService.getAll(searchText, userId, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene una presentación de usuario por su identificador.
     *
     * @param id Identificador único de la presentación.
     * @return ResponseEntity con el UserPresentationDTO encontrado, 404 si no existe o error 500.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPresentationDTO> getById(@PathVariable Long id){
        try{
            UserPresentationDTO userPresentationDTO = userPresentationService.findById(id);
            if(userPresentationDTO == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userPresentationDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Crea o actualiza una presentación de usuario.
     *
     * @param userPresentationRequestDTO DTO con los datos de la presentación a guardar.
     * @return ResponseEntity con el UserPresentationDTO guardado o error 500.
     */
    @PostMapping("/save")
    public ResponseEntity<UserPresentationDTO> save(@Valid @RequestBody UserPresentationDTO userPresentationRequestDTO){
        try{
            UserPresentationDTO response = userPresentationService.save(userPresentationRequestDTO);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina una presentación de usuario por su identificador.
     *
     * @param id Identificador único de la presentación a eliminar.
     * @return ResponseEntity con mensaje de confirmación o error 400.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            userPresentationService.delete(id);
            return ResponseEntity.ok("UserPresentation eliminada correctamente");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar la UserPresentation");
        }
    }

}