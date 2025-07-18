package org.developers.api.controllers.Video;

import org.developers.api.response.YouTube.*;
import org.developers.service.interfaces.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/youtube")
@CrossOrigin(origins = "*")
public class VideoYouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/search")
    public ResponseEntity<YouTubeSearchResult> searchVideos(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "US") String regionCode) {
        
        try {
            YouTubeSearchResult result = youTubeService.searchVideos(query, maxResults, regionCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<YouTubeVideoDetails> getVideoDetails(@PathVariable String videoId) {
        try {
            Optional<YouTubeVideoDetails> video = youTubeService.getVideoDetails(videoId);
            return video.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/videos/{videoId}/statistics")
    public ResponseEntity<YouTubeVideoStatistics> getVideoStatistics(@PathVariable String videoId) {
        try {
            Optional<YouTubeVideoStatistics> stats = youTubeService.getVideoStatistics(videoId);
            return stats.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/channels/{channelId}")
    public ResponseEntity<YouTubeChannelDetails> getChannelDetails(@PathVariable String channelId) {
        try {
            Optional<YouTubeChannelDetails> channel = youTubeService.getChannelDetails(channelId);
            return channel.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/videos/{videoId}/comments")
    public ResponseEntity<List<YouTubeComment>> getVideoComments(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeComment> comments = youTubeService.getVideoComments(videoId, maxResults);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<List<YouTubeVideoDetails>> getTrendingVideos(
            @RequestParam(defaultValue = "US") String regionCode,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getTrendingVideos(regionCode, categoryId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<YouTubeCategory>> getVideoCategories(
            @RequestParam(defaultValue = "US") String regionCode) {
        
        try {
            List<YouTubeCategory> categories = youTubeService.getVideoCategories(regionCode);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/videos/{videoId}/related")
    public ResponseEntity<List<YouTubeVideoDetails>> getRelatedVideos(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getRelatedVideos(videoId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/playlists/{playlistId}")
    public ResponseEntity<YouTubePlaylist> getPlaylistDetails(@PathVariable String playlistId) {
        try {
            Optional<YouTubePlaylist> playlist = youTubeService.getPlaylistDetails(playlistId);
            return playlist.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/playlists/{playlistId}/videos")
    public ResponseEntity<List<YouTubeVideoDetails>> getPlaylistVideos(
            @PathVariable String playlistId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getPlaylistVideos(playlistId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/channels/search")
    public ResponseEntity<List<YouTubeChannelDetails>> searchChannels(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeChannelDetails> channels = youTubeService.searchChannels(query, maxResults);
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/channels/{channelId}/videos")
    public ResponseEntity<List<YouTubeVideoDetails>> getChannelVideos(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "20") int maxResults) {
        
        try {
            List<YouTubeVideoDetails> videos = youTubeService.getChannelVideos(channelId, maxResults);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 