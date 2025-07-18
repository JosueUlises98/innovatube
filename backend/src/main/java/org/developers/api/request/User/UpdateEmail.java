package org.developers.api.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmail {
    private Long userId;
    
    @NotBlank(message = "El email actual es requerido")
    @Email(message = "El formato del email no es válido")
    private String currentEmail;
    
    @NotBlank(message = "El nuevo email es requerido")
    @Email(message = "El formato del nuevo email no es válido")
    private String newEmail;
    
    @NotBlank(message = "La contraseña es requerida para confirmar el cambio")
    private String password;
} 