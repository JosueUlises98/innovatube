package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class RemoveVideoFromFavorites {
    private Long userId;
    private Long videoId;
}
