package org.developers.api.request.Video;

import lombok.Data;

@Data
public class GetLastestVideosRequest {
    private Integer page;
    private Integer size;
}
