package org.developers.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.developers.api.response.YouTube.*;
import org.developers.common.config.YoutubeConfig;
import org.developers.service.interfaces.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "InnovaTube";
    
    @Autowired
    private YoutubeConfig youtubeConfig;
    
    private YouTube getYouTubeService() throws Exception {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    @Override
    public YouTubeSearchResult searchVideos(String query, int maxResults, String regionCode) {
        try {
            YouTube.Search.List searchRequest = getYouTubeService().search().list(List.of("snippet"));
            searchRequest.setKey(youtubeConfig.getApiKey());
            searchRequest.setQ(query);
            searchRequest.setType(List.of("video"));
            searchRequest.setMaxResults((long) maxResults);
            searchRequest.setRegionCode(regionCode);
            searchRequest.setOrder("relevance");
            
            SearchListResponse response = searchRequest.execute();
            
            List<YouTubeVideoDetails> videos = new ArrayList<>();
            for (SearchResult item : response.getItems()) {
                YouTubeVideoDetails video = convertToYouTubeVideoDetails(item);
                videos.add(video);
            }
            
            return YouTubeSearchResult.builder()
                .videoId(videos.isEmpty() ? "" : videos.getFirst().getVideoId())
                .title("Search Results for: " + query)
                .description("YouTube search results")
                .channelId("")
                .channelTitle("")
                .publishedAt("")
                .thumbnailUrl("")
                .liveBroadcastContent("none")
                .kind("youtube#searchResult")
                .build();
            
        } catch (Exception e) {
            throw new RuntimeException("Error searching YouTube videos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<YouTubeVideoDetails> getVideoDetails(String videoId) {
        try {
            YouTube.Videos.List videoRequest = getYouTubeService().videos().list(List.of("snippet,contentDetails,statistics"));
            videoRequest.setKey(youtubeConfig.getApiKey());
            videoRequest.setId(List.of(videoId));
            
            VideoListResponse response = videoRequest.execute();
            
            if (response.getItems().isEmpty()) {
                return Optional.empty();
            }
            
            Video video = response.getItems().getFirst();
            return Optional.of(convertToYouTubeVideoDetails(video));
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting video details: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<YouTubeVideoStatistics> getVideoStatistics(String videoId) {
        try {
            YouTube.Videos.List videoRequest = getYouTubeService().videos().list(List.of("statistics"));
            videoRequest.setKey(youtubeConfig.getApiKey());
            videoRequest.setId(List.of(videoId));
            
            VideoListResponse response = videoRequest.execute();
            
            if (response.getItems().isEmpty()) {
                return Optional.empty();
            }
            
            VideoStatistics stats = response.getItems().getFirst().getStatistics();
            
            return Optional.of(YouTubeVideoStatistics.builder()
                .videoId(videoId)
                .viewCount(stats.getViewCount() != null ? stats.getViewCount().longValue() : 0L)
                .likeCount(stats.getLikeCount() != null ? stats.getLikeCount().longValue() : 0L)
                .dislikeCount(stats.getDislikeCount() != null ? stats.getDislikeCount().longValue() : 0L)
                .favoriteCount(stats.getFavoriteCount() != null ? stats.getFavoriteCount().longValue() : 0L)
                .commentCount(stats.getCommentCount() != null ? stats.getCommentCount().longValue() : 0L)
                .build());
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting video statistics: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<YouTubeChannelDetails> getChannelDetails(String channelId) {
        try {
            YouTube.Channels.List channelRequest = getYouTubeService().channels().list(List.of("snippet,statistics"));
            channelRequest.setKey(youtubeConfig.getApiKey());
            channelRequest.setId(List.of(channelId));
            
            ChannelListResponse response = channelRequest.execute();
            
            if (response.getItems().isEmpty()) {
                return Optional.empty();
            }
            
            Channel channel = response.getItems().getFirst();
            ChannelSnippet snippet = channel.getSnippet();
            ChannelStatistics stats = channel.getStatistics();
            
            return Optional.of(YouTubeChannelDetails.builder()
                .channelId(channelId)
                .title(snippet.getTitle())
                .description(snippet.getDescription())
                .publishedAt(snippet.getPublishedAt().toString())
                .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
                .subscriberCount(stats.getSubscriberCount() != null ? stats.getSubscriberCount().longValue() : 0L)
                .videoCount(stats.getVideoCount() != null ? stats.getVideoCount().longValue() : 0L)
                .viewCount(stats.getViewCount() != null ? stats.getViewCount().longValue() : 0L)
                .build());
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting channel details: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeComment> getVideoComments(String videoId, int maxResults) {
        try {
            YouTube.CommentThreads.List commentRequest = getYouTubeService().commentThreads().list(List.of("snippet"));
            commentRequest.setKey(youtubeConfig.getApiKey());
            commentRequest.setVideoId(videoId);
            commentRequest.setMaxResults((long) maxResults);
            commentRequest.setOrder("relevance");
            
            CommentThreadListResponse response = commentRequest.execute();
            
            List<YouTubeComment> comments = new ArrayList<>();
            for (CommentThread thread : response.getItems()) {
                Comment comment = thread.getSnippet().getTopLevelComment();
                CommentSnippet snippet = comment.getSnippet();
                
                comments.add(YouTubeComment.builder()
                    .commentId(comment.getId())
                    .authorDisplayName(snippet.getAuthorDisplayName())
                    .authorProfileImageUrl(snippet.getAuthorProfileImageUrl())
                    .textDisplay(snippet.getTextDisplay())
                    .publishedAt(snippet.getPublishedAt().toString())
                    .updatedAt(snippet.getUpdatedAt().toString())
                    .likeCount(snippet.getLikeCount() != null ? snippet.getLikeCount() : 0L)
                    .build());
            }
            
            return comments;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting video comments: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeVideoDetails> getTrendingVideos(String regionCode, String categoryId, int maxResults) {
        try {
            YouTube.Videos.List videoRequest = getYouTubeService().videos().list(List.of("snippet,contentDetails,statistics"));
            videoRequest.setKey(youtubeConfig.getApiKey());
            videoRequest.setChart("mostPopular");
            videoRequest.setRegionCode(regionCode);
            if (categoryId != null && !categoryId.isEmpty()) {
                videoRequest.setVideoCategoryId(categoryId);
            }
            videoRequest.setMaxResults((long) maxResults);
            
            VideoListResponse response = videoRequest.execute();
            
            List<YouTubeVideoDetails> videos = new ArrayList<>();
            for (Video video : response.getItems()) {
                videos.add(convertToYouTubeVideoDetails(video));
            }
            
            return videos;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting trending videos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeCategory> getVideoCategories(String regionCode) {
        try {
            YouTube.VideoCategories.List categoryRequest = getYouTubeService().videoCategories().list(List.of("snippet"));
            categoryRequest.setKey(youtubeConfig.getApiKey());
            categoryRequest.setRegionCode(regionCode);
            
            VideoCategoryListResponse response = categoryRequest.execute();
            
            List<YouTubeCategory> categories = new ArrayList<>();
            for (VideoCategory category : response.getItems()) {
                VideoCategorySnippet snippet = category.getSnippet();
                categories.add(YouTubeCategory.builder()
                    .id(category.getId())
                    .title(snippet.getTitle())
                    .assignable(snippet.getAssignable() != null ? snippet.getAssignable().toString() : "false")
                    .channelId(snippet.getChannelId())
                    .build());
            }
            
            return categories;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting video categories: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeVideoDetails> getRelatedVideos(String videoId, int maxResults) {
        try {
            YouTube.Search.List searchRequest = getYouTubeService().search().list(List.of("snippet"));
            searchRequest.setKey(youtubeConfig.getApiKey());
            searchRequest.setType(List.of("video"));
            searchRequest.setMaxResults((long) maxResults);
            // Note: setRelatedToVideoId is not available in the YouTube API
            searchRequest.setOrder("relevance");
            
            SearchListResponse response = searchRequest.execute();
            
            List<YouTubeVideoDetails> videos = new ArrayList<>();
            for (SearchResult item : response.getItems()) {
                videos.add(convertToYouTubeVideoDetails(item));
            }
            
            return videos;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting related videos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<YouTubePlaylist> getPlaylistDetails(String playlistId) {
        try {
            YouTube.Playlists.List playlistRequest = getYouTubeService().playlists().list(List.of("snippet,contentDetails"));
            playlistRequest.setKey(youtubeConfig.getApiKey());
            playlistRequest.setId(List.of(playlistId));
            
            PlaylistListResponse response = playlistRequest.execute();
            
            if (response.getItems().isEmpty()) {
                return Optional.empty();
            }
            
            Playlist playlist = response.getItems().get(0);
            PlaylistSnippet snippet = playlist.getSnippet();
            
            return Optional.of(YouTubePlaylist.builder()
                .playlistId(playlistId)
                .title(snippet.getTitle())
                .description(snippet.getDescription())
                .channelId(snippet.getChannelId())
                .channelTitle(snippet.getChannelTitle())
                .publishedAt(snippet.getPublishedAt().toString())
                .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
                .build());
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting playlist details: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeVideoDetails> getPlaylistVideos(String playlistId, int maxResults) {
        try {
            YouTube.PlaylistItems.List playlistItemsRequest = getYouTubeService().playlistItems().list(List.of("snippet,contentDetails"));
            playlistItemsRequest.setKey(youtubeConfig.getApiKey());
            playlistItemsRequest.setPlaylistId(playlistId);
            playlistItemsRequest.setMaxResults((long) maxResults);
            
            PlaylistItemListResponse response = playlistItemsRequest.execute();
            
            List<YouTubeVideoDetails> videos = new ArrayList<>();
            for (PlaylistItem item : response.getItems()) {
                PlaylistItemSnippet snippet = item.getSnippet();
                videos.add(YouTubeVideoDetails.builder()
                    .videoId(snippet.getResourceId().getVideoId())
                    .title(snippet.getTitle())
                    .description(snippet.getDescription())
                    .channelId(snippet.getChannelId())
                    .channelTitle(snippet.getChannelTitle())
                    .publishedAt(snippet.getPublishedAt().toString())
                    .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
                    .viewCount(0L)
                    .likeCount(0L)
                    .dislikeCount(0L)
                    .commentCount(0L)
                    .duration("")
                    .tags(List.of())
                    .isLive(false)
                    .isHD(false)
                    .categoryId("")
                    .categoryTitle("")
                    .build());
            }
            
            return videos;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting playlist videos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeChannelDetails> searchChannels(String query, int maxResults) {
        try {
            YouTube.Search.List searchRequest = getYouTubeService().search().list(List.of("snippet"));
            searchRequest.setKey(youtubeConfig.getApiKey());
            searchRequest.setQ(query);
            searchRequest.setType(List.of("channel"));
            searchRequest.setMaxResults((long) maxResults);
            searchRequest.setOrder("relevance");
            
            SearchListResponse response = searchRequest.execute();
            
            List<YouTubeChannelDetails> channels = new ArrayList<>();
            for (SearchResult item : response.getItems()) {
                SearchResultSnippet snippet = item.getSnippet();
                channels.add(YouTubeChannelDetails.builder()
                    .channelId(item.getId().getChannelId())
                    .title(snippet.getTitle())
                    .description(snippet.getDescription())
                    .publishedAt(snippet.getPublishedAt().toString())
                    .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
                    .subscriberCount(0L)
                    .videoCount(0L)
                    .viewCount(0L)
                    .build());
            }
            
            return channels;
            
        } catch (Exception e) {
            throw new RuntimeException("Error searching channels: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<YouTubeVideoDetails> getChannelVideos(String channelId, int maxResults) {
        try {
            YouTube.Search.List searchRequest = getYouTubeService().search().list(List.of("snippet"));
            searchRequest.setKey(youtubeConfig.getApiKey());
            searchRequest.setChannelId(channelId);
            searchRequest.setType(List.of("video"));
            searchRequest.setMaxResults((long) maxResults);
            searchRequest.setOrder("date");
            
            SearchListResponse response = searchRequest.execute();
            
            List<YouTubeVideoDetails> videos = new ArrayList<>();
            for (SearchResult item : response.getItems()) {
                videos.add(convertToYouTubeVideoDetails(item));
            }
            
            return videos;
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting channel videos: " + e.getMessage(), e);
        }
    }
    
    private YouTubeVideoDetails convertToYouTubeVideoDetails(SearchResult item) {
        SearchResultSnippet snippet = item.getSnippet();
        return YouTubeVideoDetails.builder()
            .videoId(item.getId().getVideoId())
            .title(snippet.getTitle())
            .description(snippet.getDescription())
            .channelId(snippet.getChannelId())
            .channelTitle(snippet.getChannelTitle())
            .publishedAt(snippet.getPublishedAt().toString())
            .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
            .viewCount(0L)
            .likeCount(0L)
            .dislikeCount(0L)
            .commentCount(0L)
            .duration("")
            .tags(List.of())
            .isLive(false)
            .isHD(false)
            .categoryId("")
            .categoryTitle("")
            .build();
    }
    
    private YouTubeVideoDetails convertToYouTubeVideoDetails(Video video) {
        VideoSnippet snippet = video.getSnippet();
        VideoStatistics stats = video.getStatistics();
        VideoContentDetails contentDetails = video.getContentDetails();
        
        return YouTubeVideoDetails.builder()
            .videoId(video.getId())
            .title(snippet.getTitle())
            .description(snippet.getDescription())
            .channelId(snippet.getChannelId())
            .channelTitle(snippet.getChannelTitle())
            .publishedAt(snippet.getPublishedAt().toString())
            .thumbnailUrl(snippet.getThumbnails().getDefault().getUrl())
            .viewCount(stats.getViewCount() != null ? stats.getViewCount().longValue() : 0L)
            .likeCount(stats.getLikeCount() != null ? stats.getLikeCount().longValue() : 0L)
            .dislikeCount(stats.getDislikeCount() != null ? stats.getDislikeCount().longValue() : 0L)
            .commentCount(stats.getCommentCount() != null ? stats.getCommentCount().longValue() : 0L)
            .duration(contentDetails.getDuration())
            .tags(snippet.getTags() != null ? snippet.getTags() : List.of())
            .isLive(contentDetails.getLicensedContent() != null ? contentDetails.getLicensedContent() : false)
            .isHD(contentDetails.getHasCustomThumbnail() != null ? contentDetails.getHasCustomThumbnail() : false)
            .categoryId(snippet.getCategoryId())
            .categoryTitle("")
            .build();
    }
} 