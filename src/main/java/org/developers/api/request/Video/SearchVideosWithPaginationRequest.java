package org.developers.api.request.Video;

import lombok.Data;

@Data
public class SearchVideosWithPaginationRequest {
    private String title;
    private int page;
    private int size;
}
