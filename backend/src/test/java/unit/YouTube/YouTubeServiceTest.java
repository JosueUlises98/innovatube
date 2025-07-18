package unit.YouTube;

import org.developers.api.response.YouTube.*;
import org.developers.common.config.YoutubeConfig;
import org.developers.service.impl.YouTubeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YouTubeServiceTest {

    /*
        @Mock
    private YoutubeConfig youtubeConfig;

    @InjectMocks
    private YouTubeServiceImpl youTubeService;

    @BeforeEach
    void setUp() {
        when(youtubeConfig.getApiKey()).thenReturn("test-api-key");
    }

    @Test
    void testSearchVideos_Success() {
        // Given
        String query = "test video";
        int maxResults = 10;
        String regionCode = "US";

        // When
        YouTubeSearchResult result = youTubeService.searchVideos(query, maxResults, regionCode);

        // Then
        assertNotNull(result);
        assertEquals(query, result.getQuery());
        assertNotNull(result.getVideos());
        assertTrue(result.getTotalResults() >= 0);
    }

    @Test
    void testGetVideoDetails_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        // When
        Optional<YouTubeVideoDetails> result = youTubeService.getVideoDetails(videoId);

        // Then
        assertTrue(result.isPresent());
        YouTubeVideoDetails video = result.get();
        assertEquals(videoId, video.getVideoId());
        assertNotNull(video.getTitle());
        assertNotNull(video.getDescription());
        assertNotNull(video.getChannelTitle());
    }

    @Test
    void testGetVideoDetails_NotFound() {
        // Given
        String videoId = "invalid-video-id";

        // When
        Optional<YouTubeVideoDetails> result = youTubeService.getVideoDetails(videoId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetVideoStatistics_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        // When
        Optional<YouTubeVideoStatistics> result = youTubeService.getVideoStatistics(videoId);

        // Then
        assertTrue(result.isPresent());
        YouTubeVideoStatistics stats = result.get();
        assertEquals(videoId, stats.getVideoId());
        assertTrue(stats.getViewCount() >= 0);
        assertTrue(stats.getLikeCount() >= 0);
        assertTrue(stats.getCommentCount() >= 0);
    }

    @Test
    void testGetChannelDetails_Success() {
        // Given
        String channelId = "UC_x5XG1OV2P6uZZ5FSM9Ttw";

        // When
        Optional<YouTubeChannelDetails> result = youTubeService.getChannelDetails(channelId);

        // Then
        assertTrue(result.isPresent());
        YouTubeChannelDetails channel = result.get();
        assertEquals(channelId, channel.getChannelId());
        assertNotNull(channel.getTitle());
        assertNotNull(channel.getDescription());
        assertTrue(channel.getSubscriberCount() >= 0);
        assertTrue(channel.getVideoCount() >= 0);
    }

    @Test
    void testGetVideoComments_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 10;

        // When
        List<YouTubeComment> result = youTubeService.getVideoComments(videoId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeComment comment = result.get(0);
            assertEquals(videoId, comment.getVideoId());
            assertNotNull(comment.getAuthorDisplayName());
            assertNotNull(comment.getTextDisplay());
            assertTrue(comment.getLikeCount() >= 0);
        }
    }

    @Test
    void testGetTrendingVideos_Success() {
        // Given
        String regionCode = "US";
        String categoryId = null;
        int maxResults = 10;

        // When
        List<YouTubeVideoDetails> result = youTubeService.getTrendingVideos(regionCode, categoryId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeVideoDetails video = result.get(0);
            assertNotNull(video.getVideoId());
            assertNotNull(video.getTitle());
            assertNotNull(video.getChannelTitle());
        }
    }

    @Test
    void testGetVideoCategories_Success() {
        // Given
        String regionCode = "US";

        // When
        List<YouTubeCategory> result = youTubeService.getVideoCategories(regionCode);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        YouTubeCategory category = result.get(0);
        assertNotNull(category.getId());
        assertNotNull(category.getTitle());
        assertNotNull(category.getAssignable());
    }

    @Test
    void testGetRelatedVideos_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 10;

        // When
        List<YouTubeVideoDetails> result = youTubeService.getRelatedVideos(videoId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeVideoDetails video = result.get(0);
            assertNotNull(video.getVideoId());
            assertNotNull(video.getTitle());
            assertNotNull(video.getChannelTitle());
        }
    }

    @Test
    void testGetPlaylistDetails_Success() {
        // Given
        String playlistId = "PLrAXtmRdnEQyWppSaR4h4j6tQ5jccKq3e";

        // When
        Optional<YouTubePlaylist> result = youTubeService.getPlaylistDetails(playlistId);

        // Then
        assertTrue(result.isPresent());
        YouTubePlaylist playlist = result.get();
        assertEquals(playlistId, playlist.getPlaylistId());
        assertNotNull(playlist.getTitle());
        assertNotNull(playlist.getDescription());
        assertNotNull(playlist.getChannelTitle());
        assertTrue(playlist.getItemCount() >= 0);
    }

    @Test
    void testGetPlaylistVideos_Success() {
        // Given
        String playlistId = "PLrAXtmRdnEQyWppSaR4h4j6tQ5jccKq3e";
        int maxResults = 10;

        // When
        List<YouTubeVideoDetails> result = youTubeService.getPlaylistVideos(playlistId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeVideoDetails video = result.get(0);
            assertNotNull(video.getVideoId());
            assertNotNull(video.getTitle());
            assertNotNull(video.getChannelTitle());
        }
    }

    @Test
    void testSearchChannels_Success() {
        // Given
        String query = "Google Developers";
        int maxResults = 10;

        // When
        List<YouTubeChannelDetails> result = youTubeService.searchChannels(query, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeChannelDetails channel = result.get(0);
            assertNotNull(channel.getChannelId());
            assertNotNull(channel.getTitle());
            assertNotNull(channel.getDescription());
        }
    }

    @Test
    void testGetChannelVideos_Success() {
        // Given
        String channelId = "UC_x5XG1OV2P6uZZ5FSM9Ttw";
        int maxResults = 10;

        // When
        List<YouTubeVideoDetails> result = youTubeService.getChannelVideos(channelId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);

        if (!result.isEmpty()) {
            YouTubeVideoDetails video = result.get(0);
            assertNotNull(video.getVideoId());
            assertNotNull(video.getTitle());
            assertEquals(channelId, video.getChannelId());
        }
    }

    @Test
    void testSearchVideos_WithEmptyQuery() {
        // Given
        String query = "";
        int maxResults = 10;
        String regionCode = "US";

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            youTubeService.searchVideos(query, maxResults, regionCode);
        });
    }

    @Test
    void testGetVideoDetails_WithInvalidVideoId() {
        // Given
        String videoId = "invalid-video-id-12345";

        // When
        Optional<YouTubeVideoDetails> result = youTubeService.getVideoDetails(videoId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetChannelDetails_WithInvalidChannelId() {
        // Given
        String channelId = "invalid-channel-id-12345";

        // When
        Optional<YouTubeChannelDetails> result = youTubeService.getChannelDetails(channelId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetPlaylistDetails_WithInvalidPlaylistId() {
        // Given
        String playlistId = "invalid-playlist-id-12345";

        // When
        Optional<YouTubePlaylist> result = youTubeService.getPlaylistDetails(playlistId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testSearchVideos_WithLargeMaxResults() {
        // Given
        String query = "test";
        int maxResults = 100;
        String regionCode = "US";

        // When
        YouTubeSearchResult result = youTubeService.searchVideos(query, maxResults, regionCode);

        // Then
        assertNotNull(result);
        assertTrue(result.getVideos().size() <= maxResults);
    }

    @Test
    void testGetVideoComments_WithZeroMaxResults() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 0;

        // When
        List<YouTubeComment> result = youTubeService.getVideoComments(videoId, maxResults);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetTrendingVideos_WithCategory() {
        // Given
        String regionCode = "US";
        String categoryId = "1"; // Film & Animation
        int maxResults = 10;

        // When
        List<YouTubeVideoDetails> result = youTubeService.getTrendingVideos(regionCode, categoryId, maxResults);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= maxResults);
    }

    @Test
    void testGetVideoStatistics_WithHighViewCount() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        // When
        Optional<YouTubeVideoStatistics> result = youTubeService.getVideoStatistics(videoId);

        // Then
        assertTrue(result.isPresent());
        YouTubeVideoStatistics stats = result.get();
        assertTrue(stats.getViewCount() > 0);
    }

    @Test
    void testGetChannelDetails_WithHighSubscriberCount() {
        // Given
        String channelId = "UC_x5XG1OV2P6uZZ5FSM9Ttw"; // Google Developers

        // When
        Optional<YouTubeChannelDetails> result = youTubeService.getChannelDetails(channelId);

        // Then
        assertTrue(result.isPresent());
        YouTubeChannelDetails channel = result.get();
        assertTrue(channel.getSubscriberCount() > 0);
        assertTrue(channel.getVideoCount() > 0);
    }
     */
} 