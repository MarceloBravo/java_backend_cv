package com.mabc.back_cv.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPresentationDTO{

    private Long id;

    @NotNull(message = "La posicion es obligatoria")
    private Integer posicion;

    @NotBlank(message = "El parrafo es obligatorio")
    @Size(max = 1000, message = "El parrafo no puede exceder 1000 caracteres")
    private String parrafo;

    @NotNull(message = "El usuario es obligatorio")
    private UserDTO user;
    
}