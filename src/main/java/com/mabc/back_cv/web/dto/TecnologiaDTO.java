package com.mabc.back_cv.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TecnologiaDTO{
    private Long id;

    @NotBlank(message = "El nombre de la tecnología es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de tecnología es obligatorio")
    private TipoTecnologiaEnum type;

    @Size(max = 500, message = "La ruta de la imagen no puede exceder 500 caracteres")
    private String pathImage;

    private String logoSvg;

}