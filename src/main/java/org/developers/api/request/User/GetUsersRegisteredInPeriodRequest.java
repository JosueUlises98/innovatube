package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetUsersRegisteredInPeriodRequest {
    @NotNull(message = "Start date cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format for startDate. Expected format is yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format for endDate. Expected format is yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime endDate;
}
