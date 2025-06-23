package org.developers.service.impl;


import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.common.exception.exceptions.CouldNotCreatedVideoException;
import org.developers.common.exception.exceptions.CouldNotDeleteVideoException;
import org.developers.common.exception.exceptions.CouldNotUpdateVideoException;
import org.developers.common.exception.exceptions.VideoNotFoundException;
import org.developers.common.utils.quota.YouTubeQuotaUtil;
import org.developers.model.entities.Video;
import org.developers.model.mapper.VideoMapper;
import org.developers.repository.VideoRepository;
import org.developers.service.interfaces.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final YouTube youtube;
    private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, VideoMapper videoMapper, YouTube youtube) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
        this.youtube = youtube;
    }

    public VideoResponse createVideo(CreateVideoRequest request) {
        logger.info("Iniciando la creación de un video con YouTube ID: {}", request.getYoutubeId());
        VideoListResponse response = null;
        try {
            if (YouTubeQuotaUtil.checkQuotaAvailable()) {
                logger.debug("Verificando la cuota disponible para obtener información del video.");
                YouTube.Videos.List youtubeRequest = youtube.videos().list(List.of("snippet", "contentDetails", "statistics"));
                youtubeRequest.setId(Collections.singletonList(request.getYoutubeId()));

                response = youtubeRequest.execute();
                YouTubeQuotaUtil.incrementQuota(1);
                logger.debug("Información del video obtenida exitosamente.");
            }

            assert response != null;
            if (response.getItems().isEmpty()) {
                logger.warn("Video con YouTube ID {} no encontrado.", request.getYoutubeId());
                throw new VideoNotFoundException("Video de YouTube no encontrado");
            }

            com.google.api.services.youtube.model.Video youtubeVideo = response.getItems().getFirst();
            logger.debug("Creando VideoResponse con la información del video recuperada.");

            VideoResponse videoResponse = VideoResponse.builder().youtubeVideoId(request.getYoutubeId())
                    .title(youtubeVideo.getSnippet().getTitle())
                    .description(youtubeVideo.getSnippet().getDescription())
                    .thumbnailUrl(youtubeVideo.getSnippet().getThumbnails().getDefault().getUrl())
                    .duration(youtubeVideo.getContentDetails().getDuration())
                    .addedAt(LocalDateTime.parse(youtubeVideo.getSnippet().getPublishedAt().toStringRfc3339()))
                    .viewCount(youtubeVideo.getStatistics().getViewCount())
                    .likeCount(youtubeVideo.getStatistics().getLikeCount())
                    .build();

            Video video = videoMapper.toEntity(videoResponse);
            VideoResponse savedResponse = videoMapper.toVideoResponse(videoRepository.save(video));
            logger.info("Video creado con éxito y almacenado en la base de datos.");
            return savedResponse;
        } catch (IOException e) {
            logger.error("Error al crear video con YouTube ID: {}", request.getYoutubeId(), e);
            throw new CouldNotCreatedVideoException("No se pudo crear el video", e);
        }
    }

    @Transactional
    public VideoResponse updateVideo(UpdateVideoRequest request) {
        logger.info("Iniciando actualización del video con Video ID: {}", request.getVideoId());
        Video existingVideo = videoRepository.findById(request.getVideoId())
                .orElseThrow(() -> {
                    logger.warn("Video con ID: {} no encontrado.", request.getVideoId());
                    return new VideoNotFoundException("Video no encontrado");
                });
        com.google.api.services.youtube.model.Video youtubeVideo = null;

        try {
            if (YouTubeQuotaUtil.checkQuotaAvailable()) {
                logger.debug("Verificando cuota disponible para actualizar video en YouTube.");
                YouTube.Videos.Update youTubeRequest = youtube.videos().update(
                        List.of("snippet", "contentDetails", "statistics"),
                        new com.google.api.services.youtube.model.Video()
                                .setId(existingVideo.getYoutubeVideoId())
                                .setSnippet(new VideoSnippet()
                                        .setTitle(request.getTitle())
                                        .setDescription(request.getDescription())
                                        .setPublishedAt(new DateTime(request.getPublishDate().toString()))
                                )
                                .setContentDetails(new VideoContentDetails()
                                        .setDuration(String.valueOf(request.getDuration())))
                                .setStatistics(new VideoStatistics()
                                        .setViewCount(request.getViews())
                                        .setLikeCount(request.getLikes())));

                youtubeVideo = youTubeRequest.execute();
                YouTubeQuotaUtil.incrementQuota(50);
                logger.debug("Video actualizado en YouTube exitosamente.");
            }
            assert youtubeVideo != null;
            if (youtubeVideo.isEmpty()) {
                logger.warn("Video de YouTube con ID {} no encontrado en la plataforma.", existingVideo.getYoutubeVideoId());
                throw new VideoNotFoundException("Video de YouTube no encontrado en la plataforma");
            }
        } catch (IOException e) {
            logger.error("Error al actualizar video con Video ID: {}", request.getVideoId(), e);
            throw new CouldNotUpdateVideoException("No se pudo actualizar el video", e);
        }
        logger.debug("Actualizando información del video en la base de datos.");
        VideoResponse videoResponse = VideoResponse.builder().youtubeVideoId(existingVideo.getYoutubeVideoId())
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .duration(String.valueOf(request.getDuration()))
                .addedAt(request.getPublishDate())
                .viewCount(request.getViews())
                .likeCount(request.getLikes())
                .build();
        videoMapper.updateEntity(existingVideo, videoResponse);
        VideoResponse updatedResponse = videoMapper.toVideoResponse(videoRepository.save(existingVideo));
        logger.info("Video actualizado con éxito en la base de datos.");
        return updatedResponse;
    }

    @Transactional
    public void deleteVideo(DeleteVideoRequest request) {
        logger.info("Iniciando eliminación del video con Video ID: {}", request.getVideoId());
        Video video = videoRepository.findById(request.getVideoId())
                .orElseThrow(() -> {
                    logger.warn("Video con ID {} no encontrado.", request.getVideoId());
                    return new VideoNotFoundException("Video no encontrado");
                });

        try {
            if (YouTubeQuotaUtil.checkQuotaAvailable()) {
                logger.debug("Verificando cuota disponible para eliminar video de YouTube.");
                YouTube.Videos.Delete youTubeRequest = youtube.videos().delete(video.getYoutubeVideoId());
                youTubeRequest.execute();
                YouTubeQuotaUtil.incrementQuota(50);
                logger.debug("Video eliminado de YouTube con éxito.");
            }
        } catch (IOException e) {
            logger.error("Error al eliminar video con Video ID: {}", request.getVideoId(), e);
            throw new CouldNotDeleteVideoException("No se pudo eliminar el video", e);
        }
        videoRepository.delete(video);
        logger.info("Video eliminado con éxito de la base de datos.");
    }

    public Optional<VideoResponse> getVideoByYoutubeId(GetVideoByYoutubeIdRequest request) {
        logger.info("Recuperando video con YouTube ID: {}", request.getYoutubeId());
        return videoRepository.findByYoutubeVideoId(request.getYoutubeId())
                .map(video -> {
                    logger.debug("Video con YouTube ID: {} encontrado.", request.getYoutubeId());
                    return videoMapper.toVideoResponse(video);
                });
    }

    public List<VideoResponse> searchVideosByTitle(SearchVideosByTitleRequest request) {
        logger.info("Realizando búsqueda de videos con título que contiene: {}", request.getTitle());
        List<VideoResponse> videos = videoRepository.findByTitleContainingIgnoreCase(request.getTitle())
                .stream()
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());
        logger.debug("Se encontraron {} videos para el título {}", videos.size(), request.getTitle());
        return videos;
    }

    public PaginatedResponse<VideoResponse> getLastestVideos(GetLastestVideosRequest request) {
        logger.info("Obteniendo los últimos videos (Página: {}, Tamaño: {}).", request.getPage(), request.getSize());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("addedAt").descending());
        Page<Video> videoPage = videoRepository.findAll(pageable);
        logger.debug("Se encontraron {} videos en la página {}.", videoPage.getTotalElements(), request.getPage());
        return createPaginatedResponse(videoPage);
    }

    public PaginatedResponse<VideoResponse> searchVideosWithPagination(SearchVideosWithPaginationRequest request) {
        logger.info("Buscando videos con título que contiene: {} (Página: {}, Tamaño: {}).", request.getTitle(), request.getPage(), request.getSize());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Video> videoPage = videoRepository.findByTitleContainingIgnoreCase(request.getTitle(), pageable);
        logger.debug("Se encontraron {} videos en la página {} para el título {}.", videoPage.getTotalElements(), request.getPage(), request.getTitle());
        return createPaginatedResponse(videoPage);
    }

    public List<VideoResponse> getTrendingVideos(GetTrendingVideosRequest request) {
        logger.info("Obteniendo videos con al menos {} vistas.", request.getMinimumViews());
        List<VideoResponse> videos = videoRepository.findPopularVideos(request.getMinimumViews())
                .stream()
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());
        logger.debug("Se encontraron {} videos populares con al menos {} vistas.", videos.size(), request.getMinimumViews());
        return videos;
    }

    public List<VideoResponse> getMostPopularVideos(GetMostPopularVideosRequest request) {
        logger.info("Obteniendo los {} videos más populares.", request.getLimit());
        Pageable pageable = PageRequest.of(0, request.getLimit(), Sort.by("viewCount").descending());
        List<VideoResponse> videos = videoRepository.findAll(pageable)
                .stream()
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());
        logger.debug("Se encontraron {} videos populares.", videos.size());
        return videos;
    }

    public List<VideoResponse> getVideosAddedInPeriod(GetVideosAddedInPeriodRequest request) {
        logger.info("Obteniendo videos añadidos entre {} y {}.", request.getStartDate(), request.getEndDate());
        List<VideoResponse> videos = videoRepository.findByAddedAtBetween(request.getStartDate(), request.getEndDate())
                .stream()
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());
        logger.debug("Se encontraron {} videos en el rango de fechas especificado.", videos.size());
        return videos;
    }

    public List<VideoResponse> getShortFormVideos(GetShortFormVideosRequest request) {
        logger.info("Obteniendo videos con duración máxima de {}.", request.getMaxDuration());
        Duration maxDurationParsed = Duration.parse(request.getMaxDuration());
        List<VideoResponse> shortVideos = videoRepository.findAll()
                .stream()
                .filter(video -> Duration.parse(video.getDuration()).compareTo(maxDurationParsed) <= 0)
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());
        logger.debug("Se encontraron {} videos cortos con duración máxima de {}.", shortVideos.size(), request.getMaxDuration());
        return shortVideos;
    }

    public VideoStatisticsResponse getVideoStatistics(GetVideoStatisticsRequest request) {
        logger.info("Calculando estadísticas para el video con ID: {}", request.getVideoId());
        List<Video> videos = videoRepository.findById(request.getVideoId()).map(Collections::singletonList).orElse(Collections.emptyList());

        long totalViews = videos.stream().mapToLong(video -> {
            if (video.getViewCount() == null) {
                return 0;
            }
            return video.getViewCount().longValue();
        }).sum();
        double averageDuration = videos.stream()
                .mapToLong(video -> Duration.parse(video.getDuration()).getSeconds())
                .average()
                .orElse(0.0);
        long totalFavorites = videos.stream()
                .mapToLong(video -> video.getFavorites().size())
                .sum();

        logger.debug("Estadísticas calculadas: Total Vistas: {}, Duración Promedio: {}, Total Favoritos: {}.",
                totalViews, averageDuration, totalFavorites);

        return VideoStatisticsResponse.builder()
                .totalViews(totalViews)
                .averageDuration(averageDuration)
                .totalFavorites(totalFavorites)
                .build();
    }

    public Map<String, Long> getVideosByDurationCategories() {
        logger.info("Categorizing videos by duration.");
        Map<String, Long> categories = new HashMap<>();
        videoRepository.findAll().forEach(video -> {
            Duration duration = Duration.parse(video.getDuration());
            String category = categorizeVideoLenght(duration);
            categories.merge(category, 1L, Long::sum);
        });
        logger.debug("Categorías calculadas: {}", categories);
        return categories;
    }

    public boolean isVideoAvailableById(IsVideoAvailableById isVideoAvailableById) {
        logger.info("Verificando si el video con ID {} existe.", isVideoAvailableById.getVideoId());
        boolean exists = videoRepository.existsById(isVideoAvailableById.getVideoId());
        logger.debug("El video con ID {} {} en la base de datos.", isVideoAvailableById.getVideoId(), exists ? "existe" : "no existe");
        return exists;
    }

    public boolean isVideoAvailableByTitle(IsVideoAvailableByTitle isVideoAvailableByTitle) {
        logger.info("Verificando si existe un video con título {}.", isVideoAvailableByTitle.getTitle());
        boolean exists = !videoRepository.findByTitleContainingIgnoreCase(isVideoAvailableByTitle.getTitle()).isEmpty();
        logger.debug("El video con título {} {} en la base de datos.", isVideoAvailableByTitle.getTitle(), exists ? "existe" : "no existe");
        return exists;
    }

    private PaginatedResponse<VideoResponse> createPaginatedResponse(Page<Video> videoPage) {
        logger.debug("Creando respuesta paginada con {} elementos.", videoPage.getTotalElements());
        List<VideoResponse> content = videoPage.getContent()
                .stream()
                .map(videoMapper::toVideoResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<VideoResponse>builder()
                .content(content)
                .currentPage(videoPage.getNumber())
                .totalPages(videoPage.getTotalPages())
                .totalElements(videoPage.getTotalElements())
                .hasNext(videoPage.hasNext())
                .hasPrevious(videoPage.hasPrevious())
                .build();
    }

    private String categorizeVideoLenght(Duration duration) {
        long minutes = duration.toMinutes();
        logger.debug("Categorizing video of {} minutes duration.", minutes);
        if (minutes <= 3) return "CORTO";
        if (minutes <= 10) return "MEDIO";
        if (minutes <= 20) return "LARGO";
        return "MUY_LARGO";
    }
}
