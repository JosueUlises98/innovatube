package org.developers.api.response.Video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    private String youtubeVideoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Duration duration;
    private LocalDateTime addedAt;
    private BigInteger viewCount;
    private BigInteger likeCount;
    private Long userId;
}
