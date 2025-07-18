package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoDetails {
    private String videoId;
    private String title;
    private String description;
    private String channelId;
    private String channelTitle;
    private String publishedAt;
    private String thumbnailUrl;
    private Long viewCount;
    private Long likeCount;
    private Long dislikeCount;
    private Long commentCount;
    private String duration;
    private List<String> tags;
    private Boolean isLive;
    private Boolean isHD;
    private String categoryId;
    private String categoryTitle;
} 