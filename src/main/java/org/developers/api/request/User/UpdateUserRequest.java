package org.developers.api.request.User;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateUserRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be a positive number")
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @NotNull(message = "isActive status cannot be null")
    private Boolean isActive;
}
