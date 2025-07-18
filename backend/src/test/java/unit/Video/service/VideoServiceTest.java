package unit.Video.service;


import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.common.exception.exceptions.*;
import org.developers.common.utils.datetime.DateTimeUtils;
import org.developers.common.utils.quota.YouTubeQuotaUtil;
import org.developers.model.entities.Video;
import org.developers.model.mapper.VideoMapper;
import org.developers.repository.VideoRepository;
import org.developers.service.impl.VideoServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    /*
        @Mock
    private VideoRepository videoRepository;
    @Mock
    private VideoMapper videoMapper;
    @Mock
    private YouTube youtube;
    @Mock
    private Logger logger;
    @Mock
    private YouTube.Videos videos;
    @Mock
    private YouTube.Videos.List videoList;

    @InjectMocks
    private VideoServiceImpl videoService;

    private LocalDateTime now;
    private Video mockVideo;
    private VideoResponse mockVideoResponse;
    private List<Video> mockVideos;
    private List<VideoResponse> mockVideoResponses;

    private static MockedStatic<YouTubeQuotaUtil> mockedYouTubeQuotaUtil;

    @BeforeAll
    static void beforeAll() {
        mockedYouTubeQuotaUtil = mockStatic(YouTubeQuotaUtil.class);
    }

    @AfterAll
    static void afterAll() {
        if (mockedYouTubeQuotaUtil != null) {
            mockedYouTubeQuotaUtil.close();
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        now = LocalDateTime.now();
        logger = LoggerFactory.getLogger(VideoServiceImpl.class);

        // Configuración de los mocks existentes
        setupMockData();

        //Configuracion de YouTube API

        when(videos.list(anyList())).thenReturn(videoList);
        when(videoList.execute()).thenReturn(new VideoListResponse());
        when(videoList.setId(anyList())).thenReturn(videoList);

        // Configuración por defecto de YouTubeQuotaUtil
        mockedYouTubeQuotaUtil.when(YouTubeQuotaUtil::checkQuotaAvailable).thenReturn(true);
    }

    @Nested
    @DisplayName("CreateVideo Tests")
    class CreateVideoClass {

        @Test
        @DisplayName("Debería crear video exitosamente cuando hay cuota disponible")
        void createVideo_DeberiaCrearVideoExitosamente() throws IOException {
            // Arrange
            String youtubeId = "video-proof-10";
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId(youtubeId);

            // Configurar comportamiento de YouTubeQuotaUtil
            mockedYouTubeQuotaUtil.when(YouTubeQuotaUtil::checkQuotaAvailable).thenReturn(true);

            // Configurar YouTube API mocks
            when(youtube.videos()).thenReturn(videos);
            // Corregir la configuración del mock para videos.list()
            when(videos.list(List.of("snippet", "contentDetails", "statistics"))).thenReturn(videoList);
            when(videoList.setId(Collections.singletonList(youtubeId))).thenReturn(videoList);

            // Configurar respuesta de YouTube API
            VideoListResponse youtubeResponse = new VideoListResponse();
            com.google.api.services.youtube.model.Video youtubeVideo = crearYoutubeVideoMock();
            youtubeResponse.setItems(Collections.singletonList(youtubeVideo));
            when(videoList.execute()).thenReturn(youtubeResponse);

            // Configurar comportamiento del repository y mapper
            when(videoMapper.toEntity(any())).thenReturn(mockVideo);
            when(videoRepository.save(mockVideo)).thenReturn(mockVideo);
            when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

            // Act
            VideoResponse result = videoService.createVideo(request);
            System.out.println(result.toString());

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(mockVideoResponse.getYoutubeVideoId(), result.getYoutubeVideoId()),
                    () -> assertEquals(mockVideoResponse.getTitle(), result.getTitle()),
                    () -> assertEquals(mockVideoResponse.getDescription(), result.getDescription()),
                    () -> assertEquals(mockVideoResponse.getThumbnailUrl(), result.getThumbnailUrl()),
                    () -> assertEquals(mockVideoResponse.getDuration(), result.getDuration()),
                    () -> assertEquals(mockVideoResponse.getViewCount(), result.getViewCount()),
                    () -> assertEquals(mockVideoResponse.getLikeCount(), result.getLikeCount())
            );

            // Verificar interacciones
            verify(youtube).videos();
            // Corregir la verificación para que coincida con la llamada real
            verify(videos).list(List.of("snippet", "contentDetails", "statistics"));
            verify(videoList).setId(Collections.singletonList(youtubeId));
            verify(videoList).execute();
            verify(videoMapper).toEntity(any());
            verify(videoRepository).save(any(Video.class));
            verify(videoMapper).toVideoResponse(any(Video.class));

            mockedYouTubeQuotaUtil.verify(YouTubeQuotaUtil::checkQuotaAvailable);
            mockedYouTubeQuotaUtil.verify(() -> YouTubeQuotaUtil.incrementQuota(1));

        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no hay cuota disponible")
        void createVideo_CuandoNoHayCuotaDisponible_DebeLanzarExcepcion() throws IOException {
            // Arrange
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId("video-proof-1");
            mockedYouTubeQuotaUtil.when(YouTubeQuotaUtil::checkQuotaAvailable).thenReturn(false);

            try{
                videoService.createVideo(request);
            }catch (CouldNotCreatedVideoException e){
                System.out.println(e.getMessage());
            }

            assertAll(
                    () -> {
                        mockedYouTubeQuotaUtil.verify(YouTubeQuotaUtil::checkQuotaAvailable);
                        verify(videoList, never()).execute();
                        verify(videoRepository, never()).save(mockVideo);
                        verify(videoMapper, never()).toEntity(mockVideoResponse);
                        verify(videoMapper, never()).toVideoResponse(mockVideo);
                        assertThrows(CouldNotCreatedVideoException.class,() -> videoService.createVideo(request));
                    }
            );
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el video no existe en YouTube")
        void createVideo_CuandoVideoNoExiste_DebeLanzarExcepcion() throws IOException {
            // Arrange
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId("video-proof-1");

            // Mock para la verificación de cuota
            mockedYouTubeQuotaUtil.when(YouTubeQuotaUtil::checkQuotaAvailable).thenReturn(true);

            when(youtube.videos()).thenReturn(videos);
            when(videos.list(List.of("snippet", "contentDetails", "statistics"))).thenReturn(videoList);
            when(videoList.setId(any())).thenReturn(videoList);

            // Configurar la respuesta vacía que provocará la excepción
            VideoListResponse emptyResponse = new VideoListResponse();
            List<com.google.api.services.youtube.model.Video> emptyList = new ArrayList<>();
            emptyResponse.setItems(emptyList); // Esto es importante
            when(videoList.execute()).thenReturn(emptyResponse);

            // Act & Assert
            try{
                videoService.createVideo(request);
            }catch(VideoNotFoundException e){
                System.out.println(e.getMessage());
            }

            // Verificaciones
            verify(youtube).videos();
            verify(videos).list(List.of("snippet", "contentDetails", "statistics"));
            verify(videoList).setId(Collections.singletonList(request.getYoutubeId()));
            verify(videoRepository, never()).save(any());
            verify(videoMapper, never()).toEntity(any());
            verify(videoMapper, never()).toVideoResponse(any());

        }

        @Test
        @DisplayName("Debería lanzar excepción cuando hay error en la API de YouTube")
        void createVideo_CuandoHayErrorDeYoutube_DebeLanzarExcepcion() throws IOException {
            // Arrange
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId("video-proof-1");

            // Configurar todos los mocks necesarios
            mockedYouTubeQuotaUtil.when(YouTubeQuotaUtil::checkQuotaAvailable).thenReturn(true);
            when(youtube.videos()).thenReturn(videos);
            when(videos.list(List.of("snippet", "contentDetails", "statistics"))).thenReturn(videoList);
            when(videoList.setId(Collections.singletonList(request.getYoutubeId()))).thenReturn(videoList);
            when(videoList.execute()).thenThrow(new IOException("Error de API"));

            // Act & Assert
            CouldNotCreatedVideoException exception = assertThrows(CouldNotCreatedVideoException.class,
                    () -> videoService.createVideo(request));

            assertAll(
                    () -> {
                        mockedYouTubeQuotaUtil.verify(YouTubeQuotaUtil::checkQuotaAvailable);
                        verify(youtube).videos();
                        verify(videos).list(List.of("snippet", "contentDetails", "statistics"));
                        verify(videoList).setId(Collections.singletonList(request.getYoutubeId()));
                        verify(videoList).execute();
                        verify(videoRepository, never()).save(any(Video.class));
                        verify(videoMapper, never()).toEntity(any(VideoResponse.class));
                        verify(videoMapper, never()).toVideoResponse(any(Video.class));
                    }
            );
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el video ya existe en la base de datos")
        void createVideo_CuandoVideoYaExiste_DebeLanzarExcepcion() throws IOException {
            // Arrange
            String youtubeId = "video-proof-1";
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId(youtubeId);

            try{
                videoService.createVideo(request);
            }catch(VideoAlreadyExistsException e){
                System.out.println(e.getMessage());
            }
            assertAll(
                    () -> {
                        verify(videoList, never()).execute();
                        verify(videoRepository, never()).save(mockVideo);
                    }
            );
        }
    }

    @Nested
    @DisplayName("Pruebas de Búsqueda de Videos por Título")
    class SearchVideosByTitleClass {

        @Test
        @DisplayName("Debería retornar videos cuando hay coincidencias exactas")
        void searchVideosByTitle_ReturnsMatchingVideos() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle(mockVideo.getTitle());
            List<Video> videos = Collections.singletonList(mockVideo);
            List<VideoResponse> expectedResponses = Collections.singletonList(mockVideoResponse);

            when(videoMapper.toVideoResponse(mockVideo)).thenReturn(expectedResponses.getFirst());

            // Act
            List<VideoResponse> responses = videoService.searchVideosByTitle(request);

            // Assert
            assertFalse(responses.isEmpty());
            assertEquals(1, responses.size());
            assertEquals(expectedResponses.getFirst(), responses.getFirst());
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay coincidencias")
        void searchVideosByTitle_WhenNoMatches_ReturnsEmptyList() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle("título inexistente");


            // Act
            List<VideoResponse> responses = videoService.searchVideosByTitle(request);

            // Assert
            assertTrue(responses.isEmpty());
        }

        @Test
        @DisplayName("Debería retornar excepcion con título vacío")
        void searchVideosByTitle_WithEmptyTitle_ReturnsEmptyList() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle("");

            // Act
            try {
                videoService.searchVideosByTitle(request);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        @DisplayName("Debería lanzar excepcion con caracteres especiales")
        void searchVideosByTitle_WithSpecialCharacters_ReturnsMatches() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle("título!@#$%");
            // Act
            try{
                videoService.searchVideosByTitle(request);
            }catch (InvalidCriteriaException e){
                System.out.println(e.getMessage());
            }
        }

        @Test
        @DisplayName("Debería manejar múltiples coincidencias correctamente")
        void searchVideosByTitle_WithMultipleMatches_ReturnsAllMatches() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle("motos");
            mockVideo.setTitle(request.getTitle());
            List<Video> videos = Arrays.asList(mockVideo, mockVideo);


            when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

            // Act
            List<VideoResponse> responses = videoService.searchVideosByTitle(request);

            // Assert
            assertEquals(2, responses.size());
            verify(videoMapper, times(2)).toVideoResponse(mockVideo);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el request es null")
        void searchVideosByTitle_WithNullRequest_ThrowsException() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                videoService.searchVideosByTitle(null);
            });
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el título es null")
        void searchVideosByTitle_WithNullTitle_ThrowsException() {
            // Arrange
            SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
            request.setTitle(null);

            // Act & Assert
            assertThrows(NullCriteriaException.class, () -> {
                videoService.searchVideosByTitle(request);
            });
        }
    }

    @Nested
    @DisplayName("Pruebas de Obtención de Videos más Recientes")
    class GetLastestVideosClass {

        @Test
        @DisplayName("Debería retornar resultados paginados correctamente")
        void getLastestVideos_DeberiaRetornarResultadosPaginados() {
            // Arrange
            GetLastestVideosRequest request = new GetLastestVideosRequest();
            request.setPage(1);
            request.setSize(3);

            List<Video> videos = List.of(mockVideo, mockVideo, mockVideo);
            Page<Video> videoPage = new PageImpl<>(videos);
            when(videoRepository.findAll(any(Pageable.class))).thenReturn(videoPage);
            when(videoMapper.toVideoResponse(any(Video.class))).thenReturn(mockVideoResponse);

            // Act
            PaginatedResponse<VideoResponse> response = videoService.getLastestVideos(request);

            // Assert
            assertNotNull(response);
            assertEquals(3, response.getContent().size());
            verify(videoRepository).findAll(any(Pageable.class));
            verify(videoMapper, times(3)).toVideoResponse(any(Video.class));
        }

        //TODO:Terminar de testear desde este metodo hasta el final de esta clase
        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay videos")
        void getLastestVideos_CuandoNoHayVideos_DeberiaRetornarListaVacia() {
            // Arrange
            GetLastestVideosRequest request = new GetLastestVideosRequest();
            request.setPage(1);
            request.setSize(10);

            Page<Video> emptyPage = new PageImpl<>(Collections.emptyList());
            when(videoRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

            // Act
            PaginatedResponse<VideoResponse> response = videoService.getLastestVideos(request);

            // Assert
            assertNotNull(response);
            assertTrue(response.getContent().isEmpty());
            assertEquals(0, response.getTotalElements());
        }

        @Test
        @DisplayName("Debería lanzar excepcion con paginacion negativa")
        void getLastestVideos_ConParametrosInvalidos_DeberiaUsarValoresPorDefecto() {
            // Arrange
            GetLastestVideosRequest request = new GetLastestVideosRequest();
            request.setPage(-1);
            request.setSize(0);

            Page<Video> videoPage = new PageImpl<>(List.of(mockVideo));

            // Act-Assert
            try {
                videoService.getLastestVideos(request);
            }catch (InvalidPaginationException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Pruebas de Obtención de Videos Tendencia")
    class GetTrendingVideosClass {

        @Test
        @DisplayName("Debería retornar videos tendencia cuando existen videos que cumplen el criterio")
        void getTrendingVideos_CuandoExistenVideosTendencia_DebeRetornarVideos() {
            // Arrange
            GetTrendingVideosRequest request = new GetTrendingVideosRequest();
            request.setMinimumViews(1000);

            List<Video> trendingVideos = List.of(mockVideo, mockVideo);
            when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

            // Act
            List<VideoResponse> response = videoService.getTrendingVideos(request);

            // Assert
            assertNotNull(response);
            assertEquals(2, response.size());
            verify(videoMapper, times(2)).toVideoResponse(any(Video.class));
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay videos tendencia")
        void getTrendingVideos_CuandoNoHayVideosTendencia_DebeRetornarListaVacia() {
            // Arrange
            GetTrendingVideosRequest request = new GetTrendingVideosRequest();
            request.setMinimumViews(10000);


            // Act
            List<VideoResponse> response = videoService.getTrendingVideos(request);

            // Assert
            assertNotNull(response);
            assertTrue(response.isEmpty());
            verify(videoMapper, never()).toVideoResponse(any(Video.class));
        }

        @Test
        @DisplayName("Debería manejar correctamente cuando minimumViews es cero")
        void getTrendingVideos_CuandoMinimumViewsEsCero_DebeRetornarVideos() {
            // Arrange
            GetTrendingVideosRequest request = new GetTrendingVideosRequest();
            request.setMinimumViews(0);

            List<Video> trendingVideos = Collections.singletonList(mockVideo);
            when(videoMapper.toVideoResponse(mockVideo)).thenReturn(mockVideoResponse);

            // Act
            List<VideoResponse> response = videoService.getTrendingVideos(request);

            // Assert
            assertNotNull(response);
            assertFalse(response.isEmpty());
            assertEquals(1, response.size());
            verify(videoMapper).toVideoResponse(any(Video.class));
        }

        @Test
        @DisplayName("Debería lanzar excepcion cuando minimumViews es negativo")
        void getTrendingVideos_CuandoMinimumViewsEsNegativo_DebeRetornarVideos() {
            // Arrange
            GetTrendingVideosRequest request = new GetTrendingVideosRequest();
            request.setMinimumViews(-100);

            // Act
            try{
                videoService.getTrendingVideos(request);
            }catch (InvalidCriteriaException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Pruebas de Obtención de Estadísticas de Video")
    class GetVideoStatisticsClass {

        @Test
        @DisplayName("Debería retornar estadísticas cuando el video existe")
        void getVideoStatistics_CuandoVideoExiste_DebeRetornarEstadisticas() {
            // Arrange
            GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
            request.setVideoId(1L);

            when(videoRepository.findById(request.getVideoId())).thenReturn(Optional.of(mockVideo));

            // Act
            VideoStatisticsResponse response = videoService.getVideoStatistics(request);

            // Assert
            assertNotNull(response);
            assertEquals(125000L, response.getTotalViews());
            assertEquals(0L, response.getTotalFavorites());
            assertEquals(600L, response.getAverageDuration());
            verify(videoRepository).findById(request.getVideoId());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el video no existe")
        void getVideoStatistics_CuandoVideoNoExiste_DebeLanzarExcepcion() {
            // Arrange
            GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
            request.setVideoId(999L);

            when(videoRepository.findById(request.getVideoId())).thenReturn(Optional.empty());

            // Act & Assert
            try{
                videoService.getVideoStatistics(request);
            }catch(ResourceNotFoundException e){
                System.out.println(e.getMessage());
            }
            verify(videoRepository).findById(request.getVideoId());
        }
    }

    @Nested
    @DisplayName(value = "GetVideosByDurationCategories Tests")
    class GetVideosByDurationCategories{
        @Nested
        @DisplayName("Pruebas de Categorización de Videos por Duración")
        class GetVideosByDurationCategoriesClass {

            @Test
            @DisplayName("Debería retornar categorías con distribución correcta cuando existen videos")
            void getVideosByDurationCategories_WithVideos_ReturnsCorrectDistribution() {
                // Arrange
                List<Video> mockVideoList = Arrays.asList(
                        createMockVideo("PT2M"),   // SHORT
                        createMockVideo("PT2M"),   // SHORT
                        createMockVideo("PT2M"),   // SHORT
                        createMockVideo("PT8M"),   // MEDIUM - x10
                        createMockVideo("PT20M")   // LONG - x20
                );

                when(videoRepository.findAll()).thenReturn(mockVideoList);

                Map<String, Long> expectedCategories = new HashMap<>();
                expectedCategories.put("CORTO", 3L);
                expectedCategories.put("MEDIO", 1L);
                expectedCategories.put("LARGO", 1L);

                // Act
                Map<String, Long> result = videoService.getVideosByDurationCategories();

                // Assert
                assertAll(
                        () -> assertFalse(result.isEmpty()),
                        () -> assertEquals(expectedCategories, result),
                        () -> verify(videoRepository).findAll()
                );
            }

            @Test
            @DisplayName("Debería retornar mapa vacío cuando no hay videos")
            void getVideosByDurationCategories_WithNoVideos_ReturnsEmptyMap() {
                // Arrange
                when(videoRepository.findAll()).thenReturn(Collections.emptyList());

                // Act
                Map<String, Long> result = videoService.getVideosByDurationCategories();

                // Assert
                assertAll(
                        () -> assertTrue(result.isEmpty()),
                        () -> verify(videoRepository).findAll(),
                        () -> verify(videoMapper, never()).toVideoResponse(any())
                );
            }

            private Video createMockVideo(String duration) {
                return Video.builder().duration(Duration.parse(duration)).build();
            }
        }
    }

    private com.google.api.services.youtube.model.Video crearYoutubeVideoMock() {
        com.google.api.services.youtube.model.Video video = new com.google.api.services.youtube.model.Video();

        // Configurar Snippet
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(mockVideo.getTitle());
        snippet.setDescription(mockVideo.getDescription());
        snippet.setPublishedAt(DateTimeUtils.toGoogleDateTime(mockVideo.getAddedAt()));

        // Configurar Thumbnails
        ThumbnailDetails thumbnails = new ThumbnailDetails();
        Thumbnail defaultThumbnail = new Thumbnail();
        defaultThumbnail.setUrl(mockVideo.getThumbnailUrl());
        thumbnails.setDefault(defaultThumbnail);
        snippet.setThumbnails(thumbnails);

        // Configurar ContentDetails
        VideoContentDetails contentDetails = new VideoContentDetails();
        contentDetails.setDuration(String.valueOf(mockVideo.getDuration()));

        // Configurar Statistics
        VideoStatistics statistics = new VideoStatistics();
        statistics.setViewCount(mockVideo.getViewCount());
        statistics.setLikeCount(mockVideo.getLikes());

        video.setSnippet(snippet);
        video.setContentDetails(contentDetails);
        video.setStatistics(statistics);

        return video;
    }

    private void setupMockData() {
        // Configuración de las entidades mock
        mockVideo = Video.builder()
                .videoId(1L)
                .youtubeVideoId("video-proof-10")
                .title("camiones en china")
                .description("camiones urbanos en piojang")
                .thumbnailUrl("https:localhost:8080")
                .duration(Duration.parse("PT10M"))
                .addedAt(now)
                .viewCount(BigInteger.valueOf(125000L))
                .likes(BigInteger.valueOf(23000L))
                .build();

        Video video1 = Video.builder()
                .videoId(2L)
                .youtubeVideoId("random-youtube-id")
                .title("Perros en el parque")
                .description("Perros jugando en el parque bajo la lluvia")
                .thumbnailUrl("https://example.com/thumbnail2")
                .duration(Duration.parse("15"))
                .addedAt(now.minusDays(5))
                .viewCount(BigInteger.valueOf(5000L))
                .likes(BigInteger.valueOf(600L))
                .build();

        Video video2 = Video.builder()
                .videoId(3L)
                .youtubeVideoId("unique-youtube-id-3")
                .title("Gatos en el tejado")
                .description("Gatos saltando y jugando en los tejados")
                .thumbnailUrl("https://example.com/thumbnail3")
                .duration(Duration.parse("12"))
                .addedAt(now.minusDays(3))
                .viewCount(BigInteger.valueOf(8000L))
                .likes(BigInteger.valueOf(1200L))
                .build();

        mockVideos = List.of(mockVideo, video1, video2);

        // Configuración de los DTOs de respuesta
        mockVideoResponse = VideoResponse.builder()
                .youtubeVideoId(mockVideo.getYoutubeVideoId())
                .title(mockVideo.getTitle())
                .description(mockVideo.getDescription())
                .thumbnailUrl(mockVideo.getThumbnailUrl())
                .duration(Duration.parse(String.valueOf(mockVideo.getDuration())))
                .addedAt(mockVideo.getAddedAt())
                .viewCount(mockVideo.getViewCount())
                .likeCount(mockVideo.getLikes())
                .build();

        VideoResponse videoResponse1 = VideoResponse.builder()
                .youtubeVideoId(video1.getYoutubeVideoId())
                .title(video1.getTitle())
                .description(video1.getDescription())
                .thumbnailUrl(video1.getThumbnailUrl())
                .duration(Duration.parse(String.valueOf(video1.getDuration())))
                .addedAt(video1.getAddedAt())
                .viewCount(video1.getViewCount())
                .likeCount(video1.getLikes())
                .build();

        VideoResponse videoResponse2 = VideoResponse.builder()
                .youtubeVideoId(video2.getYoutubeVideoId())
                .title(video2.getTitle())
                .description(video2.getDescription())
                .thumbnailUrl(video2.getThumbnailUrl())
                .duration(Duration.parse(String.valueOf(video2.getDuration())))
                .addedAt(video2.getAddedAt())
                .viewCount(video2.getViewCount())
                .likeCount(video2.getLikes())
                .build();

        mockVideoResponses = List.of(mockVideoResponse, videoResponse1, videoResponse2);
    }
     */
}
