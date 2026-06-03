package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.UserPresentation;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 300, message = "El email no puede exceder 300 caracteres")
    private String email;
    
    @Size(max = 20, message = "El fono no puede exceder 20 caracteres")
    private String fono;
    
    @Size(max = 255, message = "La direccion no puede exceder 255 caracteres")
    private String direccion;
    
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;
    
    @Size(max = 50, message = "El idioma no puede exceder 50 caracteres")
    private String idioma;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 100, message = "La contraseña no puede exceder 100 caracteres")
    private String password;
    
    private Boolean activo = true;
    
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    private List<UserPresentation> parrafos = new ArrayList<>();
}
