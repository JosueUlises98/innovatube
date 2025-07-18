package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeChapter {
    private String id;
    private String videoId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String thumbnailUrl;
    private int chapterNumber;
    private String lastUpdated;
} 