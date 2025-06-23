package org.developers.api.response.Video;


import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class VideoResponse {

    private String youtubeVideoId;

    private String title;

    private String description;

    private String thumbnailUrl;

    private String duration;

    private LocalDateTime addedAt;

    private BigInteger viewCount;
    private BigInteger likeCount;
}
