package org.developers.api.response.Favorite;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PaginatedVideoFavorites {
    List<FavoriteVideo> favorites;
    int currentPage;
    int totalPages;
    long totalElements;
    boolean hasNext;
    boolean hasPrevious;
}
