package org.developers.api.response.Video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private String id;
    private String youtubeVideoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String duration;
    private String addedAt;
    private BigInteger viewCount;
    private BigInteger likeCount;
}
