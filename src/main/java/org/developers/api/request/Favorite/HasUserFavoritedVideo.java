package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class HasUserFavoritedVideo {
    private Long userId;
    private Long videoId;
}
