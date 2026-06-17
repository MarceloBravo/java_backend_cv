package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoDTO{

    private Long id;

    @NotNull(message = "La posicion es obligatoria")
    private Integer posicion;

    @NotBlank(message = "La compañia es obligatoria")
    @Size(max = 255, message = "La compañia no puede exceder 255 caracteres")
    private String company;
    
    @NotBlank(message = "La posicion es obligatoria")
    private String position;
    
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede exceder 255 caracteres")
    private String description;
    
    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String startDate;

    private String endDate;
    
    private Boolean current = true;

    private List<TecnologiaDTO> tecnologias = new ArrayList<>();

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO user;
    
}