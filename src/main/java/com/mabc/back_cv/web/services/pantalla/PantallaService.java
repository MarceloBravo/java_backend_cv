package com.mabc.back_cv.web.services.pantalla;

import java.util.List;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.PantallaDTO;

/**
 * Interfaz de servicio para la gestión de pantallas de la aplicación.
 * Define las operaciones CRUD disponibles para pantallas.
 */
public interface PantallaService{

    /**
     * Obtiene la lista completa de todas las pantallas.
     *
     * @return Lista de PantallaDTO.
     */
    public List<PantallaDTO> getAllPantallas();

    /**
     * Obtiene una pantalla por su identificador.
     *
     * @param id Identificador de la pantalla.
     * @return PantallaDTO encontrado o null si no existe.
     */
    public PantallaDTO getPantallaById(Long id);

    /**
     * Crea o actualiza una pantalla.
     *
     * @param pantallaDTO DTO de la pantalla a guardar.
     * @return PantallaDTO guardado.
     */
    public PantallaDTO savePantalla(PantallaDTO pantallaDTO);

    /**
     * Elimina una pantalla por su identificador.
     *
     * @param id Identificador de la pantalla a eliminar.
     */
    public void deletePantalla(Long id);

    /**
     * Busca pantallas por término, estado y paginación.
     *
     * @param terminoBuscado Término de búsqueda opcional.
     * @param estado         Estado de la pantalla opcional.
     * @param page           Número de página opcional.
     * @param size           Tamaño de página opcional.
     * @param sortBy         Campo de ordenación opcional.
     * @return Página de PantallaDTO.
     */
    public Page<PantallaDTO> searchPantallas(String terminoBuscado, Boolean estado, Integer page, Integer size, String sortBy);
    
}
