package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetTrendingVideos {
    private Long userId;
    @NotNull(message = "Minimum views cannot be null")
    @Positive(message = "Minimum views must be a positive number")
    private Integer minimumViews;
}
