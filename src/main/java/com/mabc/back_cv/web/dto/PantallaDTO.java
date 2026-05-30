package com.mabc.back_cv.web.dto;

import com.mabc.back_cv.web.entities.Menu;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PantallaDTO{
    private Long id;

    @NotBlank(message = "El nombre de la pantalla es obligatorio")
    @Size(max = 100, message = "El nombre de la pantalla no puede exceder 100 caracteres")
    private String nombre;
    /*
    @NotBlank(message = "La URL del archivo de la pantalla es obligatoria")
    @Size(max = 255, message = "La URL del archivo de la pantalla no puede exceder 255 caracteres")
    private String url_archivo;
    */
    private Menu menu;

    @NotBlank(message = "El estado de la pantalla es obligatorio")
    private Boolean activo = true;

    @NotBlank(message = "El estado de la acción de crear es obligatorio")
    private Boolean accion_crear = false;

    @NotBlank(message = "El estado de la acción de editar es obligatorio")
    private Boolean accion_editar = false;

    @NotBlank(message = "El estado de la acción de eliminar es obligatorio")
    private Boolean accion_eliminar = false;

    @NotBlank(message = "El estado de la acción de consultar es obligatorio")
    private Boolean accion_consultar = false;

    @NotBlank(message = "El estado de la acción de listar es obligatorio")
    private Boolean listar = false;

}