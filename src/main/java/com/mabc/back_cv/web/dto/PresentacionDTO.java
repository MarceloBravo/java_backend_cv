package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.mabc.back_cv.web.entities.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresentacionDTO {

    private Long id;

    @NotBlank(message = "El párrafo de la presentación es obligatorio")
    @Size(max = 5000, message = "El párrafo de la presentación no puede exceder los 5000 caracteres")
    private String parrafo;

    @NotBlank(message = "El usuario es obligatorio")
    private User user;
}