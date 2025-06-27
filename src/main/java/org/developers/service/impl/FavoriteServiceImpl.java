package org.developers.service.impl;

import com.google.api.services.youtube.YouTube;
import org.developers.api.request.Favorite.*;
import org.developers.api.request.User.IsUserNameAvaibleRequest;
import org.developers.api.request.Video.IsVideoAvailableById;
import org.developers.api.response.Favorite.FavoriteVideoResponse;
import org.developers.api.response.Favorite.PaginatedVideoFavoritesResponse;
import org.developers.common.exception.exceptions.*;
import org.developers.common.utils.quota.YouTubeQuotaUtil;
import org.developers.model.entities.Favorite;
import org.developers.model.mapper.FavoriteMapper;
import org.developers.repository.FavoriteRepository;
import org.developers.service.interfaces.FavoriteService;
import org.developers.service.interfaces.UserService;
import org.developers.service.interfaces.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserService userService;
    private final VideoService videoService;
    private final YouTube youtube;
    private static final Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper,
                               YouTube youtube, UserService userService, VideoService videoService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.youtube = youtube;
        this.userService = userService;
        this.videoService = videoService;
        logger.info("FavoriteService inicializado");
    }

    @Override
    public List<FavoriteVideoResponse> getUserFavoriteVideos(GetUserFavoriteVideosRequest request) {
        logger.info("Obteniendo videos favoritos para usuario: {}", request.getUserId());
        List<Favorite> favorites = favoriteRepository.findByUserId(request.getUserId());
        if (favorites.isEmpty()) {
            throw new UserFavoriteVideosException(String.format("No hay favoritos para el usuario %s", request.getUserId()));
        }
        return favoriteMapper.toResponseList(favorites);
    }

    @Override
    public boolean hasUserFavoritedVideo(HasUserFavoritedVideo request) {
        logger.info("Verificando si el usuario {} tiene como favorito el video {}",
                request.getUserId(), request.getVideoId());
        return favoriteRepository.existsByUserIdAndVideoId(request.getUserId(), request.getVideoId());
    }

    @Override
    public PaginatedVideoFavoritesResponse getUserFavoritesPaginated(GetUserFavoritesPaginated request) {
        logger.info("Obteniendo favoritos paginados para usuario: {}, página: {}, tamaño: {}",
                request.getUserId(), request.getPage(), request.getSize());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Favorite> favoritesPage = favoriteRepository.findByUserId(request.getUserId(), pageable);

        if (favoritesPage.isEmpty()) {
            throw new UserFavoritesPaginatedException(
                    String.format("No hay favoritos para el usuario %s", request.getUserId()));
        }

        return PaginatedVideoFavoritesResponse.builder()
                .favorites(favoriteMapper.toResponseList(favoritesPage.getContent()))
                .currentPage(favoritesPage.getNumber())
                .totalPages(favoritesPage.getTotalPages())
                .totalElements(favoritesPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public void addVideoToFavorites(AddVideoToFavorites request) {
        logger.info("Agregando video {} a favoritos del usuario {}",
                request.getVideoId(), request.getUserId());

        IsUserNameAvaibleRequest avaibleRequest = new IsUserNameAvaibleRequest();
        avaibleRequest.setUserId(request.getUserId());

        if (!userService.isUserNameAvailableById(avaibleRequest)) {
            logger.error("Usuario no encontrado con ID: {}", request.getUserId());
            throw new UserNotFoundException(String.format("Usuario no encontrado con ID: %s", request.getUserId()));
        }
        IsVideoAvailableById videoAvailableRequest = new IsVideoAvailableById();
        videoAvailableRequest.setVideoId(request.getVideoId());

        if (!videoService.isVideoAvailableById(videoAvailableRequest)) {
            logger.error("Video no encontrado con ID: {}", request.getVideoId());
            throw new VideoNotFoundException(String.format("Video no encontrado con ID: %s", request.getVideoId())
                    + " - " + videoAvailableRequest.getVideoId());
        }
        HasUserFavoritedVideo hasUserFavoritedVideoRequest = new HasUserFavoritedVideo();
        hasUserFavoritedVideoRequest.setUserId(request.getUserId());
        hasUserFavoritedVideoRequest.setVideoId(request.getVideoId());

        if (hasUserFavoritedVideo(hasUserFavoritedVideoRequest)) {
            logger.warn("El video ya está en favoritos para el usuario");
            throw new FavoritedVideoExistsException(String.format("Video ya existe para el usuario %s", request.getUserId()));
        }

        try {
            Favorite favorite = Favorite.builder()
                    .userId(request.getUserId())
                    .videoId(request.getVideoId())
                    .addedAt(LocalDateTime.now())
                    .build();

            favoriteRepository.save(favorite);
            syncWithYouTube(favorite, true);

            logger.info("Video agregado exitosamente a favoritos");
        } catch (Exception e) {
            logger.error("Error al agregar video a favoritos: {}", e.getMessage());
            throw new YouTubeSyncException("Error al sincronizar con YouTube");
        }
    }

    @Override
    @Transactional
    public void removeVideoFromFavorites(RemoveVideoFromFavorites request) {
        logger.info("Removiendo video {} de favoritos del usuario {}",
                request.getVideoId(), request.getUserId());

        if (favoriteRepository.existsByUserIdAndVideoId(request.getUserId(), request.getVideoId())) {
            Optional<Favorite> byVideoIdAndUserId = favoriteRepository.findByUserIdAndVideoId(request.getUserId(),request.getVideoId());
            try {
                syncWithYouTube(byVideoIdAndUserId.orElseThrow(), false);
                logger.info("Video removido exitosamente de favoritos");
            } catch (Exception e) {
                logger.error("Error al remover video de favoritos: {}", e.getMessage());
                throw new YouTubeSyncException("Error al sincronizar con YouTube");
            }
        }
    }

    @Override
    public List<FavoriteVideoResponse> getRecentlyFavoritedVideos(GetRecentlyFavoritedVideos request) {
        logger.info("Obteniendo videos favoritos recientes");
        List<Favorite> recentFavoritesByUserIdAndVideoId = favoriteRepository.findRecentFavoritesByUserId(request.getUserId());
        if (recentFavoritesByUserIdAndVideoId.isEmpty()) {
            throw new RecentlyFavoritedVideosNotFoundException(String.format("No hay videos favoritos recientes para el usuario %s", request.getUserId()));
        }
        return favoriteMapper.toResponseList(recentFavoritesByUserIdAndVideoId);
    }

    @Override
    public List<FavoriteVideoResponse> getTrendingFavoriteVideos(GetTrendingFavoriteVideos request) {
        logger.info("Obteniendo videos favoritos en tendencia");
        List<Favorite> trendingFavorites = favoriteRepository.findTrendingFavorites(request.getUserId(), LocalDateTime.now().minusHours(1));
        if (trendingFavorites.isEmpty()) {
            throw new TrendingFavoritedVideosNotFoundException(String.format("No hay videos de tendencia para el usuario %s", request.getUserId()));
        }
        return favoriteMapper.toResponseList(trendingFavorites);
    }

    private void syncWithYouTube(Favorite favorite, boolean isAdding) {
        logger.info("Sincronizando calificación con YouTube - Operación: {}, VideoId: {}",
                isAdding ? "like" : "none", favorite.getVideoId());
        try {
            List<String> videoIdsByUserId = favoriteRepository.findYoutubeVideoIdsByUserId(favorite.getUserId());
            if (videoIdsByUserId.isEmpty()) {
                throw new FavoritedVideosNotExistsException(String.format("No hay videos favoritos para el usuario %s", favorite.getUserId()));
            }
            String rating = isAdding ? "like" : "none";

            if (YouTubeQuotaUtil.checkQuotaAvailable()){
                YouTube.Videos.Rate rateRequest = youtube.videos()
                        .rate(videoIdsByUserId.getFirst(), rating);

                rateRequest.execute();
                YouTubeQuotaUtil.incrementQuota(50);
            }

            logger.info("Sincronización de calificación completada exitosamente. Rating: {}", rating);
        } catch (Exception e) {
            logger.error("Error en sincronización de calificación: {}", e.getMessage());
            throw new YouTubeSyncException("Error al sincronizar calificación: " + e.getMessage());
        }
    }
}
