package unit.Video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.developers.api.controllers.Video.VideoController;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.common.exception.exceptions.ResourceNotFoundException;
import org.developers.common.exception.handler.GlobalExceptionHandler;
import org.developers.service.impl.VideoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VideoControllerTest {
    /*
        private MockMvc mockMvc;

    @Mock
    private VideoServiceImpl videoService;

    @InjectMocks
    private VideoController videoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(videoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("Create Video Tests")
    class CreateVideoTests {
        @Test
        void createVideo_ConDatosValidos_DeberiaRetornarVideoCreado() throws Exception {
            // Arrange
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId("yt123456");
            VideoResponse response = VideoResponse.builder()
                    .youtubeVideoId("1")
                    .title("Test Video")
                    .description("Description")
                    .thumbnailUrl("yt123456")
                    .build();

            when(videoService.createVideo(request)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(post("/api/v1/videos/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)))
                    .andDo(print());
        }

        @Test
        void createVideo_ConDatosInvalidos_DeberiaRetornarError400() throws Exception {
            // Arrange
            CreateVideoRequest request = new CreateVideoRequest();
            request.setYoutubeId("yt123456");

            when(videoService.createVideo(request)).thenThrow(new IllegalArgumentException("Invalid data"));

            // Act & Assert
            mockMvc.perform(post("/api/v1/videos/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Search Videos Tests")
    class SearchVideosTests {
        @Test
        void searchVideosByTitle_ConResultados_DeberiaRetornarLista() throws Exception {
            // Arrange
            List<VideoResponse> videos = Arrays.asList(
                    VideoResponse.builder().youtubeVideoId("1").title("Test Video 1").description("Desc 1").thumbnailUrl("yt1").build(),
                    VideoResponse.builder().youtubeVideoId("2").title("Test Video 2").description("Desc 2").thumbnailUrl("yt2").build()
            );

            when(videoService.searchVideosByTitle(any())).thenReturn(videos);

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/search/title")
                            .param("title", "Nonexistent"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(videos)))
                    .andDo(print());
        }

        @Test
        void searchVideosByTitle_SinResultados_DeberiaRetornarListaVacia() throws Exception {
            // Arrange
            when(videoService.searchVideosByTitle(any())).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/search/title")
                            .param("title", "Nonexistent"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Video Statistics Tests")
    class VideoStatisticsTests {
        @Test
        void getVideoStatistics_ConIdValido_DeberiaRetornarEstadisticas() throws Exception {
            // Arrange
            GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
            request.setVideoId(1L);
            VideoStatisticsResponse statistics = VideoStatisticsResponse.builder()
                    .totalViews(1000L)
                    .averageDuration(500L)
                    .totalFavorites(100L)
                    .build();

            when(videoService.getVideoStatistics(request)).thenReturn(statistics);

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/{videoId}/statistics",request.getVideoId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }

        @Test
        void getVideoStatistics_ConIdInvalido_DeberiaRetornarError404() throws Exception {
            // Arrange
            Long videoId = 999L;
            GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
            request.setVideoId(videoId);
            when(videoService.getVideoStatistics(request))
                    .thenThrow(new ResourceNotFoundException("Estadísticas no encontradas para el ID: " + videoId + " en la base de datos"));

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/{videoId}/statistics", videoId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Latest Videos Tests")
    class LatestVideosTests {
        @Test
        void getLatestVideos_DeberiaRetornarPaginado() throws Exception {
            // Arrange
            GetLastestVideosRequest request = new GetLastestVideosRequest();
            request.setPage(0);
            request.setSize(10);
            List<VideoResponse> videos = Arrays.asList(
                    VideoResponse.builder().youtubeVideoId("1").title("Latest 1").description("Desc 1").thumbnailUrl("yt1").build(),
                    VideoResponse.builder().youtubeVideoId("2").title("Latest 2").description("Desc 2").thumbnailUrl("yt2").build()
            );
            PaginatedResponse<VideoResponse> response = PaginatedResponse.<VideoResponse>builder()
                    .content(videos)
                    .currentPage(0)
                    .totalPages(10)
                    .totalElements(2L)
                    .build();

            when(videoService.getLastestVideos(request)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/latest")
                            .param("page", "0")
                            .param("size", "10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(response)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Video Availability Tests")
    class VideoAvailabilityTests {
        @Test
        void isVideoAvailableById_ConVideoExistente_DeberiaRetornarTrue() throws Exception {
            // Arrange
            IsVideoAvailableById request = new IsVideoAvailableById();
            request.setVideoId(1L);
            when(videoService.isVideoAvailableById(request)).thenReturn(true);

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/available/id/{videoId}", request.getVideoId()))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }

        @Test
        void isVideoAvailableById_ConVideoInexistente_DeberiaRetornarFalse() throws Exception {
            // Arrange
            IsVideoAvailableById request = new IsVideoAvailableById();
            request.setVideoId(999L);
            when(videoService.isVideoAvailableById(any())).thenReturn(false);

            // Act & Assert
            mockMvc.perform(get("/api/v1/videos/available/id/{videoId}",request.getVideoId()))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
    }
     */
}
