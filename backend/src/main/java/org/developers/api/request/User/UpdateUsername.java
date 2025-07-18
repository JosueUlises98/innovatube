package org.developers.api.request.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUsername {
    private Long userId;
    
    @NotBlank(message = "El username es requerido")
    @Size(min = 3, max = 30, message = "El username debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El username solo puede contener letras, números, puntos, guiones bajos y guiones")
    private String username;
    
    @NotBlank(message = "La contraseña es requerida para confirmar el cambio")
    private String password;
} 