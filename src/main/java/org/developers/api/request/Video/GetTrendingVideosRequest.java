package org.developers.api.request.Video;

import lombok.Data;

@Data
public class GetTrendingVideosRequest {
    private Integer minimumViews;
}
