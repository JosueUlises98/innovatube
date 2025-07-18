package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchResult {
    private String videoId;
    private String title;
    private String description;
    private String channelId;
    private String channelTitle;
    private String publishedAt;
    private String thumbnailUrl;
    private String liveBroadcastContent;
    private String kind;
} 