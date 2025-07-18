package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetVideoStatistics {
    @NotNull(message = "Video ID cannot be null")
    @Positive(message = "Video ID must be a positive number")
    private Long videoId;
}
