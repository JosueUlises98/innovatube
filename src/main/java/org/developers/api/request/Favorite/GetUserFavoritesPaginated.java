package org.developers.api.request.Favorite;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GetUserFavoritesPaginated {
    private Long userId;
    private Integer page;
    private Integer size;
}
