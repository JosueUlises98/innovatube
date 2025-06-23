package org.developers.api.response.Video;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoStatisticsResponse {
    long totalViews;
    double averageDuration;
    long totalFavorites;
}
