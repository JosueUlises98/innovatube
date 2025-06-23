package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class AddVideoToFavorites {
    private Long userId;
    private Long videoId;
}
