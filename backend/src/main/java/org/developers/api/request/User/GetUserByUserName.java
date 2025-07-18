package org.developers.api.request.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetUserByUserName {
    @NotBlank(message = "Username cannot be empty")
    private String userName;
}
