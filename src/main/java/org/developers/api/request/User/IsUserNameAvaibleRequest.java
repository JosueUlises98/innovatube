package org.developers.api.request.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IsUserNameAvaibleRequest {
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotNull(message = "User ID cannot be empty")
    private Long userId;
}
