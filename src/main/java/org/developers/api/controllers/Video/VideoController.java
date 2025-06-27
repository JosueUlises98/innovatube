package org.developers.api.controllers.Video;

import jakarta.validation.Valid;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.service.interfaces.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<VideoResponse> createVideo(@Valid @RequestBody CreateVideoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(videoService.createVideo(request));
    }

    @PutMapping
    public ResponseEntity<VideoResponse> updateVideo(@Valid @RequestBody UpdateVideoRequest request) {
        return ResponseEntity.ok(videoService.updateVideo(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVideo(@Valid @RequestBody DeleteVideoRequest request) {
        videoService.deleteVideo(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/youtube/{youtubeId}")
    public ResponseEntity<VideoResponse> getVideoByYoutubeId(@PathVariable String youtubeId) {
        GetVideoByYoutubeIdRequest request = new GetVideoByYoutubeIdRequest();
        request.setYoutubeId(youtubeId);
        return videoService.getVideoByYoutubeId(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VideoResponse>> searchVideosByTitle(@RequestParam String title) {
        SearchVideosByTitleRequest request = new SearchVideosByTitleRequest();
        request.setTitle(title);
        return ResponseEntity.ok(videoService.searchVideosByTitle(request));
    }

    @GetMapping("/latest")
    public ResponseEntity<PaginatedResponse<VideoResponse>> getLatestVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        GetLastestVideosRequest request = new GetLastestVideosRequest();
        request.setPage(page);
        request.setSize(size);
        return ResponseEntity.ok(videoService.getLastestVideos(request));
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<PaginatedResponse<VideoResponse>> searchVideosWithPagination(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        SearchVideosWithPaginationRequest request = new SearchVideosWithPaginationRequest();
        request.setPage(page);
        request.setSize(size);
        request.setTitle(query);
        return ResponseEntity.ok(videoService.searchVideosWithPagination(request));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<VideoResponse>> getTrendingVideos(
            @RequestParam(defaultValue = "10") int limit) {
        GetTrendingVideosRequest request = new GetTrendingVideosRequest();
        request.setMinimumViews(limit);
        return ResponseEntity.ok(videoService.getTrendingVideos(request));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<VideoResponse>> getMostPopularVideos(
            @RequestParam(defaultValue = "10") int limit) {
        GetMostPopularVideosRequest request = new GetMostPopularVideosRequest();
        request.setLimit(limit);
        return ResponseEntity.ok(videoService.getMostPopularVideos(request));
    }

    @GetMapping("/period")
    public ResponseEntity<List<VideoResponse>> getVideosAddedInPeriod(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        GetVideosAddedInPeriodRequest request = new GetVideosAddedInPeriodRequest();
        request.setStartDate(LocalDateTime.parse(startDate));
        request.setEndDate(LocalDateTime.parse(endDate));
        return ResponseEntity.ok(videoService.getVideosAddedInPeriod(request));
    }

    @GetMapping("/shorts")
    public ResponseEntity<List<VideoResponse>> getShortFormVideos(
            @RequestParam(defaultValue = "10") String duration) {
        GetShortFormVideosRequest request = new GetShortFormVideosRequest();
        request.setMaxDuration(duration);
        return ResponseEntity.ok(videoService.getShortFormVideos(request));
    }

    @GetMapping("/{videoId}/statistics")
    public ResponseEntity<VideoStatisticsResponse> getVideoStatistics(
            @PathVariable String videoId) {
        GetVideoStatisticsRequest request = new GetVideoStatisticsRequest();
        request.setVideoId(Long.valueOf(videoId));
        return ResponseEntity.ok(videoService.getVideoStatistics(request));
    }

    @GetMapping("/duration-categories")
    public ResponseEntity<Map<String, Long>> getVideosByDurationCategories() {
        return ResponseEntity.ok(videoService.getVideosByDurationCategories());
    }

    @GetMapping("/available/id/{videoId}")
    public ResponseEntity<Boolean> isVideoAvailableById(@PathVariable String videoId) {
        IsVideoAvailableById request = new IsVideoAvailableById();
        request.setVideoId(Long.valueOf(videoId));
        return ResponseEntity.ok(videoService.isVideoAvailableById(request));
    }

    @GetMapping("/available/title")
    public ResponseEntity<Boolean> isVideoAvailableByTitle(@RequestParam String title) {
        IsVideoAvailableByTitle request = new IsVideoAvailableByTitle();
        request.setTitle(title);
        return ResponseEntity.ok(videoService.isVideoAvailableByTitle(request));
    }
}
