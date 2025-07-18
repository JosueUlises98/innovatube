package unit.Favorite.service;



import org.developers.api.request.Favorite.*;
import org.developers.common.exception.exceptions.*;
import org.developers.model.dto.FavoriteVideo;
import org.developers.model.entities.Favorite;
import org.developers.model.entities.User;
import org.developers.model.entities.Video;
import org.developers.model.mapper.FavoriteMapper;
import org.developers.repository.FavoriteRepository;
import org.developers.service.impl.FavoriteServiceImpl;
import org.developers.service.impl.UserServiceImpl;
import org.developers.service.impl.VideoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteTest {
/*
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private FavoriteMapper favoriteMapper;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private VideoServiceImpl videoService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Favorite mockFavorite;
    private User mockUser;
    private Video mockVideo;
    private FavoriteVideoResponse mockFavoriteVideoResponse;
    private FavoriteVideo mockFavoriteVideo;

    @BeforeEach
    void setUp() throws IOException {
        //Configurar los mocks
        initDataMock();
        // Configurar el mock de YouTube
        //Para algunos metodos de esta clase de Test se requieren simular los comportamientos siguientes:
        //En caso de que el resultado de ejecucion del test sea fallido, desmarcar como comentarios.
        ///when(youtube.videos()).thenReturn(mock(YouTube.Videos.class));
        ///when(youtube.videos().rate(anyString(), anyString())).thenReturn(rate);
    }

    @Nested
    @DisplayName("GetUserFavoriteVideos Tests" )
    class GetUserFavoriteVideosTest {

        @Test
        void deberiaLanzarExcepcionCuandoNoHayFavoritos() {
            // Arrange
            GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
            request.setUserId(1000L);
            when(favoriteRepository.findByUserId(request.getUserId())).thenReturn(Collections.emptyList());

            // Act - Assert
            try{
                favoriteService.getUserFavoriteVideos(request);
            }catch(UserFavoriteVideosException e){
                System.out.println(e.getMessage());
            }
        }

        @Test
        void deberiaRetornarListaConMultiplesFavoritos() {
            // Arrange
            GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
            request.setUserId(1000L);
            when(favoriteRepository.findByUserId(request.getUserId()))
                    .thenReturn(List.of(mockFavoriteVideo,mockFavoriteVideo));
            when(favoriteMapper.toResponseList(List.of(mockFavoriteVideo,mockFavoriteVideo))).thenReturn(List.of(mockFavoriteVideoResponse,mockFavoriteVideoResponse));

            // Act
            List<FavoriteVideoResponse> result = favoriteService.getUserFavoriteVideos(request);
            result.forEach(System.out::println);

            // Assert
            assertEquals(2, result.size());
            verify(favoriteRepository).findByUserId(request.getUserId());
        }

        @Test
        void deberiaArrojarExcepcionCuandoNoHayUsuarioConId() {
            // Arrange
            GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
            request.setUserId(9999L);
            when(favoriteRepository.findByUserId(request.getUserId())).thenReturn(Collections.emptyList());

            // Act & Assert
            try{
                favoriteService.getUserFavoriteVideos(request);
            }catch(UserFavoriteVideosException e){
                System.out.println(e.getMessage());
            }
            verify(favoriteRepository).findByUserId(request.getUserId());
        }

        @Test
        void deberiaLanzarExcepcionConRequestNulo() {
            // Act & Assert
            try{
                favoriteService.getUserFavoriteVideos(null);
            }catch (NullPointerException e){
                System.out.println(e.getMessage());
            }
        }
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

    @Nested
    @DisplayName("GetUserFavoritesPaginated Tests" )
    class GetUserFavoritesPaginatedTest {
        @Test
        void getUserFavoritesPaginated_DeberiaRetornarResultadosPaginados() {
            // Arrange
            GetUserFavoritesPaginated request = GetUserFavoritesPaginated.builder()
                    .userId(1000L)
                    .page(0)
                    .size(2)
                    .build();
            Page<FavoriteVideo>page = new PageImpl<>(List.of(mockFavoriteVideo,mockFavoriteVideo), PageRequest.of(0, 2), 2);
            when(favoriteRepository.findByUserId(request.getUserId(), Pageable.ofSize(request.getSize()).withPage(request.getPage()))).thenReturn(page);
            when(favoriteMapper.toResponseList(page.getContent())).thenReturn(List.of(mockFavoriteVideoResponse,mockFavoriteVideoResponse));

            // Act
            PaginatedVideoFavoritesResponse response = favoriteService.getUserFavoritesPaginated(request);
            response.getFavorites().forEach(System.out::println);

            // Assert
            verify(favoriteRepository).findByUserId(request.getUserId(), Pageable.ofSize(request.getSize()).withPage(request.getPage()));
            verify(favoriteMapper).toResponseList(page.getContent());
            assertFalse(response.getFavorites().isEmpty());
            assertEquals(2, response.getFavorites().size());
        }
        @Test
        void getUserFavoritesPaginated_DeberiaLanzarExcepcion_CuandoNoHayFavoritos() {
            //Arrange
            GetUserFavoritesPaginated request = GetUserFavoritesPaginated.builder()
                    .userId(1000L)
                    .page(0)
                    .size(2)
                    .build();
            Page<FavoriteVideo>page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 2), 0);
            when(favoriteRepository.findByUserId(request.getUserId(), Pageable.ofSize(request.getSize()).withPage(request.getPage()))).thenReturn(page);
            //Act
            try{
                favoriteService.getUserFavoritesPaginated(request);
            }catch(UserFavoritesPaginatedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("AddVideoToFavorites Tests")
    class AddVideoToFavoritesTest {

        @Test
        @DisplayName("Debería agregar video a favoritos correctamente")
        void addVideoToFavorites_DeberiaAgregarVideoCorrectamente() {
            // Arrange
            Long userId = 1000L;
            Long videoId = 5005L;
            AddVideoToFavorites request = new AddVideoToFavorites();
            request.setUserId(userId);
            request.setVideoId(videoId);

            IsUserNameAvaibleRequest avaibleRequest = new IsUserNameAvaibleRequest();
            avaibleRequest.setUserId(userId);
            when(userService.isUserNameAvailableById(avaibleRequest)).thenReturn(true);

            IsVideoAvailableById videoAvailableRequest = new IsVideoAvailableById();
            videoAvailableRequest.setVideoId(videoId);
            when(videoService.isVideoAvailableById(videoAvailableRequest)).thenReturn(true);

            HasUserFavoritedVideo hasUserFavoritedVideoRequest = new HasUserFavoritedVideo();
            hasUserFavoritedVideoRequest.setUserId(userId);
            hasUserFavoritedVideoRequest.setVideoId(videoId);
            when(favoriteService.hasUserFavoritedVideo(hasUserFavoritedVideoRequest)).thenReturn(false);
            when(favoriteRepository.findYoutubeVideoIdsByUserId(userId)).thenReturn(List.of("5005L"));
            when(favoriteRepository.save(any(Favorite.class))).thenReturn(mockFavorite);

            // Act & Assert
            assertDoesNotThrow(() -> favoriteService.addVideoToFavorites(request));
            verify(favoriteRepository).save(any(Favorite.class));
        }

        @Test
        @DisplayName("Debería lanzar InvalidArgumentException cuando el request es nulo")
        void addVideoToFavorites_DeberiaLanzarExcepcion_CuandoRequestEsNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class,
                    () -> favoriteService.addVideoToFavorites(null));
        }

        @Test
        @DisplayName("Debería lanzar excepcion cuando el usuario no esta disponible")
        void addVideoToFavorites_DeberiaLanzarExcepcion_CuandoUsuarioNoActivo() {
            // Arrange
            Long userId = 1000L;
            AddVideoToFavorites request = new AddVideoToFavorites();
            request.setUserId(userId);

            IsUserNameAvaibleRequest avaibleRequest = new IsUserNameAvaibleRequest();
            avaibleRequest.setUserId(userId);
            when(userService.isUserNameAvailableById(avaibleRequest)).thenReturn(false);

            // Act & Assert
            try{
                favoriteService.addVideoToFavorites(request);
            }catch (UserNotFoundException e){
                System.out.println(e.getMessage());
            }
        }

        @Test
        @DisplayName("Debería lanzar VideoNotFoundException cuando el video no esta disponible")
        void addVideoToFavorites_DeberiaLanzarExcepcion_CuandoVideoNoExiste() {
            // Arrange
            Long userId = 1000L;
            Long videoId = 5005L;
            AddVideoToFavorites request = new AddVideoToFavorites();
            request.setUserId(userId);
            request.setVideoId(videoId);

            IsUserNameAvaibleRequest avaibleRequest = new IsUserNameAvaibleRequest();
            avaibleRequest.setUserId(userId);

            IsVideoAvailableById videoAvailableRequest = new IsVideoAvailableById();
            videoAvailableRequest.setVideoId(videoId);
            when(videoService.isVideoAvailableById(videoAvailableRequest)).thenReturn(false);
            when(userService.isUserNameAvailableById(avaibleRequest)).thenReturn(true);

            // Act & Assert
           try{
               favoriteService.addVideoToFavorites(request);
           }catch (VideoNotFoundException e){
               System.out.println(e.getMessage());
           }
        }

        @Test
        @DisplayName("Debería lanzar excepcion cuando el video ya está en favoritos")
        void addVideoToFavorites_DeberiaLanzarExcepcion_CuandoVideoYaEstaEnFavoritos() {
            // Arrange
            Long userId = 1000L;
            Long videoId = 5005L;
            AddVideoToFavorites request = new AddVideoToFavorites();
            request.setUserId(userId);
            request.setVideoId(videoId);
            IsUserNameAvaibleRequest useravaibleRequest = new IsUserNameAvaibleRequest();
            useravaibleRequest.setUserId(userId);
            IsVideoAvailableById videoAvailableRequest = new IsVideoAvailableById();
            videoAvailableRequest.setVideoId(videoId);
            HasUserFavoritedVideo hasUserFavoritedVideoRequest = new HasUserFavoritedVideo();
            hasUserFavoritedVideoRequest.setUserId(userId);
            hasUserFavoritedVideoRequest.setVideoId(videoId);
            when(userService.isUserNameAvailableById(useravaibleRequest)).thenReturn(true);
            when(videoService.isVideoAvailableById(videoAvailableRequest)).thenReturn(true);
            when(favoriteService.hasUserFavoritedVideo(hasUserFavoritedVideoRequest)).thenReturn(true);

            // Act & Assert
            try{
                favoriteService.addVideoToFavorites(request);
            }catch (FavoritedVideoExistsException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("RemoveVideoFromFavorites Tests")
    class RemoveVideoFromFavoritesTest{
        @Test
        void removeVideoFromFavorites_DeberiaEliminarVideoDeFavoritos() {
            //Arrange
            Long userId = 1000L;
            Long videoId = 5005L;
            RemoveVideoFromFavorites removeRequest = new RemoveVideoFromFavorites();
            removeRequest.setUserId(userId);
            removeRequest.setVideoId(videoId);
            when(favoriteRepository.existsByUserIdAndVideoId(userId,videoId)).thenReturn(true);
            when(favoriteRepository.findByUserIdAndVideoId(removeRequest.getUserId(),removeRequest.getVideoId())).thenReturn(Optional.of(mockFavoriteVideo));
            when(favoriteRepository.findYoutubeVideoIdsByUserId(userId)).thenReturn(List.of("5005L"));
            //Act
            favoriteService.removeVideoFromFavorites(removeRequest);
            //Assert
            verify(favoriteRepository).delete(mockFavorite);
            verify(favoriteRepository).existsByUserIdAndVideoId(userId,videoId);
            verify(favoriteRepository).findByUserIdAndVideoId(removeRequest.getUserId(),removeRequest.getVideoId());
            verify(favoriteRepository).findYoutubeVideoIdsByUserId(userId);
        }

        @Test
        void removeVideoFromFavoritesWithInvalidData_DeberiaLanzarExcepcion() {
            // Arrange
            Long userId = 1000L;
            Long videoId = 9999L; // Invalid video ID
            RemoveVideoFromFavorites removeRequest = new RemoveVideoFromFavorites();
            removeRequest.setUserId(userId);
            removeRequest.setVideoId(videoId);
            when(favoriteRepository.existsByUserIdAndVideoId(userId, videoId)).thenReturn(true);
            when(favoriteRepository.findByUserIdAndVideoId(removeRequest.getUserId(), removeRequest.getVideoId())).thenReturn(Optional.empty());

            // Act & Assert
            try {
                favoriteService.removeVideoFromFavorites(removeRequest);
            } catch (RemoveFailedException e) {
                System.out.println(e.getMessage());
            }
            verify(favoriteRepository).existsByUserIdAndVideoId(userId, videoId);
            verify(favoriteRepository).findByUserIdAndVideoId(removeRequest.getUserId(), removeRequest.getVideoId());
            verify(favoriteRepository, never()).delete(any(Favorite.class));
            verify(favoriteRepository, never()).findYoutubeVideoIdsByUserId(userId);
        }
    }

    private void initDataMock(){
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
                .duration(Duration.parse("10"))
                .addedAt(LocalDateTime.of(2025,6,30,16,0).minusHours(1L))
                .build();

        mockFavorite = Favorite.builder()
                .favoriteId(1010L)
                .userId(mockUser.getUserid())
                .videoId(mockVideo.getVideoId())
                .addedAt(mockVideo.getAddedAt())
                .build();

        mockFavoriteVideo = new FavoriteVideo(mockFavorite.getUserId(),
                mockFavorite.getVideoId(),
                mockVideo.getAddedAt(),
                mockVideo.getTitle(),
                mockVideo.getYoutubeVideoId(),
                mockVideo.getDescription(),
                mockVideo.getThumbnailUrl(),
                mockVideo.getDuration(),
                null,null);

        BeanUtils.copyProperties(mockFavoriteVideo,mockFavoriteVideoResponse);
    }

 */
}
