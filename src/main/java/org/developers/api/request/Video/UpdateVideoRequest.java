package org.developers.api.request.Video;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateVideoRequest {
    private Long videoId;
    private String title;
    private String description;
    private LocalDateTime publishDate;
    private String thumbnailUrl;
    private Duration duration;
    private BigInteger views;
    private BigInteger likes;
}
