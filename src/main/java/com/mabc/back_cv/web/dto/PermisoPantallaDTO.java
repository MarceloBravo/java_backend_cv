package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;

import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermisoPantallaDTO {

    private Long id;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    @NotNull(message = "La pantalla es obligatoria")
    private Pantalla pantalla;
    
    private Boolean accion_consultar;
    private Boolean accion_crear;
    private Boolean accion_editar;
    private Boolean accion_eliminar;
    private Boolean activo;

}