package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetVideoStatisticsRequest {
    @NotNull(message = "Video Id is required")
    @Positive(message = "Video Id must be positive")
    private Long videoId;
}
