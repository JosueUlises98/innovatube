package unit.YouTube;

import org.developers.api.response.YouTube.*;
import org.developers.service.interfaces.YouTubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YouTubeControllerTest {
    /*
        @Mock
    private YouTubeService youTubeService;

    @InjectMocks
    private org.developers.api.controllers.Video.YouTubeController youTubeController;

    private YouTubeVideoDetails sampleVideo;
    private YouTubeChannelDetails sampleChannel;
    private YouTubePlaylist samplePlaylist;
    private YouTubeComment sampleComment;
    private YouTubeCategory sampleCategory;

    @BeforeEach
    void setUp() {
        // Sample video data
        sampleVideo = new YouTubeVideoDetails(
            "dQw4w9WgXcQ",
            "Sample Video Title",
            "This is a sample video description",
            "https://example.com/thumbnail.jpg",
            "https://example.com/thumbnail-medium.jpg",
            "https://example.com/thumbnail-high.jpg",
            "Sample Channel",
            "UC_sample_channel",
            "2023-01-01T00:00:00Z",
            "Sample Channel",
            "UC_sample_channel",
            "PT4M13S",
            1000000L,
            50000L,
            1000L
        );

        // Sample channel data
        sampleChannel = new YouTubeChannelDetails(
            "UC_sample_channel",
            "Sample Channel",
            "This is a sample channel description",
            "https://example.com/channel-thumbnail.jpg",
            "https://example.com/channel-thumbnail-medium.jpg",
            "https://example.com/channel-thumbnail-high.jpg",
            "2020-01-01T00:00:00Z",
            100000L,
            500L,
            10000000L
        );

        // Sample playlist data
        samplePlaylist = new YouTubePlaylist(
            "PL_sample_playlist",
            "Sample Playlist",
            "This is a sample playlist description",
            "https://example.com/playlist-thumbnail.jpg",
            "https://example.com/playlist-thumbnail-medium.jpg",
            "https://example.com/playlist-thumbnail-high.jpg",
            "Sample Channel",
            "UC_sample_channel",
            "2023-01-01T00:00:00Z",
            50L
        );

        // Sample comment data
        sampleComment = new YouTubeComment(
            "comment_id_123",
            "dQw4w9WgXcQ",
            "John Doe",
            "https://example.com/avatar.jpg",
            "https://example.com/channel",
            "This is a great video!",
            10L,
            "2023-01-01T00:00:00Z",
            "2023-01-01T00:00:00Z"
        );

        // Sample category data
        sampleCategory = new YouTubeCategory(
            "1",
            "Film & Animation",
            true,
            "UC_sample_channel"
        );
    }

    @Test
    void testSearchVideos_Success() {
        // Given
        String query = "test video";
        int maxResults = 20;
        String regionCode = "US";

        YouTubeSearchResult searchResult = new YouTubeSearchResult(
            query,
            Arrays.asList(sampleVideo),
            1L,
            "nextPageToken",
            "prevPageToken"
        );

        when(youTubeService.searchVideos(query, maxResults, regionCode))
            .thenReturn(searchResult);

        // When
        ResponseEntity<YouTubeSearchResult> response = youTubeController.searchVideos(query, maxResults, regionCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(query, response.getBody().getQuery());
        assertEquals(1, response.getBody().getVideos().size());
        assertEquals(1L, response.getBody().getTotalResults());
    }

    @Test
    void testSearchVideos_Error() {
        // Given
        String query = "test video";
        int maxResults = 20;
        String regionCode = "US";

        when(youTubeService.searchVideos(query, maxResults, regionCode))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<YouTubeSearchResult> response = youTubeController.searchVideos(query, maxResults, regionCode);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetVideoDetails_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        when(youTubeService.getVideoDetails(videoId))
            .thenReturn(Optional.of(sampleVideo));

        // When
        ResponseEntity<YouTubeVideoDetails> response = youTubeController.getVideoDetails(videoId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(videoId, response.getBody().getVideoId());
        assertEquals("Sample Video Title", response.getBody().getTitle());
    }

    @Test
    void testGetVideoDetails_NotFound() {
        // Given
        String videoId = "invalid-video-id";

        when(youTubeService.getVideoDetails(videoId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<YouTubeVideoDetails> response = youTubeController.getVideoDetails(videoId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetVideoStatistics_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        YouTubeVideoStatistics stats = new YouTubeVideoStatistics(
            videoId,
            1000000L,
            50000L,
            100L,
            1000L
        );

        when(youTubeService.getVideoStatistics(videoId))
            .thenReturn(Optional.of(stats));

        // When
        ResponseEntity<YouTubeVideoStatistics> response = youTubeController.getVideoStatistics(videoId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(videoId, response.getBody().getVideoId());
        assertEquals(1000000L, response.getBody().getViewCount());
        assertEquals(50000L, response.getBody().getLikeCount());
    }

    @Test
    void testGetChannelDetails_Success() {
        // Given
        String channelId = "UC_sample_channel";

        when(youTubeService.getChannelDetails(channelId))
            .thenReturn(Optional.of(sampleChannel));

        // When
        ResponseEntity<YouTubeChannelDetails> response = youTubeController.getChannelDetails(channelId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(channelId, response.getBody().getChannelId());
        assertEquals("Sample Channel", response.getBody().getTitle());
    }

    @Test
    void testGetVideoComments_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 20;
        List<YouTubeComment> comments = Arrays.asList(sampleComment);

        when(youTubeService.getVideoComments(videoId, maxResults))
            .thenReturn(comments);

        // When
        ResponseEntity<List<YouTubeComment>> response = youTubeController.getVideoComments(videoId, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(videoId, response.getBody().get(0).getVideoId());
    }

    @Test
    void testGetTrendingVideos_Success() {
        // Given
        String regionCode = "US";
        String categoryId = "1";
        int maxResults = 20;
        List<YouTubeVideoDetails> videos = Arrays.asList(sampleVideo);

        when(youTubeService.getTrendingVideos(regionCode, categoryId, maxResults))
            .thenReturn(videos);

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getTrendingVideos(regionCode, categoryId, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetVideoCategories_Success() {
        // Given
        String regionCode = "US";
        List<YouTubeCategory> categories = Arrays.asList(sampleCategory);

        when(youTubeService.getVideoCategories(regionCode))
            .thenReturn(categories);

        // When
        ResponseEntity<List<YouTubeCategory>> response = youTubeController.getVideoCategories(regionCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("1", response.getBody().get(0).getId());
        assertEquals("Film & Animation", response.getBody().get(0).getTitle());
    }

    @Test
    void testGetRelatedVideos_Success() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 20;
        List<YouTubeVideoDetails> videos = Arrays.asList(sampleVideo);

        when(youTubeService.getRelatedVideos(videoId, maxResults))
            .thenReturn(videos);

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getRelatedVideos(videoId, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetPlaylistDetails_Success() {
        // Given
        String playlistId = "PL_sample_playlist";

        when(youTubeService.getPlaylistDetails(playlistId))
            .thenReturn(Optional.of(samplePlaylist));

        // When
        ResponseEntity<YouTubePlaylist> response = youTubeController.getPlaylistDetails(playlistId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getPlaylistId());
        assertEquals("Sample Playlist", response.getBody().getTitle());
    }

    @Test
    void testGetPlaylistVideos_Success() {
        // Given
        String playlistId = "PL_sample_playlist";
        int maxResults = 20;
        List<YouTubeVideoDetails> videos = Arrays.asList(sampleVideo);

        when(youTubeService.getPlaylistVideos(playlistId, maxResults))
            .thenReturn(videos);

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getPlaylistVideos(playlistId, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchChannels_Success() {
        // Given
        String query = "sample channel";
        int maxResults = 20;
        List<YouTubeChannelDetails> channels = Arrays.asList(sampleChannel);

        when(youTubeService.searchChannels(query, maxResults))
            .thenReturn(channels);

        // When
        ResponseEntity<List<YouTubeChannelDetails>> response = youTubeController.searchChannels(query, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("UC_sample_channel", response.getBody().get(0).getChannelId());
    }

    @Test
    void testGetChannelVideos_Success() {
        // Given
        String channelId = "UC_sample_channel";
        int maxResults = 20;
        List<YouTubeVideoDetails> videos = Arrays.asList(sampleVideo);

        when(youTubeService.getChannelVideos(channelId, maxResults))
            .thenReturn(videos);

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getChannelVideos(channelId, maxResults);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(channelId, response.getBody().get(0).getChannelId());
    }

    @Test
    void testGetVideoStatistics_Error() {
        // Given
        String videoId = "dQw4w9WgXcQ";

        when(youTubeService.getVideoStatistics(videoId))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<YouTubeVideoStatistics> response = youTubeController.getVideoStatistics(videoId);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetChannelDetails_Error() {
        // Given
        String channelId = "UC_sample_channel";

        when(youTubeService.getChannelDetails(channelId))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<YouTubeChannelDetails> response = youTubeController.getChannelDetails(channelId);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetVideoComments_Error() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 20;

        when(youTubeService.getVideoComments(videoId, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeComment>> response = youTubeController.getVideoComments(videoId, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetTrendingVideos_Error() {
        // Given
        String regionCode = "US";
        String categoryId = "1";
        int maxResults = 20;

        when(youTubeService.getTrendingVideos(regionCode, categoryId, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getTrendingVideos(regionCode, categoryId, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetVideoCategories_Error() {
        // Given
        String regionCode = "US";

        when(youTubeService.getVideoCategories(regionCode))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeCategory>> response = youTubeController.getVideoCategories(regionCode);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetRelatedVideos_Error() {
        // Given
        String videoId = "dQw4w9WgXcQ";
        int maxResults = 20;

        when(youTubeService.getRelatedVideos(videoId, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getRelatedVideos(videoId, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetPlaylistDetails_NotFound() {
        // Given
        String playlistId = "invalid-playlist-id";

        when(youTubeService.getPlaylistDetails(playlistId))
            .thenReturn(Optional.empty());

        // When
        ResponseEntity<YouTubePlaylist> response = youTubeController.getPlaylistDetails(playlistId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetPlaylistVideos_Error() {
        // Given
        String playlistId = "PL_sample_playlist";
        int maxResults = 20;

        when(youTubeService.getPlaylistVideos(playlistId, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getPlaylistVideos(playlistId, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSearchChannels_Error() {
        // Given
        String query = "sample channel";
        int maxResults = 20;

        when(youTubeService.searchChannels(query, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeChannelDetails>> response = youTubeController.searchChannels(query, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetChannelVideos_Error() {
        // Given
        String channelId = "UC_sample_channel";
        int maxResults = 20;

        when(youTubeService.getChannelVideos(channelId, maxResults))
            .thenThrow(new RuntimeException("API Error"));

        // When
        ResponseEntity<List<YouTubeVideoDetails>> response = youTubeController.getChannelVideos(channelId, maxResults);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
     */
} 