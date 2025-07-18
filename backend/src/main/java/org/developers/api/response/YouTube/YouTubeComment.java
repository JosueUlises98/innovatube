package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeComment {
    private String commentId;
    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String textDisplay;
    private String publishedAt;
    private String updatedAt;
    private Long likeCount;
} 