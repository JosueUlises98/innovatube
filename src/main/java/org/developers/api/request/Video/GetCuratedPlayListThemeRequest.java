package org.developers.api.request.Video;

import lombok.Data;

@Data
public class GetCuratedPlayListThemeRequest {
    private String theme;
    private Integer limit;
}
