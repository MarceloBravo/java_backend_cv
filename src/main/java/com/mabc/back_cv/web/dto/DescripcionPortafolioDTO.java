package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.mabc.back_cv.web.entities.Portafolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DescripcionPortafolioDTO {

    private Long id;

    @NotBlank(message = "La descripción del párrafo es obligatoria")
    @Size(max = 1000, message = "La descripción del párrafo no puede exceder 1000 caracteres")
    private String parrafo;

    @NotBlank(message = "La posición del párrafo es obligatoria")
    private Integer posicion;

    @NotBlank(message = "El portafolio es obligatorio")
    private Portafolio portafolio;

}
