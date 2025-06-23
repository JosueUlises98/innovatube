package org.developers.api.request.Video;

import lombok.Data;

@Data
public class GetRecommendedVideosRequest {
    private Long userId;
    private Integer limit;
}
