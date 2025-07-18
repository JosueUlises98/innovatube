package unit.Favorite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.developers.api.controllers.Favorite.FavoriteController;
import org.developers.api.request.Favorite.*;
import org.developers.common.exception.handler.GlobalExceptionHandler;
import org.developers.service.impl.FavoriteServiceImpl;
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
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {
/*
    private MockMvc mockMvc;

    @Mock
    private FavoriteServiceImpl favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(favoriteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("Favorite Tests")
    class GetUserFavoriteVideosTests {
        @Test
        void whenValidRequest_thenReturnsOk() throws Exception {
            GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
            request.setUserId(1L);
            FavoriteVideoResponse response = FavoriteVideoResponse.builder()
                    .youtubeVideoId("123")
                    .videoDescription("ada")
                    .build();
            List<FavoriteVideoResponse> responseList = List.of(response);

            when(favoriteService.getUserFavoriteVideos(request)).thenReturn(responseList);

            mockMvc.perform(post("/api/v1/favorites/user-videos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(responseList)))
                    .andDo(print()) // Esto nos ayudará a ver la respuesta completa en caso de error
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void whenInvalidRequest_thenReturnsBadRequest() throws Exception {

            GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
            request.setUserId(1909887L);

            when(favoriteService.getUserFavoriteVideos(request)).thenThrow(new IllegalArgumentException("Invalid data"));
            mockMvc.perform(post("/api/v1/favorites/user-videos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andDo(print()); // Esto nos ayudará a ver la respuesta completa en caso de error
        }
    }

    @Nested
    @DisplayName("Favorite Tests")
    class GetUserFavoritesPaginatedTests {
        @Test
        void whenValidRequest_thenReturnsOk() throws Exception {
            FavoriteVideoResponse response = FavoriteVideoResponse.builder()
                    .videoDescription("sdsds")
                    .build();
            List<FavoriteVideoResponse> favoriteVideos = List.of(response);
            GetUserFavoritesPaginated request = GetUserFavoritesPaginated.builder()
                    .page(1)
                    .size(1)
                    .userId(1L)
                    .build();
            PaginatedVideoFavoritesResponse response1 = PaginatedVideoFavoritesResponse.builder()
                    .favorites(favoriteVideos)
                    .currentPage(1)
                    .totalPages(1)
                    .totalElements(1)
                    .hasNext(false)
                    .hasPrevious(false)
                    .build();

            when(favoriteService.getUserFavoritesPaginated(request)).thenReturn(response1);

            mockMvc.perform(post("/api/v1/favorites/user-videos/paginated")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andDo(print()) // Esto nos ayudará a ver la respuesta completa en caso de error
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Favorite Tests")
    class HasUserFavoritedVideoTests {
        @Test
        void whenValidRequest_thenReturnsOk() throws Exception {
            HasUserFavoritedVideo request = new HasUserFavoritedVideo();
            request.setVideoId(1L);
            request.setUserId(1L);
            when(favoriteService.hasUserFavoritedVideo(request)).thenReturn(true);

            mockMvc.perform(post("/api/v1/favorites/user-videos/check-favorited")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("Favorite Tests")
    class AddVideoToFavoritesTests {
        @Test
        void whenValidRequest_thenReturnsCreated() throws Exception {
            AddVideoToFavorites request = new AddVideoToFavorites();
            request.setVideoId(1L);
            request.setUserId(2L);
            doNothing().when(favoriteService).addVideoToFavorites(request);

            mockMvc.perform(post("/api/v1/favorites/user-videos/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print()); // Esto nos ayudará a ver la respuesta completa en caso de error
        }
    }

    @Nested
    @DisplayName("Favorite Tests")
    class RemoveVideoFromFavoritesTests {
        @Test
        void whenValidRequest_thenReturnsNoContent() throws Exception {
            RemoveVideoFromFavorites request = new RemoveVideoFromFavorites();
            request.setVideoId(1L);
            request.setUserId(2L);
            doNothing().when(favoriteService).removeVideoFromFavorites(request);

            mockMvc.perform(delete("/api/v1/favorites/user-videos/remove")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNoContent())
                    .andDo(print()); // Esto nos ayudará a ver la respuesta completa en caso de error
        }
    }

    @Nested
    @DisplayName("Favorite Tests")
    class GetTrendingFavoriteVideosTests {
        @Test
        void whenValidRequest_thenReturnsOk() throws Exception {
            GetTrendingFavoriteVideos request = new GetTrendingFavoriteVideos();
            request.setUserId(1L);
            FavoriteVideoResponse response = FavoriteVideoResponse.builder().build();
            List<FavoriteVideoResponse> responseList = List.of(response);

            when(favoriteService.getTrendingFavoriteVideos(request)).thenReturn(responseList);

            mockMvc.perform(post("/api/v1/favorites/user-videos/trending")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(responseList)))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }
 */
}
