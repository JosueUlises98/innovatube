package org.developers.api.request.Favorite;

import lombok.Data;

@Data
public class GetUsersWithSimilarFavorites {
    private Long userId;
    private Integer minimumCommon;
    private Integer limit;
}
