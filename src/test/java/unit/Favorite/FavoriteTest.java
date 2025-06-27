package unit.Favorite;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.google.api.services.youtube.YouTube;
import org.developers.api.request.Favorite.*;
import org.developers.api.response.Favorite.FavoriteVideoResponse;
import org.developers.api.response.Favorite.PaginatedVideoFavoritesResponse;
import org.developers.model.entities.Favorite;
import org.developers.model.entities.User;
import org.developers.model.entities.Video;
import org.developers.model.mapper.FavoriteMapper;
import org.developers.repository.FavoriteRepository;
import org.developers.service.impl.FavoriteServiceImpl;
import org.developers.service.interfaces.UserService;
import org.developers.service.interfaces.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private UserService userService;

    @Mock
    private VideoService videoService;

    @Mock
    private YouTube youtube;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;
    private ListAppender<ILoggingEvent> logAppender;

    private Favorite mockFavorite;
    private User mockUser;
    private Video mockVideo;
    private FavoriteVideoResponse mockFavoriteVideoResponse;

    @BeforeEach
    void setUp() {
        // Configurar el capturador de logs
        Logger logger = (Logger) LoggerFactory.getLogger(FavoriteServiceImpl.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        ((ch.qos.logback.classic.Logger) logger).addAppender(logAppender);

        // Initialize mock entities with random values
        mockUser = User.builder()
                .userid(1000L)
                .username("JohnDoe")
                .email("johndoe@example.com")
                .createdAt(LocalDateTime.of(2022, 1, 15, 10, 30))
                .build();

        mockVideo = Video.builder()
                .videoId(5005L)
                .title("Amazing Video Title")
                .thumbnailUrl("http://example.com/amazing-video.mp4")
                .description("Amazing Video Description")
                .duration("10")
                .addedAt(LocalDateTime.of(2022, 1, 15, 10, 30))
                .build();

        mockFavorite = Favorite.builder()
                .favoriteId(1010L)
                .userId(mockUser.getUserid())
                .videoId(mockVideo.getVideoId())
                .title(mockVideo.getTitle())
                .thumbnailUrl("http://example.com/amazing-video.mp4")
                .addedAt(LocalDateTime.of(2022, 1, 15, 10, 30))
                .build();

        mockFavoriteVideoResponse = new FavoriteVideoResponse();
        mockFavoriteVideoResponse.setTitle(mockVideo.getTitle());
        mockFavoriteVideoResponse.setPublishDate(mockVideo.getAddedAt());

    }

    @Test
    void getUserFavoriteVideos_DeberiaRetornarListaDeVideos() {
        // Arrange
        GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
        request.setUserId(1000L);
        when(favoriteRepository.findByUserId(request.getUserId())).thenReturn(List.of(mockFavorite));

        // Act
        List<FavoriteVideoResponse> result = favoriteService.getUserFavoriteVideos(request);

        // Assert
        assertFalse(result.isEmpty());
        verify(favoriteRepository).findByUserId(request.getUserId());
        assertFalse(logAppender.list.isEmpty());
        assertTrue(logAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("Obteniendo videos favoritos")));
    }

    @Test
    void hasUserFavoritedVideo_DeberiaRetornarTrue_CuandoExisteFavorito() {
        // Arrange
        HasUserFavoritedVideo request = new HasUserFavoritedVideo();
        request.setUserId(1000L);
        request.setVideoId(5005L);
        when(favoriteRepository.existsByUserIdAndVideoId(request.getUserId(),request.getVideoId())).thenReturn(true);

        // Act
        boolean result = favoriteService.hasUserFavoritedVideo(request);

        // Assert
        assertTrue(result);
        verify(favoriteRepository).existsByUserIdAndVideoId(request.getUserId(),request.getVideoId());
    }

    @Test
    void getUserFavoritesPaginated_DeberiaRetornarResultadosPaginados() {
        // Arrange
        GetUserFavoritesPaginated request = GetUserFavoritesPaginated.builder()
                .userId(1000L)
                .page(1)
                .size(2)
                .build();
        when(favoriteRepository.findRecentFavoritesByUserId(request.getUserId())).thenReturn(List.of(mockFavorite));

        // Act
        PaginatedVideoFavoritesResponse response = favoriteService.getUserFavoritesPaginated(request);

        // Assert
        verify(favoriteRepository).findRecentFavoritesByUserId(request.getUserId());
        assertFalse(response.getFavorites().isEmpty());
    }

    @Test
    void addVideoToFavorites_DeberiaAgregarVideoCorrectamente() {
        // Arrange
        AddVideoToFavorites request = new AddVideoToFavorites();
        request.setUserId(1000L);
        request.setVideoId(5005L);

        when(favoriteRepository.save(mockFavorite)).thenReturn(mockFavorite);

        // Act
        favoriteService.addVideoToFavorites(request);

        // Assert
        verify(favoriteRepository).save(mockFavorite);
        assertTrue(logAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("Video añadido a favoritos")));
    }

    @Test
    void removeVideoFromFavorites_DeberiaEliminarVideoCorrectamente() {
        // Arrange
        RemoveVideoFromFavorites request = new RemoveVideoFromFavorites();
        request.setUserId(1000L);
        request.setVideoId(5005L);
        when(favoriteRepository.findByUserIdAndVideoId(request.getUserId(),request.getVideoId())).thenReturn(Optional.of(mockFavorite));

        // Act
        favoriteService.removeVideoFromFavorites(request);

        // Assert
        verify(favoriteRepository).delete(mockFavorite);
        assertTrue(logAppender.list.stream()
                .anyMatch(event -> event.getMessage().contains("Video eliminado de favoritos")));
    }

    @Test
    void getRecentlyFavoritedVideos_DeberiaRetornarVideosRecientes() {
        // Arrange
        GetRecentlyFavoritedVideos request = new GetRecentlyFavoritedVideos();
        request.setUserId(1000L);
        when(favoriteRepository.findRecentFavoritesByUserId(request.getUserId())).thenReturn(List.of(mockFavorite));

        // Act
        List<FavoriteVideoResponse> result = favoriteService.getRecentlyFavoritedVideos(request);

        // Assert
        verify(favoriteRepository).findRecentFavoritesByUserId(request.getUserId());
    }

    @Test
    void getTrendingFavoriteVideos_DeberiaRetornarVideosTendencia() {
        // Arrange
        GetTrendingFavoriteVideos request = new GetTrendingFavoriteVideos();
        request.setUserId(1000L);
        LocalDateTime nowMinusOneDay = LocalDateTime.now().minusDays(1);
        when(favoriteRepository.findTrendingFavorites(request.getUserId(),nowMinusOneDay)).thenReturn(List.of(mockFavorite));

        // Act
        List<FavoriteVideoResponse> result = favoriteService.getTrendingFavoriteVideos(request);

        // Assert
        verify(favoriteRepository).findTrendingVideos(nowMinusOneDay, Pageable.ofSize(1));
    }

}
