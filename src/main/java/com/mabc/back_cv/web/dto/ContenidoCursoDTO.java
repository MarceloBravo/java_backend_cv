package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoCursoDTO{

    private Long id;

    @NotNull(message = "El titulo es obligatorio")    
    @Size(max = 100, message = "El título no puede exceder 255 caracteres")
    private String title;
    
    @NotNull(message = "La descripción es obligatoria")    
    @Size(max = 100, message = "La descripción no puede exceder 255 caracteres")
    private String description;
    
    private Boolean activo = true;
}