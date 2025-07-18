package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeChannelDetails {
    private String channelId;
    private String title;
    private String description;
    private String publishedAt;
    private String thumbnailUrl;
    private Long subscriberCount;
    private Long videoCount;
    private Long viewCount;
} 