package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetInactiveUsersSince {
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;
}
