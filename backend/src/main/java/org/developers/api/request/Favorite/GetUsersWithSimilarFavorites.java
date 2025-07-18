package org.developers.api.request.Favorite;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetUsersWithSimilarFavorites {
    @NotNull(message = "Video ID cannot be null")
    @Positive(message = "Video ID must be a positive number")
    private Long userId;
    @NotNull(message = "Minimum common cannot be null")
    @Positive(message = "Minimum common must be a positive number")
    private Integer minimumCommon;
    @NotNull(message = "Limit cannot be null")
    @Positive(message = "Limit must be a positive number")
    private Integer limit;
}
