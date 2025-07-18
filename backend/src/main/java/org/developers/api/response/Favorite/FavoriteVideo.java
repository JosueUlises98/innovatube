package org.developers.api.response.Favorite;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteVideo {
    private LocalDateTime addedAt;
    private String youtubeVideoId;
    private String videoDescription;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String duration;
    private BigInteger viewCount;
    private BigInteger likes;
}
