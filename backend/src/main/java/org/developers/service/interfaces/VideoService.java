package org.developers.service.interfaces;

import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.SearchResult;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VideoService {

    VideoResponse createVideo(CreateVideo createVideoRequest);

    List<SearchResult> searchYoutubeVideos(SearchVideos searchVideosRequest) throws IOException;

    Optional<VideoResponse> getVideoByYoutubeId(GetVideoByYoutubeId getVideoByYoutubeIdRequest);

    List<VideoResponse> searchVideosByTitle(SearchVideosByTitle searchVideosByTitleRequest);

    PaginatedResponse<VideoResponse> getLastestVideos(GetLastestVideos getLatestVideosRequest);

    PaginatedResponse<VideoResponse> searchVideosWithPagination(SearchVideosWithPagination searchVideosWithPaginationRequest);

    List<VideoResponse> getTrendingVideos(GetTrendingVideos getTrendingVideosRequest);

    List<VideoResponse> getMostPopularVideos(GetMostPopularVideos getMostPopularVideosRequest);

    List<VideoResponse> getVideosAddedInPeriod(GetVideosAddedInPeriod videosAddedInPeriodRequest);

    List<VideoResponse> getShortFormVideos(GetShortFormVideos shortFormVideosRequest);

    VideoStatisticsResponse getVideoStatistics(GetVideoStatistics getVideoStatisticsRequest);

    Map<String, Long> getVideosByDurationCategories();

    boolean isVideoAvailableById(IsVideoAvailableById isVideoAvailableById);

    boolean isVideoAvailableByTitle(IsVideoAvailableByTitle isVideoAvailableByTitle);
}
