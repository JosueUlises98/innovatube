package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeactivateInactiveUsers {
    @NotNull(message = "Treshold date cannot be null")
    private LocalDateTime tresholdDate;
}
