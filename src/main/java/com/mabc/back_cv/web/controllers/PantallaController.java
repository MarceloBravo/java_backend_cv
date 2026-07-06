package com.mabc.back_cv.web.controllers;

import com.mabc.back_cv.web.dto.PantallaDTO;
import com.mabc.back_cv.web.services.pantalla.PantallaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para administrar las pantallas de la aplicación.
 * Proporciona endpoints para listar, buscar, obtener, guardar y eliminar pantallas.
 */
@RestController
@RequestMapping("/api/pantallas")
public class PantallaController {

    private final PantallaService pantallaService;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param pantallaService Servicio de pantallas.
     */
    public PantallaController(PantallaService pantallaService) {
        this.pantallaService = pantallaService;
    }

    /**
     * Obtiene la lista completa de pantallas.
     *
     * @return ResponseEntity con la lista de PantallaDTO o estado 500 en caso de error.
     */
    @GetMapping("/list")
    public ResponseEntity<List<PantallaDTO>> getAllPantallas() {
        try{
            return ResponseEntity.ok(pantallaService.getAllPantallas());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca pantallas según criterios de filtrado y paginación.
     *
     * @param terminoBuscado término de búsqueda opcional.
     * @param estado estado de la pantalla opcional.
     * @param page número de página opcional.
     * @param size tamaño de página opcional.
     * @param sortBy campo por el cual ordenar opcional.
     * @return ResponseEntity con la página de resultados o estado 500 en caso de error.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PantallaDTO>> searchPantallas(
            @RequestParam(required = false) String terminoBuscado,
            @RequestParam(required = false) Boolean estado,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy
    ) {
        try{
            Page<PantallaDTO> pantallaDTOPage = pantallaService.searchPantallas(terminoBuscado, estado, page, size, sortBy);
            return ResponseEntity.ok(pantallaDTOPage);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene una pantalla por su identificador.
     *
     * @param id identificador de la pantalla.
     * @return ResponseEntity con el PantallaDTO encontrado, 404 si no existe o 500 en caso de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PantallaDTO> getPantallaById(@PathVariable Long id) {
        try{
            PantallaDTO pantallaDTO = pantallaService.getPantallaById(id);
            if (pantallaDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pantallaDTO);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Guarda una nueva pantalla o actualiza una existente.
     *
     * @param pantallaDTO datos de la pantalla a guardar.
     * @return ResponseEntity con el PantallaDTO guardado o estado 500 en caso de error.
     */
    @PostMapping("/save")
    public ResponseEntity<PantallaDTO> savePantalla(@RequestBody PantallaDTO pantallaDTO) {
        try{
            PantallaDTO pantallaDTOGuardada = pantallaService.savePantalla(pantallaDTO);
            return ResponseEntity.ok(pantallaDTOGuardada);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Elimina una pantalla existente por su identificador.
     *
     * @param id identificador de la pantalla a eliminar.
     * @return ResponseEntity con mensaje de confirmación o estado 500 en caso de error.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePantalla(@PathVariable Long id) {
        try{
            pantallaService.deletePantalla(id);
            return ResponseEntity.ok("Pantalla eliminada correctamente");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la pantalla");
        }
    }
}