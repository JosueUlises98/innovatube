package org.developers.api.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IsEmailAvaibleRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
