package org.developers.api.controllers.YouTube;

import lombok.extern.log4j.Log4j2;
import org.developers.api.response.YouTube.*;
import org.developers.service.impl.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/youtube"})
@Log4j2
public class YouTubeController {

    private final VideoServiceImpl videoService;

    @Autowired
    public YouTubeController(VideoServiceImpl videoService) {
        this.videoService = videoService;
    }

    @GetMapping({"/search"})
    public ResponseEntity<YouTubeSearchResult> searchYouTubeVideos(
            @RequestParam String q,
            @RequestParam(defaultValue = "50") int maxResults,
            @RequestParam(defaultValue = "US") String regionCode) throws IOException {
        log.info("Searching YouTube videos for query: '{}'", q);
        return ResponseEntity.ok(videoService.searchYouTubeVideos(q, maxResults, regionCode));
    }

    @GetMapping({"/videos/{videoId}"})
    public ResponseEntity<YouTubeVideoDetails> getYouTubeVideoDetails(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video details for videoId: '{}'", videoId);
        Optional<YouTubeVideoDetails> videoDetails = videoService.getYouTubeVideoDetails(videoId);
        return videoDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/trending"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeTrendingVideos(
            @RequestParam(defaultValue = "US") String regionCode,
            @RequestParam(defaultValue = "50") int maxResults,
            @RequestParam(defaultValue = "") String categoryId) throws IOException {
        log.info("Getting YouTube trending videos for region: '{}'", regionCode);
        return ResponseEntity.ok(videoService.getYouTubeTrendingVideos(regionCode, categoryId));
    }

    @GetMapping({"/categories"})
    public ResponseEntity<List<YouTubeCategory>> getYouTubeVideoCategories(
            @RequestParam(defaultValue = "US") String regionCode) throws IOException {
        log.info("Getting YouTube video categories for region: '{}'", regionCode);
        return ResponseEntity.ok(videoService.getYouTubeVideoCategories(regionCode));
    }

    @GetMapping({"/playlists"})
    public ResponseEntity<List<YouTubePlaylist>> getYouTubePlaylists(
            @RequestParam String query,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube playlists for query: '{}'", query);
        return ResponseEntity.ok(videoService.getYouTubePlaylists(query, maxResults));
    }

    @GetMapping({"/playlists/{playlistId}/items"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubePlaylistItems(
            @PathVariable String playlistId,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube playlist items for playlistId: '{}'", playlistId);
        return ResponseEntity.ok(videoService.getYouTubePlaylistItems(playlistId, maxResults));
    }

    @GetMapping({"/channels/{channelId}/videos"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeChannelVideos(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube channel videos for channelId: '{}'", channelId);
        return ResponseEntity.ok(videoService.getYouTubeChannelVideos(channelId, maxResults));
    }

    @GetMapping({"/videos/{videoId}/comments"})
    public ResponseEntity<List<YouTubeComment>> getYouTubeVideoComments(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "100") int maxResults) throws IOException {
        log.info("Getting YouTube video comments for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoComments(videoId, maxResults));
    }

    @GetMapping({"/videos/{videoId}/related"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeRelatedVideos(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube related videos for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeRelatedVideos(videoId, maxResults));
    }

    @GetMapping({"/videos/{videoId}/statistics"})
    public ResponseEntity<YouTubeVideoStatistics> getYouTubeVideoStatistics(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video statistics for videoId: '{}'", videoId);
        Optional<YouTubeVideoStatistics> statistics = videoService.getYouTubeVideoStatistics(videoId);
        return statistics.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/channels/{channelId}"})
    public ResponseEntity<YouTubeChannelDetails> getYouTubeChannelDetails(@PathVariable String channelId) throws IOException {
        log.info("Getting YouTube channel details for channelId: '{}'", channelId);
        Optional<YouTubeChannelDetails> channelDetails = videoService.getYouTubeChannelDetails(channelId);
        return channelDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/suggestions"})
    public ResponseEntity<List<String>> getYouTubeSearchSuggestions(@RequestParam String q) throws IOException {
        log.info("Getting YouTube search suggestions for query: '{}'", q);
        return ResponseEntity.ok(videoService.getYouTubeSearchSuggestions(q));
    }

    @GetMapping({"/videos/{videoId}/captions"})
    public ResponseEntity<List<YouTubeCaption>> getYouTubeVideoCaptions(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video captions for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoCaptions(videoId));
    }

    @GetMapping({"/videos/{videoId}/thumbnails"})
    public ResponseEntity<YouTubeThumbnails> getYouTubeVideoThumbnails(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video thumbnails for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoThumbnails(videoId));
    }

    @GetMapping({"/videos/{videoId}/quality"})
    public ResponseEntity<List<String>> getYouTubeVideoQualityOptions(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video quality options for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoQualityOptions(videoId));
    }

    @GetMapping({"/videos/{videoId}/subtitles"})
    public ResponseEntity<List<YouTubeSubtitle>> getYouTubeVideoSubtitles(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video subtitles for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoSubtitles(videoId));
    }

    @GetMapping({"/videos/{videoId}/chapters"})
    public ResponseEntity<List<YouTubeChapter>> getYouTubeVideoChapters(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video chapters for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoChapters(videoId));
    }

    @GetMapping({"/videos/{videoId}/analytics"})
    public ResponseEntity<YouTubeVideoAnalytics> getYouTubeVideoAnalytics(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video analytics for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoAnalytics(videoId));
    }

    @GetMapping({"/videos/{videoId}/recommendations"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeVideoRecommendations(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube video recommendations for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoRecommendations(videoId, maxResults));
    }

    @GetMapping({"/videos/{videoId}/similar"})
    public ResponseEntity<List<YouTubeVideoDetails>> getYouTubeSimilarVideos(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "50") int maxResults) throws IOException {
        log.info("Getting YouTube similar videos for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeSimilarVideos(videoId, maxResults));
    }

    @GetMapping({"/videos/{videoId}/transcript"})
    public ResponseEntity<String> getYouTubeVideoTranscript(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video transcript for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoTranscript(videoId));
    }

    @GetMapping({"/videos/{videoId}/metadata"})
    public ResponseEntity<YouTubeVideoMetadata> getYouTubeVideoMetadata(@PathVariable String videoId) throws IOException {
        log.info("Getting YouTube video metadata for videoId: '{}'", videoId);
        return ResponseEntity.ok(videoService.getYouTubeVideoMetadata(videoId));
    }
} 