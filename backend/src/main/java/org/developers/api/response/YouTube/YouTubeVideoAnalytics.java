package org.developers.api.response.YouTube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoAnalytics {
    private String videoId;
    private String channelId;
    private String date;
    private Long views;
    private Long estimatedMinutesWatched;
    private Double averageViewDuration;
    private Double averageViewPercentage;
    private Long subscribersGained;
    private Long subscribersLost;
    private Long likes;
    private Long dislikes;
    private Long shares;
    private Long comments;
    private Long favoritesAdded;
    private Long favoritesRemoved;
    private Long uniqueViewers;
    private Double averageViewerPercentage;
    private Long estimatedRevenue;
    private Long adImpressions;
    private Double cpm;
} 