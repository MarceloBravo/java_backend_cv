package com.mabc.back_cv.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPresentatioDTO{

    private Long id;

    @NotNull(message = "La posicion es obligatoria")
    private Integer posicion;

    @NotBlank(message = "El parrafo es obligatorio")
    @Size(max = 1000, message = "El parrafo no puede exceder 1000 caracteres")
    private String parrafo;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO usuario;
    
}