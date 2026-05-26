package com.mabc.back_cv.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Objeto de Transferencia de Datos) para la entidad Rol.
 * Utilizado para la validación de datos de entrada en las operaciones de
 * gestión de roles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El estado del rol es obligatorio")
    private Boolean activo;

}
