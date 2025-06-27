package org.developers.api.request.Favorite;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.developers.model.entities.Video;


@Data
public class GetFavoriteStatistics {
    @NotNull(message = "Video ID cannot be null")
    @Positive(message = "Video ID must be a positive number")
    private Long videoId;
}
