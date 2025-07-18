package org.developers.api.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfile {
    private Long userId;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;
    
    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;
    
    @Size(max = 500, message = "La biografía no puede exceder 500 caracteres")
    private String bio;
    
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String location;
    
    @Size(max = 200, message = "El sitio web no puede exceder 200 caracteres")
    private String website;
    
    @Size(max = 100, message = "La ocupación no puede exceder 100 caracteres")
    private String occupation;
    
    @Size(max = 200, message = "Los intereses no pueden exceder 200 caracteres")
    private String interests;
    
    private String avatarUrl;
    
    private String bannerUrl;
    
    private String dateOfBirth;
    
    private String gender;
    
    private String phoneNumber;
    
    private String socialLinks;
} 