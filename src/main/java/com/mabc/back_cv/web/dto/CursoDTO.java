package com.mabc.back_cv.web.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {

    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Size(max = 255, message = "El titulo no puede exceder 255 caracteres")
    private String title;

    @NotNull(message = "El instituto es obligatorio")
    private String institute;

    private CertificadoDTO certificate;

    private List<ContenidoCursoDTO> contenidos;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private Date startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private Date endDate;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO usuario;

    private Boolean activo = true;

}
