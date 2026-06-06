package com.mabc.back_cv.web.dto;

import jakarta.validation.constraints.Size;
import com.mabc.back_cv.web.entities.User;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortafolioDTO {

    private Long id;

    @NotNull(message = "El título es obligatorio")
    private String title;

    @Size(max = 500, message = "La imagen no puede exceder los 500 caracteres")
    private String image;

    @Size(max = 500, message = "El video no puede exceder los 500 caracteres")
    private String video;

    @Size(max = 255, message = "El título del mouse move no puede exceder los 255 caracteres")
    private String mouseMoveTitle;

    @Size(max = 255, message = "La descripción del mouse move no puede exceder los 500 caracteres")
    private String mouseMoveDescription;

    @NotNull(message = "El párrafo es obligatorio")
    @Size(max = 500, message = "El párrafo no puede exceder los 500 caracteres")
    private String paragraph;

    @Size(max = 255, message = "El enlace no puede exceder los 255 caracteres")
    private String link;

    @NotNull(message = "El usuario es obligatorio")
    private User user;

}
