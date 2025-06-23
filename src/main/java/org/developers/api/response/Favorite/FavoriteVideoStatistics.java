package org.developers.api.response.Favorite;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FavoriteVideoStatistics {
    private long totalFavorites;
    private long uniqueUsers;
    private long uniqueVideos;
    private Map<String, Long> favoritesByCategory;
}
