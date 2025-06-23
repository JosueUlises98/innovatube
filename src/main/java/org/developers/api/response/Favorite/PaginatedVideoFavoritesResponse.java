package org.developers.api.response.Favorite;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class PaginatedVideoFavoritesResponse {
    List<FavoriteVideoResponse> favorites;
    int currentPage;
    int totalPages;
    long totalElements;
    boolean hasNext;
    boolean hasPrevious;
}
