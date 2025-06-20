package org.developers.model.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class VideoDTO {
    private Long videoId;
    private String youtubeVideoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String duration;
    private LocalDateTime addedAt;
    private Integer viewCount;
    private List<FavoriteDTO> favorites;

}
