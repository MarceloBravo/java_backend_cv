package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.mabc.back_cv.web.dto.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducacionDTO{

    private Long id;

    @NotNull(message = "La institucion es obligatoria")
    @Size(max = 100, message = "La institucion no puede exceder 100 caracteres")
    private String institution;

    @NotNull(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo no puede exceder 100 caracteres")
    private String title;

    @NotNull(message = "El titulo corto es obligatorio")
    @Size(max = 50, message = "El titulo corto no puede exceder 50 caracteres")
    private String shortTitle;

    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
    @NotNull(message = "La descripcion es obligatoria")
    private String description;

    @NotNull(message = "El año desde es obligatorio")
    @Min(value = 1980, message = "El año desde debe ser mayor a 0")
    @Max(value = 9999, message = "El año desde no puede ser mayor a 9999")
    private Integer yearFrom;

    @NotNull(message = "El año hasta es obligatorio")
    @Min(value = 1980, message = "El año hasta debe ser mayor a 0")
    @Max(value = 9999, message = "El año hasta no puede ser mayor a 9999")
    private Integer yearTo;

    @NotNull(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion debe ser mayor a 0")
    @Max(value = 9999, message = "La duracion no puede ser mayor a 9999")
    private Integer duration;

    @Size(max = 255, message = "La imagen no puede exceder 255 caracteres")
    private String image;

    @Size(max = 255, message = "La url no puede exceder 255 caracteres")
    private String url;
    
    @Size(max = 255, message = "Los estilos no pueden exceder 255 caracteres")
    private String styles;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO usuario;

}