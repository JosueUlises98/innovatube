package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeCaption {
    private String id;
    private String videoId;
    private String language;
    private String languageCode;
    private String name;
    private String audioTrackId;
    private boolean isAutoSynced;
    private boolean isCC;
    private boolean isDraft;
    private boolean isEasyReader;
    private boolean isLarge;
    private boolean isDefault;
    private String lastUpdated;
} 