package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetVideosAddedInPeriodRequest {

    @NotNull(message = "El campo 'startDate' no puede ser nulo")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "El campo 'startDate' debe tener el formato ISO-8601 (yyyy-MM-ddTHH:mm:ss)")
    private LocalDateTime startDate;

    @NotNull(message = "El campo 'endDate' no puede ser nulo")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "El campo 'endDate' debe tener el formato ISO-8601 (yyyy-MM-ddTHH:mm:ss)")
    private LocalDateTime endDate;
}
