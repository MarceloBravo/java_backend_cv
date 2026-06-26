package com.mabc.back_cv.web.dto;

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
public class CertificadoDTO {

    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotNull(message = "La URL de la imagen es obligatoria")
    private String image;

    @NotNull(message = "La URL del certificado es obligatoria")
    private String url;

    @NotNull(message = "El texto de mouse es obligatorio")
    private String mouse_move_title;

    @NotNull(message = "La descripción de mouse es obligatoria")
    private String mouse_move_description;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO user;

}
