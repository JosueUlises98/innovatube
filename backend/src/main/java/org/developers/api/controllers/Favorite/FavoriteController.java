package org.developers.api.controllers.Favorite;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.Favorite.*;
import org.developers.api.response.Favorite.FavoriteVideo;
import org.developers.api.response.Favorite.PaginatedVideoFavorites;
import org.developers.service.interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/favorites"})
@Log4j2
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping({"/user-videos/{userId}"})
    public ResponseEntity<List<FavoriteVideo>> getUserFavoriteVideos(@PathVariable Long userId) {
        log.info("Buscando favoritos de usuario '{}'", userId);
        GetUserFavoriteVideosRequest request = new GetUserFavoriteVideosRequest();
        request.setUserId(userId);
        return ResponseEntity.ok(this.favoriteService.getUserFavoriteVideos(request));
    }

    @GetMapping({"/user-videos/paginated/{userId}"})
    public ResponseEntity<PaginatedVideoFavorites> getUserFavoritesPaginated(@PathVariable Long userId, @RequestParam Integer page, @RequestParam Integer size) {
        log.info("Buscando favoritos de usuario paginados '{}'", userId);
        GetUserFavoritesPaginated request = GetUserFavoritesPaginated.builder().userId(userId).page(page).size(size).build();
        return ResponseEntity.ok(this.favoriteService.getUserFavoritesPaginated(request));
    }

    @GetMapping({"/user-videos/{userId}/check-favorited/{videoId}"})
    public ResponseEntity<Boolean> hasUserFavoritedVideo(@PathVariable Long userId, @PathVariable Long videoId) {
        log.info("Validacion de favorito de usuario '{}'", userId);
        HasUserFavoritedVideo request = new HasUserFavoritedVideo();
        request.setUserId(userId);
        request.setVideoId(videoId);
        return ResponseEntity.ok(this.favoriteService.hasUserFavoritedVideo(request));
    }

    @PostMapping({"/user-videos/add"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addVideoToFavorites(@RequestBody @Valid AddVideoToFavorites request) {
        log.info("Agregando favorito de usuario '{}'", request.getUserId());
        this.favoriteService.addVideoToFavorites(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping({"/user-videos/remove"})
    public ResponseEntity<Void> removeVideoFromFavorites(@RequestBody @Valid RemoveVideoFromFavorites request) {
        log.info("Eliminando favorito de usuario '{}'", request.getUserId());
        this.favoriteService.removeVideoFromFavorites(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping({"/user-videos/trending/{userId}"})
    public ResponseEntity<List<FavoriteVideo>> getTrendingFavoriteVideos(@PathVariable Long userId) {
        log.info("Obteniendo favoritos de usuario '{}'", userId);
        GetTrendingFavoriteVideos request = new GetTrendingFavoriteVideos();
        request.setUserId(userId);
        return ResponseEntity.ok(this.favoriteService.getTrendingFavoriteVideos(request));
    }
}
