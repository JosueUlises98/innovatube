package org.developers.service.interfaces;

import org.developers.api.response.YouTube.*;

import java.util.List;
import java.util.Optional;

public interface YouTubeService {

    YouTubeSearchResult searchVideos(String query, int maxResults, String regionCode);

    Optional<YouTubeVideoDetails> getVideoDetails(String videoId);

    Optional<YouTubeVideoStatistics> getVideoStatistics(String videoId);

    Optional<YouTubeChannelDetails> getChannelDetails(String channelId);

    List<YouTubeComment> getVideoComments(String videoId, int maxResults);

    List<YouTubeVideoDetails> getTrendingVideos(String regionCode, String categoryId, int maxResults);

    List<YouTubeCategory> getVideoCategories(String regionCode);

    List<YouTubeVideoDetails> getRelatedVideos(String videoId, int maxResults);

    Optional<YouTubePlaylist> getPlaylistDetails(String playlistId);

    List<YouTubeVideoDetails> getPlaylistVideos(String playlistId, int maxResults);

    List<YouTubeChannelDetails> searchChannels(String query, int maxResults);

    List<YouTubeVideoDetails> getChannelVideos(String channelId, int maxResults);
} 