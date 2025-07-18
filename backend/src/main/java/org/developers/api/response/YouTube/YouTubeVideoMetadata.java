package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoMetadata {
    private String videoId;
    private String title;
    private String description;
    private String channelId;
    private String channelTitle;
    private String categoryId;
    private String categoryTitle;
    private String tags;
    private String language;
    private String country;
    private String license;
    private String privacyStatus;
    private String uploadStatus;
    private String embeddable;
    private String publicStatsViewable;
    private String madeForKids;
    private String defaultLanguage;
    private String defaultAudioLanguage;
    private String recordingDate;
    private String locationDescription;
    private String liveBroadcastContent;
    private String liveChatId;
    private String liveChatActive;
    private String liveChatConcurrentViewers;
    private String liveChatStartTime;
    private String liveChatEndTime;
} 