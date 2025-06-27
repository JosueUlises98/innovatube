package org.developers.api.controllers.Favorite;


import jakarta.validation.Valid;
import org.developers.api.request.Favorite.*;
import org.developers.api.response.Favorite.FavoriteVideoResponse;
import org.developers.api.response.Favorite.PaginatedVideoFavoritesResponse;
import org.developers.service.interfaces.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/user-videos")
    public ResponseEntity<List<FavoriteVideoResponse>> getUserFavoriteVideos(
            @Valid @RequestBody GetUserFavoriteVideosRequest request) {
        return ResponseEntity.ok(favoriteService.getUserFavoriteVideos(request));
    }

    @GetMapping("/user-videos/paginated")
    public ResponseEntity<PaginatedVideoFavoritesResponse> getUserFavoritesPaginated(
            @Valid @RequestBody GetUserFavoritesPaginated request) {
        return ResponseEntity.ok(favoriteService.getUserFavoritesPaginated(request));
    }

    @GetMapping("/user-videos/check-favorited")
    public ResponseEntity<Boolean> hasUserFavoritedVideo(
            @Valid @RequestBody HasUserFavoritedVideo request) {
        return ResponseEntity.ok(favoriteService.hasUserFavoritedVideo(request));
    }

    @PostMapping("/user-videos/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addVideoToFavorites(
            @Valid @RequestBody AddVideoToFavorites request) {
        favoriteService.addVideoToFavorites(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/user-videos/remove")
    public ResponseEntity<Void> removeVideoFromFavorites(
            @Valid @RequestBody RemoveVideoFromFavorites request) {
        favoriteService.removeVideoFromFavorites(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user-videos/recent")
    public ResponseEntity<List<FavoriteVideoResponse>> getRecentlyFavoritedVideos(
            @Valid @RequestBody GetRecentlyFavoritedVideos request) {
        return ResponseEntity.ok(favoriteService.getRecentlyFavoritedVideos(request));
    }

    @GetMapping("/user-videos/trending")
    public ResponseEntity<List<FavoriteVideoResponse>> getTrendingFavoriteVideos(
            @Valid @RequestBody GetTrendingFavoriteVideos request) {
        return ResponseEntity.ok(favoriteService.getTrendingFavoriteVideos(request));
    }
}
