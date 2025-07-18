package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class UpdateVideo {
    @NotNull(message = "Video ID cannot be null")
    private Long videoId;
    private String youtubeId;
    private String title;
    private String description;
    private String publishDate;
    private String thumbnailUrl;
    private Duration duration;
    private Long views;
    private Long likes;
}
