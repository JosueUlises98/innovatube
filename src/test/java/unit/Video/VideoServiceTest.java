package unit.Video;

import com.google.api.services.youtube.YouTube;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.model.entities.Video;
import org.developers.model.mapper.VideoMapper;
import org.developers.repository.VideoRepository;
import org.developers.service.impl.VideoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private VideoMapper videoMapper;
    @Mock
    private YouTube youtube;

    @InjectMocks
    private VideoServiceImpl videoService;

    private LocalDateTime now;
    private Video mockVideo;
    private VideoResponse mockVideoResponse;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        mockVideo = Video.builder()
                .videoId(1L)
                .youtubeVideoId("video-proof-1")
                .title("camiones en china")
                .description("camiones urbanos en piojang")
                .thumbnailUrl("https:localhost:8080")
                .duration("10")
                .addedAt(LocalDateTime.now())
                .viewCount(BigInteger.valueOf(125000L))
                .likes(BigInteger.valueOf(23000L))
                .build();

        mockVideoResponse = VideoResponse.builder()
                .youtubeVideoId("video-proof-1")
                .title("camiones en china")
                .description("camiones urbanos en piojang")
                .thumbnailUrl("https:localhost:8080")
                .duration("10")
                .addedAt(LocalDateTime.now())
                .viewCount(BigInteger.valueOf(125000L))
                .likeCount(BigInteger.valueOf(23000L))
                .build();
    }

    @Test
    void createVideo_SuccessfulCreation() {
        // Arrange
        CreateVideoRequest request = new CreateVideoRequest();
        request.setYoutubeId("video-proof-1");

        when(videoMapper.toEntity(mockVideoResponse)).thenReturn(mockVideo);
        when(videoRepository.save(mockVideo)).thenReturn(mockVideo);
        when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

        // Act
        VideoResponse response = videoService.createVideo(request);

        // Assert
        assertNotNull(response);
        verify(videoRepository).save(mockVideo);
    }

    @Test
    void updateVideo_SuccessfulUpdate() {
        // Arrange
        UpdateVideoRequest request = UpdateVideoRequest.builder()
                .videoId(1L)
                .title("motos en china")
                .description("motos electricas en piojang")
                .publishDate(LocalDateTime.now())
                .thumbnailUrl("https:localhost:8081")
                .duration(Duration.ofMinutes(20))
                .views(BigInteger.valueOf(125040L))
                .likes(BigInteger.valueOf(23001L))
                .build();

        when(videoRepository.findById(request.getVideoId())).thenReturn(Optional.of(mockVideo));
        when(videoRepository.save(mockVideo)).thenReturn(mockVideo);
        when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

        // Act
        VideoResponse response = videoService.updateVideo(request);

        // Assert
        assertNotNull(response);
        verify(videoRepository).save(mockVideo);
    }

    @Test
    void searchVideosByTitle_ReturnsMatchingVideos() {
        // Arrange
        SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
        request.setTitle("motos en china");
        List<Video> videos = Collections.singletonList(mockVideo);
        List<VideoResponse> expectedResponses = Collections.singletonList(mockVideoResponse);

        when(videoRepository.findByTitleContainingIgnoreCase(request.getTitle()))
                .thenReturn(videos);
        when(videoMapper.toVideoResponse(mockVideo)).thenReturn(expectedResponses.getFirst());

        // Act
        List<VideoResponse> responses = videoService.searchVideosByTitle(request);

        // Assert
        assertFalse(responses.isEmpty());
        assertEquals(2, responses.size());
    }

    @Test
    void getLastestVideos_ReturnsPagedResults() {
        // Arrange
        GetLastestVideosRequest request = new GetLastestVideosRequest();
        request.setPage(1);
        request.setSize(3);

        Page<Video> videoPage = new PageImpl<>(List.of(mockVideo,mockVideo,mockVideo));
        when(videoRepository.findAll(videoPage.getPageable())).thenReturn(videoPage);

        // Act
        PaginatedResponse<VideoResponse> response = videoService.getLastestVideos(request);

        // Assert
        assertNotNull(response);
        assertEquals(3, response.getContent().size());
    }

    @Test
    void getTrendingVideos_ReturnsVideos() {
        // Arrange
        GetTrendingVideosRequest request = new GetTrendingVideosRequest();
        request.setMinimumViews(1000);
        List<Video> trendingVideos = List.of(mockVideo, mockVideo);
        List<VideoResponse> expectedResponses = Arrays.asList(mockVideoResponse, mockVideoResponse);

        when(videoRepository.findPopularVideos(request.getMinimumViews())).thenReturn(trendingVideos);
        when(videoMapper.toVideoResponse(mockVideo)).thenReturn(expectedResponses.getFirst());

        // Act
        List<VideoResponse> responses = videoService.getTrendingVideos(request);

        // Assert
        assertFalse(responses.isEmpty());
        assertEquals(2, responses.size());
    }

    @Test
    void getVideoStatistics_ReturnsStatistics() {
        // Arrange
        GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
        request.setVideoId(1L);

        when(videoRepository.findById(request.getVideoId())).thenReturn(Optional.of(mockVideo));

        // Act
        VideoStatisticsResponse response = videoService.getVideoStatistics(request);

        // Assert
        assertNotNull(response);
        assertEquals(125000L,response.getTotalViews());
        assertEquals(23000L, response.getTotalFavorites());
        assertEquals(10L,response.getAverageDuration());
    }

    @Test
    void getVideosByDurationCategories_ReturnsCategories() {
        // Arrange
        Map<String, Long> expectedCategories = new HashMap<>();
        expectedCategories.put("SHORT",3L);
        expectedCategories.put("MEDIUM", 10L);
        expectedCategories.put("LONG", 20L);

        when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

        // Act
        Map<String, Long> result = videoService.getVideosByDurationCategories();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedCategories, result);
    }

    @Test
    void isVideoAvailableById_ReturnsTrue_WhenVideoExists() {
        // Arrange
        IsVideoAvailableById request = new IsVideoAvailableById();
        request.setVideoId(1L);

        when(videoRepository.existsById(request.getVideoId())).thenReturn(true);

        // Act
        boolean result = videoService.isVideoAvailableById(request);

        // Assert
        assertTrue(result);
    }

}
