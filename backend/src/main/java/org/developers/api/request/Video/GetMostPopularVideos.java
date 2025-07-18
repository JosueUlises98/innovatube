package org.developers.api.request.Video;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetMostPopularVideos {
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;
    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be a positive number")
    @Min(value = 10, message = "Minimum limit is 1")
    @Max(value = 10000000, message = "Maximum limit is 10")
    private Integer limit;
}
