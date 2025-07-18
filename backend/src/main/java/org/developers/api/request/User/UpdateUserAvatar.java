package org.developers.api.request.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserAvatar {
    private Long userId;
    
    @NotBlank(message = "La URL del avatar es requerida")
    private String avatarUrl;
} 