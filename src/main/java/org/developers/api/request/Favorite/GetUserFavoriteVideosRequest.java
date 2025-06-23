package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class GetUserFavoriteVideosRequest {
    private Long userId;
}
