package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeCategory {
    private String id;
    private String title;
    private String assignable;
    private String channelId;
} 