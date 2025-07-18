package org.developers.api.response.Video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoStatisticsResponse {
    private Long totalViews;
    private Double averageDuration;
    private Long totalFavorites;
}
