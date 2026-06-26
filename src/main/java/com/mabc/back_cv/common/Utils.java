package com.mabc.back_cv.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


/**
 * Clase utilitaria que proporciona métodos auxiliares para la creación de objetos Pageable
 * utilizados en la paginación de resultados de consultas.
 */
public class Utils{

    /**
     * Crea un objeto Pageable con los parámetros de paginación especificados.
     * Normaliza los valores nulos o negativos a valores por defecto.
     *
     * @param page Número de página (basado en cero); se usa 0 si es null o negativo.
     * @param size Cantidad de elementos por página; se usa 10 si es null o menor a 1.
     * @return Objeto Pageable configurado con la página y tamaño indicados.
     */
    public static Pageable createPageable(Integer page, Integer size){
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(page, size);
    }
    
    /**
     * Crea un objeto Pageable con paginación y ordenamiento por un campo específico.
     * Normaliza los valores nulos o negativos a valores por defecto.
     *
     * @param page    Número de página (basado en cero); se usa 0 si es null o negativo.
     * @param size    Cantidad de elementos por página; se usa 10 si es null o menor a 1.
     * @param orderBy Campo por el cual se ordenarán los resultados.
     * @return Objeto Pageable configurado con página, tamaño y orden especificados.
     */
    public static Pageable createPageable(Integer page, Integer size, String orderBy){
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(page, size, Sort.by(orderBy));
    }

}