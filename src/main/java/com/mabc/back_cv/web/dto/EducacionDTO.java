package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.mabc.back_cv.web.entities.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsContructor
public class EducacionDTO{

    @NoBlank(message = "El id es obligatorio")
    @Min(value = 1, message = "El id debe ser mayor a 0")
    private Long id;

    @NotBlank(message = "La institucion es obligatoria")
    @Size(max = 100, message = "La institucion no puede exceder 100 caracteres")
    private String institution;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo no puede exceder 100 caracteres")
    private String title;

    @NotBlank(message = "El titulo corto es obligatorio")
    @Size(max = 50, message = "El titulo corto no puede exceder 50 caracteres")
    private String shortTitle;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
    @NotBlank(message = "La descripcion es obligatoria")
    private String description;

    @NotBlank(message = "El año desde es obligatorio")
    @Min(value = 1980, message = "El año desde debe ser mayor a 0")
    @Max(value = 9999, message = "El año desde no puede ser mayor a 9999")
    private Integer yearFrom;

    @NotBlank(message = "El año hasta es obligatorio")
    @Min(value = 1980, message = "El año hasta debe ser mayor a 0")
    @Max(value = 9999, message = "El año hasta no puede ser mayor a 9999")
    private Integer yearTo;

    @NotBlank(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion debe ser mayor a 0")
    @Max(value = 9999, message = "La duracion no puede ser mayor a 9999"))
    private Integer duration;

    @Size(max = 255, message = "La imagen no puede exceder 255 caracteres")
    private String image;

    @Size(max = 255, message = "La url no puede exceder 255 caracteres")
    private String url;
    
    @Size(max = 255, message = "Los estilos no pueden exceder 255 caracteres")
    private String styles;

    @NotBlank(message = "El usuario es obligatorio")
    private User usuario;

}