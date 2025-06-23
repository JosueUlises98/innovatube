package org.developers.api.request.Favorite;

import lombok.Data;
import org.developers.model.entities.Video;


@Data
public class GetFavoriteStatistics {
    private Long videoId;
}
