package org.developers.api.response.Favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteVideoStatistics {
    private long totalFavorites;
    private long uniqueUsers;
    private long uniqueVideos;
}
