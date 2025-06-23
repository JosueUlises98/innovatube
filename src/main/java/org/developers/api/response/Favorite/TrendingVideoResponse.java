package org.developers.api.response.Favorite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrendingVideoResponse {
    private String title;
    private long favoriteCount;
    private double trendingScore;
}
