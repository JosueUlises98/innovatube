package org.developers.api.controllers.Video;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.SearchResult;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.api.response.YouTube.*;
import org.developers.service.impl.VideoServiceImpl;
import org.developers.service.interfaces.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/videos"})
@Log4j2
@CrossOrigin(origins = "*")
public class VideoController {

    private final VideoServiceImpl videoService;
    
    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    public VideoController(VideoServiceImpl videoService) {
        this.videoService = videoService;
    }

    @PostMapping({"/create"})
    public ResponseEntity<VideoResponse> createVideo(@RequestBody @Valid CreateVideo request) {
        log.info("creando video");
        return ResponseEntity.status(HttpStatus.CREATED).body(this.videoService.createVideo(request));
    }

    @GetMapping({"/search"})
    public ResponseEntity<List<SearchResult>> searchVideos(@RequestParam String query, @RequestParam String regionCode, @RequestParam String languageCode) throws IOException {
        log.debug("Buscando videos '{}'", query);
        SearchVideos searchRequest = new SearchVideos();
        searchRequest.setSearch(query);
        searchRequest.setRegionCode(regionCode);
        searchRequest.setLanguageCode(languageCode);
        return ResponseEntity.of(Optional.ofNullable(this.videoService.searchYoutubeVideos(searchRequest)));
    }

    @GetMapping({"/youtube/{youtubeId}"})
    public ResponseEntity<VideoResponse> getVideoByYoutubeId(@PathVariable String youtubeId, @RequestParam Long userId) {
        log.info("Buscando video por youtubeId '{}'", youtubeId);
        GetVideoByYoutubeId request = new GetVideoByYoutubeId();
        request.setYoutubeId(youtubeId);
        request.setUserId(userId);
        Optional<VideoResponse> videoByYoutubeId = videoService.getVideoByYoutubeId(request);
        return videoByYoutubeId.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/search/title"})
    public ResponseEntity<List<VideoResponse>> searchVideosByTitle(@RequestParam String title, @RequestParam Long userId) {
        log.info("Buscando videos por titulo '{}'", title);
        SearchVideosByTitle request = new SearchVideosByTitle();
        request.setTitle(title);
        request.setUserId(userId);
        return ResponseEntity.of(Optional.ofNullable(this.videoService.searchVideosByTitle(request)));
    }

    @GetMapping({"/latest"})
    public ResponseEntity<PaginatedResponse<VideoResponse>> getLatestVideos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Long userId) {
        log.info("Buscando videos recientes '{}'", page);
        GetLastestVideos request = new GetLastestVideos();
        request.setPage(page);
        request.setSize(size);
        request.setUserId(userId);
        return ResponseEntity.of(Optional.ofNullable(videoService.getLastestVideos(request)));
    }

    @GetMapping({"/search/paginated"})
    public ResponseEntity<PaginatedResponse<VideoResponse>> searchVideosWithPagination(@RequestParam String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Long userId) {
        log.info("Buscando videos paginados '{}'", query);
        SearchVideosWithPagination request = new SearchVideosWithPagination();
        request.setPage(page);
        request.setSize(size);
        request.setTitle(query);
        request.setUserId(userId);
        return ResponseEntity.of(Optional.ofNullable(videoService.searchVideosWithPagination(request)));
    }

    @GetMapping({"/trending"})
    public ResponseEntity<List<VideoResponse>> getTrendingVideos(@RequestParam(defaultValue = "10") int viewlimit, @RequestParam(required = false) Long userId) {
        log.info("Obteniendo videos de trending '{}'", viewlimit);
        GetTrendingVideos request = new GetTrendingVideos();
        request.setMinimumViews(viewlimit);
        request.setUserId(userId);
        return ResponseEntity.of(Optional.ofNullable(videoService.getTrendingVideos(request)));
    }

    @GetMapping({"/popular"})
    public ResponseEntity<List<VideoResponse>> getMostPopularVideos(@RequestParam(defaultValue = "10") int limit) {
        log.info("Obteniendo videos de popularidad '{}'", limit);
        GetMostPopularVideos request = new GetMostPopularVideos();
        request.setLimit(limit);
        return ResponseEntity.of(Optional.ofNullable(videoService.getMostPopularVideos(request)));
    }

    @GetMapping({"/period"})
    public ResponseEntity<List<VideoResponse>> getVideosAddedInPeriod(@RequestParam String startDate, @RequestParam String endDate, @RequestParam Long userId) {
        log.info("Obteniendo videos agregados en el periodo '{} - {}'", startDate, endDate);
        GetVideosAddedInPeriod request = new GetVideosAddedInPeriod();
        request.setStartDate(LocalDateTime.parse(startDate));
        request.setEndDate(LocalDateTime.parse(endDate));
        request.setUserId(userId);
        return ResponseEntity.of(Optional.ofNullable(videoService.getVideosAddedInPeriod(request)));
    }

    @GetMapping({"/shorts"})
    public ResponseEntity<List<VideoResponse>> getShortFormVideos(@RequestParam(defaultValue = "10") String duration) {
        log.info("Obtener videos cortos '{}'", duration + " minutos o menos");
        GetShortFormVideos request = new GetShortFormVideos();
        request.setMaxDuration(duration);
        return ResponseEntity.of(Optional.ofNullable(videoService.getShortFormVideos(request)));
    }

    @GetMapping({"/{videoId}/statistics"})
    public ResponseEntity<VideoStatisticsResponse> getVideoStatistics(@PathVariable String videoId) {
        log.info("Obteniendo estadisticas del video '{}'", videoId);
        GetVideoStatistics request = new GetVideoStatistics();
        request.setVideoId(Long.valueOf(videoId));
        return ResponseEntity.of(Optional.ofNullable(videoService.getVideoStatistics(request)));
    }

    @GetMapping({"/duration-categories"})
    public ResponseEntity<Map<String, Long>> getVideosByDurationCategories() {
        log.info("Obteniendo videos por categorias de duracion");
        return ResponseEntity.of(Optional.ofNullable(this.videoService.getVideosByDurationCategories()));
    }

    @GetMapping({"/available/id/{videoId}"})
    public ResponseEntity<Boolean> isVideoAvailableById(@PathVariable String videoId) {
        log.info("Buscando si el video con id '{}' esta disponible", videoId);
        IsVideoAvailableById request = new IsVideoAvailableById();
        request.setVideoId(Long.valueOf(videoId));
        return ResponseEntity.ok(this.videoService.isVideoAvailableById(request));
    }

    @GetMapping({"/available/title"})
    public ResponseEntity<Boolean> isVideoAvailableByTitle(@RequestParam String title) {
        log.info("Buscando si el video con titulo '{}' esta disponible", title);
        IsVideoAvailableByTitle request = new IsVideoAvailableByTitle();
        request.setTitle(title);
        return ResponseEntity.ok(this.videoService.isVideoAvailableByTitle(request));
    }

    // YouTube Integration Endpoints

    @GetMapping({"/youtube/search"})
    public ResponseEntity<YouTubeSearchResult> searchYouTubeVideos(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "US") String regionCode) {
        
        log.info("Searching YouTube videos: '{}'", query);
        try {
            YouTubeSearchResult result = youTubeService.searchVideos(query, maxResults, regionCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error searching YouTube videos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/videos/{videoId}"})
    public ResponseEntity<YouTubeVideoDetails> getYouTubeVideoDetails(@PathVariable String videoId) {
        log.info("Getting YouTube video details: '{}'", videoId);
        try {
            Optional<YouTubeVideoDetails> video = youTubeService.getVideoDetails(videoId);
            return video.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting YouTube video details: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/trending"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeTrendingVideos(
            @RequestParam(defaultValue = "US") String regionCode,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        log.info("Getting YouTube trending videos for region: '{}'", regionCode);
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getTrendingVideos(regionCode, categoryId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("Error getting YouTube trending videos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping({"/youtube/categories"})
    public ResponseEntity<List<YouTubeCategory>> getYouTubeCategories(
            @RequestParam(defaultValue = "US") String regionCode) {
        
        log.info("Getting YouTube categories for region: '{}'", regionCode);
        try {
            List<YouTubeCategory> categories = youTubeService.getVideoCategories(regionCode);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error getting YouTube categories: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/videos/{videoId}/related"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeRelatedVideos(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        log.info("Getting related YouTube videos for: '{}'", videoId);
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getRelatedVideos(videoId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("Error getting related YouTube videos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/channels/{channelId}"})
    public ResponseEntity<YouTubeChannelDetails> getYouTubeChannelDetails(@PathVariable String channelId) {
        log.info("Getting YouTube channel details: '{}'", channelId);
        try {
            Optional<YouTubeChannelDetails> channel = youTubeService.getChannelDetails(channelId);
            return channel.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting YouTube channel details: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/videos/{videoId}/comments"})
    public ResponseEntity<List<YouTubeComment>> getYouTubeVideoComments(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        log.info("Getting YouTube video comments for: '{}'", videoId);
        try {
            List<YouTubeComment> comments = youTubeService.getVideoComments(videoId, maxResults);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("Error getting YouTube video comments: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/playlists/{playlistId}"})
    public ResponseEntity<YouTubePlaylist> getYouTubePlaylistDetails(@PathVariable String playlistId) {
        log.info("Getting YouTube playlist details: '{}'", playlistId);
        try {
            Optional<YouTubePlaylist> playlist = youTubeService.getPlaylistDetails(playlistId);
            return playlist.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting YouTube playlist details: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping({"/youtube/playlists/{playlistId}/videos"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubePlaylistVideos(
            @PathVariable String playlistId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        log.info("Getting YouTube playlist videos for: '{}'", playlistId);
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getPlaylistVideos(playlistId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("Error getting YouTube playlist videos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
