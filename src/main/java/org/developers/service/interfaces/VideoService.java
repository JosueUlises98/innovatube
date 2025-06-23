package org.developers.service.interfaces;

import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VideoService {

    //Operaciones Fundamentales
    VideoResponse createVideo(CreateVideoRequest createVideoRequest);
    VideoResponse updateVideo(UpdateVideoRequest updateVideoRequest);
    void deleteVideo(DeleteVideoRequest deleteVideoRequest);

    // Búsqueda y recuperación de videos
    Optional<VideoResponse> getVideoByYoutubeId(GetVideoByYoutubeIdRequest getVideoByYoutubeIdRequest);
    List<VideoResponse> searchVideosByTitle(SearchVideosByTitleRequest searchVideosByTitleRequest);

    // Listados paginados
    PaginatedResponse<VideoResponse> getLastestVideos(GetLastestVideosRequest getLatestVideosRequest);
    PaginatedResponse<VideoResponse> searchVideosWithPagination(SearchVideosWithPaginationRequest searchVideosWithPaginationRequest);

    // Métodos para descubrir contenido
    List<VideoResponse> getTrendingVideos(GetTrendingVideosRequest getTrendingVideosRequest);
    List<VideoResponse> getMostPopularVideos(GetMostPopularVideosRequest getMostPopularVideosRequest);

    // Filtros temporales
    List<VideoResponse> getVideosAddedInPeriod(GetVideosAddedInPeriodRequest videosAddedInPeriodRequest);

    // Filtros de duración
    List<VideoResponse> getShortFormVideos(GetShortFormVideosRequest shortFormVideosRequest);

    // Análisis y estadísticas
    VideoStatisticsResponse getVideoStatistics(GetVideoStatisticsRequest getVideoStatisticsRequest);

    // Métodos adicionales de valor agregado
    Map<String, Long> getVideosByDurationCategories();

    //Metodos de validacion
    boolean isVideoAvailableById(IsVideoAvailableById isVideoAvailableById);
    boolean isVideoAvailableByTitle(IsVideoAvailableByTitle isVideoAvailableByTitle);
}
