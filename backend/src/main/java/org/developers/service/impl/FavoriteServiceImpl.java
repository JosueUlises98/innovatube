package org.developers.service.impl;

import lombok.extern.log4j.Log4j2;
import org.developers.api.request.Favorite.*;
import org.developers.api.request.User.IsUserNameAvailable;
import org.developers.api.request.Video.IsVideoAvailableById;
import org.developers.api.response.Favorite.PaginatedVideoFavorites;
import org.developers.common.exception.exceptions.*;
import org.developers.model.dto.FavoriteVideo;
import org.developers.model.entities.Favorite;
import org.developers.model.mapper.FavoriteMapper;
import org.developers.repository.FavoriteRepository;
import org.developers.service.interfaces.FavoriteService;
import org.developers.service.interfaces.UserService;
import org.developers.service.interfaces.VideoService;
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
@Log4j2
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserService userService;
    private final VideoService videoService;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper, UserService userService, VideoService videoService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.userService = userService;
        this.videoService = videoService;
        log.info("FavoriteService inicializado");
    }

    public List<org.developers.api.response.Favorite.FavoriteVideo> getUserFavoriteVideos(GetUserFavoriteVideosRequest request) {
        log.info("Obteniendo videos favoritos para usuario: {}", request.getUserId());
        List<FavoriteVideo> favoriteVideos = this.favoriteRepository.findByUserId(request.getUserId());
        if (favoriteVideos.isEmpty()) {
            throw new UserFavoriteVideosException(String.format("No hay favoritos para el usuario %s", request.getUserId()));
        } else {
            return this.favoriteMapper.toResponseList(favoriteVideos);
        }
    }

    public boolean hasUserFavoritedVideo(HasUserFavoritedVideo request) {
        log.info("Verificando si el usuario {} tiene como favorito el video {}", request.getUserId(), request.getVideoId());
        return this.favoriteRepository.existsByUserIdAndVideoId(request.getUserId(), request.getVideoId());
    }

    public PaginatedVideoFavorites getUserFavoritesPaginated(GetUserFavoritesPaginated request) {
        log.info("Obteniendo favoritos paginados para usuario: {}, página: {}, tamaño: {}", request.getUserId(), request.getPage(), request.getSize());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<FavoriteVideo> favoriteVideos = this.favoriteRepository.findByUserId(request.getUserId(), pageable);
        if (favoriteVideos.isEmpty()) {
            throw new UserFavoritesPaginatedException(String.format("No hay favoritos para el usuario %s", request.getUserId()));
        } else {
            return PaginatedVideoFavorites.builder().favorites(this.favoriteMapper.toResponseList(favoriteVideos.getContent())).currentPage(favoriteVideos.getNumber()).totalPages(favoriteVideos.getTotalPages()).totalElements(favoriteVideos.getTotalElements()).build();
        }
    }

    @Transactional
    public void addVideoToFavorites(AddVideoToFavorites request) {
        IsUserNameAvailable avaibleRequest = new IsUserNameAvailable();
        avaibleRequest.setUserId(request.getUserId());
        if (!this.userService.isUserNameAvailableById(avaibleRequest)) {
            log.error("Usuario no encontrado con ID: {}", request.getUserId());
            throw new UserNotFoundException(String.format("Usuario no encontrado con ID: %s", request.getUserId()));
        } else {
            IsVideoAvailableById videoAvailableRequest = new IsVideoAvailableById();
            videoAvailableRequest.setVideoId(request.getVideoId());
            if (!this.videoService.isVideoAvailableById(videoAvailableRequest)) {
                log.error("Video no encontrado con ID: {}", request.getVideoId());
                throw new VideoNotFoundException(String.format("Video no encontrado con ID: %s", request.getVideoId()));
            } else {
                log.info("Agregando video {} a favoritos del usuario {}", request.getVideoId(), request.getUserId());
                HasUserFavoritedVideo hasUserFavoritedVideoRequest = new HasUserFavoritedVideo();
                hasUserFavoritedVideoRequest.setUserId(request.getUserId());
                hasUserFavoritedVideoRequest.setVideoId(request.getVideoId());
                if (this.hasUserFavoritedVideo(hasUserFavoritedVideoRequest)) {
                    log.warn("El video ya está en favoritos para el usuario");
                    throw new FavoritedVideoExistsException(String.format("Video ya existe para el usuario %s", request.getUserId()));
                } else {
                    try {
                        Favorite favorite = Favorite.builder().userId(request.getUserId()).videoId(request.getVideoId()).addedAt(LocalDateTime.now()).build();
                        this.favoriteRepository.save(favorite);
                        log.info("Video agregado exitosamente a favoritos");
                    } catch (Exception e) {
                        log.error("Error al agregar video a favoritos: {}", e.getMessage());
                        throw new AddFailedException(String.format("Error al agregar video a favoritos: %s", e.getMessage()));
                    }
                }
            }
        }
    }

    @Transactional
    public void removeVideoFromFavorites(RemoveVideoFromFavorites request) {
        log.info("Removiendo video {} de favoritos del usuario {}", request.getVideoId(), request.getUserId());
        if (this.favoriteRepository.existsByUserIdAndVideoId(request.getUserId(), request.getVideoId())) {
            Optional<FavoriteVideo> favoriteVideo = this.favoriteRepository.findByUserIdAndVideoId(request.getUserId(), request.getVideoId());

            try {
                Favorite entity = this.favoriteMapper.favoriteVideoToEntity(favoriteVideo.orElseThrow());
                this.favoriteRepository.delete(entity);
                log.info("Video removido exitosamente de favoritos");
            } catch (Exception e) {
                log.error("Error al remover video de favoritos: {}", e.getMessage());
                throw new RemoveFailedException(String.format("Error al remover video de favoritos: %s", e.getMessage()));
            }
        }
    }

    public List<org.developers.api.response.Favorite.FavoriteVideo> getTrendingFavoriteVideos(GetTrendingFavoriteVideos request) {
        log.info("Obteniendo videos favoritos en tendencia");
        List<FavoriteVideo> favoriteVideos = this.favoriteRepository.findTrendingFavorites(request.getUserId(), LocalDateTime.now().minusDays(3L));
        if (favoriteVideos.isEmpty()) {
            throw new TrendingFavoritedVideosNotFoundException(String.format("No hay videos de tendencia para el usuario %s", request.getUserId()));
        } else {
            return this.favoriteMapper.toResponseList(favoriteVideos);
        }
    }
}
