package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class GetSimilarFavorites {
    private Long videoId;
    private Integer limit;
}
