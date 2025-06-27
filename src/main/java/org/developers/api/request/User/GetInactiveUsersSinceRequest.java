package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetInactiveUsersSinceRequest {
    @NotNull(message = "Date cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format. Expected format is yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime date;
}
