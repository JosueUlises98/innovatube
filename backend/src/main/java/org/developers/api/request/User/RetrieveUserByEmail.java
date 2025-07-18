package org.developers.api.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RetrieveUserByEmail {
    @NotBlank(message = "Email should be not null")
    @Email(message = "Email should be valid")
    private String email;
}
