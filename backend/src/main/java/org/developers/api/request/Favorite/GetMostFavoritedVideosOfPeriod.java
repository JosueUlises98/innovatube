package org.developers.api.request.Favorite;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMostFavoritedVideosOfPeriod {
    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;
    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDate;
    @NotNull(message = "Limit cannot be null")
    @Positive(message = "Limit must be a positive number")
    private Integer limit;
}
