package org.developers.api.request.Favorite;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMostFavoritedVideosOfPeriod {
    @NotNull(message = "Start date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}", message = "Start date must follow the format YYYY-MM-DDTHH:mm")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}", message = "End date must follow the format YYYY-MM-DDTHH:mm")
    private LocalDateTime endDate;

    @NotNull(message = "Limit cannot be null")
    @Positive(message = "Limit must be a positive number")
    private Integer limit;
}
