package org.developers.service.impl;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.Video.*;
import org.developers.api.response.Video.PaginatedResponse;
import org.developers.api.response.Video.VideoResponse;
import org.developers.api.response.Video.VideoStatisticsResponse;
import org.developers.api.response.YouTube.*;
import org.developers.common.exception.exceptions.*;
import org.developers.common.utils.datetime.DateTimeUtils;
import org.developers.common.utils.quota.YouTubeQuotaUtil;
import org.developers.model.mapper.VideoMapper;
import org.developers.repository.VideoRepository;
import org.developers.service.interfaces.VideoService;
import org.developers.service.interfaces.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final YouTube youtube;
    private final YouTubeService youTubeService;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, VideoMapper videoMapper, YouTube youtube, YouTubeService youTubeService) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
        this.youtube = youtube;
        this.youTubeService = youTubeService;
    }

    @Transactional
    public VideoResponse createVideo(CreateVideo request) {
        log.info("Iniciando la creación de un video con YouTube ID: {}", request.getYoutubeId());
        if (videoRepository.existsByYoutubeVideoIdAndUserId(request.getYoutubeId(), request.getUserId())) {
            throw new VideoAlreadyExistsException("El video ya existe en la base de datos");
        } else {
            try {
                log.debug("Verificando la cuota disponible para obtener información del video.");
                if (!YouTubeQuotaUtil.checkQuotaAvailable()) {
                    log.warn("No hay disponible la cuota para crear video en YouTube.");
                    throw new CouldNotCreatedVideoException("No hay disponible la cuota para crear video en YouTube");
                } else {
                    YouTube.Videos.List youtubeRequest = youtube.videos().list(List.of("snippet", "contentDetails", "statistics"));
                    youtubeRequest.setId(Collections.singletonList(request.getYoutubeId()));
                    VideoListResponse response = youtubeRequest.execute();
                    YouTubeQuotaUtil.incrementQuota(1);
                    log.debug("Información del video obtenida exitosamente.");

                    assert response != null;

                    if (response.getItems().isEmpty()) {
                        log.warn("Video con YouTube ID {} no encontrado.", request.getYoutubeId());
                        throw new VideoNotFoundException("Video de YouTube no encontrado");
                    } else {
                        Video youtubeVideo = response.getItems().getFirst();
                        log.debug("Creando VideoResponse con la información del video recuperada.");
                        VideoResponse videoResponse = VideoResponse.builder().youtubeVideoId(request.getYoutubeId())
                                .title(youtubeVideo.getSnippet().getTitle()).description(youtubeVideo.getSnippet()
                                        .getDescription()).thumbnailUrl(youtubeVideo.getSnippet().getThumbnails()
                                        .getDefault().getUrl()).duration(DateTimeUtils.convertYouTubeDuration(youtubeVideo.getContentDetails()
                                        .getDuration())).addedAt(DateTimeUtils.toLocalDateTime(youtubeVideo.getSnippet().getPublishedAt()))
                                .viewCount(youtubeVideo.getStatistics().getViewCount())
                                .likeCount(youtubeVideo.getStatistics().getLikeCount() != null ? youtubeVideo.getStatistics().getLikeCount() : BigInteger.ZERO)
                                .userId(request.getUserId()).build();
                        org.developers.model.entities.Video video = videoMapper.toEntity(videoResponse);
                        log.info("Verificando los likes mapeados {}", video.getLikes());
                        VideoResponse savedResponse = this.videoMapper.toVideoResponse(videoRepository.save(video));
                        log.info("Video creado con éxito y almacenado en la base de datos.");
                        return savedResponse;
                    }
                }
            } catch (IOException e) {
                log.error("Error al crear video con YouTube ID: {}", request.getYoutubeId(), e);
                throw new CouldNotCreatedVideoException("No se pudo crear el video", e);
            }
        }
    }

    @Transactional
    public List<org.developers.api.response.Video.SearchResult> searchYoutubeVideos(SearchVideos searchVideosRequest) throws IOException {
        log.debug("Buscando videos de youtube");
        if (!YouTubeQuotaUtil.checkQuotaAvailable()) {
            log.warn("No hay disponible la cuota para buscar video en YouTube.");
            throw new CouldNotCreatedVideoException("No hay disponible la cuota para crear video en YouTube");
        } else {
            YouTube.Search.List request = youtube.search().list(List.of("id", "snippet"));
            request.setQ(searchVideosRequest.getSearch());
            request.setType(Collections.singletonList("video"));
            request.setOrder("relevance");
            request.setRegionCode(searchVideosRequest.getRegionCode());
            request.setRelevanceLanguage(searchVideosRequest.getLanguageCode());
            request.setMaxResults(50L);
            SearchListResponse searchResponseYoutube = request.execute();
            YouTubeQuotaUtil.incrementQuota(1);
            log.debug("Busqueda de videos realizada exitosamente.");

            assert searchResponseYoutube != null;

            if (searchResponseYoutube.getItems().isEmpty()) {
                log.warn("Videos con YouTube ID {} no encontrados: ", (searchResponseYoutube.getItems().getFirst()).getId().getVideoId());
                throw new VideoNotFoundException("Videos de YouTube no encontrados");
            } else {
                Iterator<SearchResult> iterator = searchResponseYoutube.getItems().iterator();
                YouTube.Videos.List propertiesRequest = this.youtube.videos().list(List.of("snippet", "contentDetails", "statistics"));
                propertiesRequest.setId(searchResponseYoutube.getItems().stream().map((searchResult) -> searchResult.getId().getVideoId()).collect(Collectors.toList()));
                VideoListResponse videoListResponse = propertiesRequest.execute();
                YouTubeQuotaUtil.incrementQuota(1);
                log.debug("Información de videos obtenida exitosamente.");

                assert videoListResponse != null;

                if (videoListResponse.getItems().isEmpty()) {
                    log.warn("Videos de YouTube no encontrados");
                    throw new VideoNotFoundException("Video de YouTube no encontrado");
                } else {
                    List<Video> items = videoListResponse.getItems();
                    log.debug("Se encontraron {} videos relacionados.", items.size());
                    return items.stream().map((searchResult) -> org.developers.api.response.Video.SearchResult.builder()
                            .id("1")
                            .youtubeVideoId((iterator.next()).getId().getVideoId())
                            .title(searchResult.getSnippet().getTitle())
                            .description(searchResult.getSnippet().getDescription())
                            .thumbnailUrl(searchResult.getSnippet().getThumbnails().getDefault().getUrl())
                            .duration(searchResult.getContentDetails().getDuration())
                            .addedAt(String.valueOf(searchResult.getSnippet().getPublishedAt()))
                            .viewCount(searchResult.getStatistics().getViewCount())
                            .likeCount(searchResult.getStatistics().getLikeCount()).build())
                            .collect(Collectors.toList());
                }
            }
        }
    }

    public Optional<VideoResponse> getVideoByYoutubeId(GetVideoByYoutubeId request) {
        log.info("Recuperando video con YouTube ID: {}", request.getYoutubeId());
        return videoRepository.findByYoutubeVideoIdAndUserId(request.getYoutubeId(), request.getUserId()).map((video) -> {
            log.debug("Video con YouTube ID: {} encontrado.", request.getYoutubeId());
            return videoMapper.toVideoResponse(video);
        });
    }

    public List<VideoResponse> searchVideosByTitle(SearchVideosByTitle request) {
        if (request.getTitle() != null && !request.getTitle().isBlank() && !request.getTitle().matches("^[a-zA-Z0-9._-]+$")) {
            log.info("Realizando búsqueda de videos con título que contiene: {}", request.getTitle());
            List<org.developers.model.entities.Video> var10000 = videoRepository.findByTitleContainingIgnoreCaseAndUserId(request.getTitle(), request.getUserId());
            List<VideoResponse> videos = var10000.stream().map(videoMapper::toVideoResponse).collect(Collectors.toList());
            log.debug("Se encontraron {} videos para el título {}", videos.size(), request.getTitle());
            return videos;
        } else {
            throw new InvalidCriteriaException("Criterios invalidos");
        }
    }

    public PaginatedResponse<VideoResponse> getLastestVideos(GetLastestVideos request) {
        if (request.getPage() >= 0 && request.getSize() >= 0) {
            log.info("Obteniendo los últimos videos (Página: {}, Tamaño: {}).", request.getPage(), request.getSize());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("addedAt").descending());
            Page<org.developers.model.entities.Video> videoPage = videoRepository.findByUserIdOrderByAddedAtDesc(request.getUserId(), pageable);
            log.debug("Se encontraron {} videos en la página {}.", videoPage.getTotalElements(), request.getPage());
            return createPaginatedResponse(videoPage);
        } else {
            log.error("Parámetros de paginación inválidos: página = {}, tamaño = {}", request.getPage(), request.getSize());
            throw new InvalidPaginationException("Los parámetros de paginación no pueden ser negativos.");
        }
    }

    public PaginatedResponse<VideoResponse> searchVideosWithPagination(SearchVideosWithPagination request) {
        log.info("Buscando videos con título que contiene: {} (Página: {}, Tamaño: {}).", request.getTitle(), request.getPage(), request.getSize());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<org.developers.model.entities.Video> videoPage = videoRepository.findByTitleContainingIgnoreCaseAndUserId(request.getTitle(), request.getUserId(), pageable);
        log.debug("Se encontraron {} videos en la página {} para el título {}.", videoPage.getTotalElements(), request.getPage(), request.getTitle());
        return this.createPaginatedResponse(videoPage);
    }

    public List<VideoResponse> getTrendingVideos(GetTrendingVideos request) {
        if (request.getMinimumViews() < 0) {
            throw new InvalidCriteriaException("Minimum views cannot be negative.");
        } else {
            log.info("Obteniendo videos con al menos {} vistas.", request.getMinimumViews());
            List<org.developers.model.entities.Video> var10000;
            if (request.getUserId() != null) {
                var10000 = videoRepository.findPopularVideosByUser(request.getUserId(), request.getMinimumViews());
            } else {
                var10000 = videoRepository.findPopularVideosGlobal(request.getMinimumViews());
            }
            List<VideoResponse> videos = var10000.stream().map(videoMapper::toVideoResponse).collect(Collectors.toList());
            log.debug("Se encontraron {} videos populares con al menos {} vistas.", videos.size(), request.getMinimumViews());
            return videos;
        }
    }

    public List<VideoResponse> getMostPopularVideos(GetMostPopularVideos request) {
        log.info("Obteniendo los {} videos más populares.", request.getLimit());
        Pageable pageable = PageRequest.of(0, request.getLimit(), Sort.by("viewCount").descending());
        Page<org.developers.model.entities.Video> var10000 = videoRepository.findAll(pageable);
        List<VideoResponse> videos = var10000.stream().map(videoMapper::toVideoResponse).collect(Collectors.toList());
        log.debug("Se encontraron {} videos populares.", videos.size());
        return videos;
    }

    public List<VideoResponse> getVideosAddedInPeriod(GetVideosAddedInPeriod request) {
        log.info("Obteniendo videos añadidos entre {} y {}.", request.getStartDate(), request.getEndDate());
        List<org.developers.model.entities.Video> var10000 = videoRepository.findByUserIdAndAddedAtBetween(request.getUserId(), request.getStartDate(), request.getEndDate());
        List<VideoResponse> videos = var10000.stream().map(videoMapper::toVideoResponse).collect(Collectors.toList());
        log.debug("Se encontraron {} videos en el rango de fechas especificado.", videos.size());
        return videos;
    }

    public List<VideoResponse> getShortFormVideos(GetShortFormVideos request) {
        log.info("Obteniendo videos con duración máxima de {}.", request.getMaxDuration());
        Duration maxDurationParsed = Duration.parse(request.getMaxDuration());
        List<VideoResponse> var10000 = this.videoRepository.findAll().stream().filter((video) -> video.getDuration().compareTo(maxDurationParsed) <= 0)
                        .map(videoMapper::toVideoResponse)
                                .collect(Collectors.toList());
        log.debug("Se encontraron {} videos cortos con duración máxima de {}.", var10000.size(), request.getMaxDuration());
        return var10000;
    }

    public VideoStatisticsResponse getVideoStatistics(GetVideoStatistics request) {
        log.info("Calculando estadísticas para el video con ID: {}", request.getVideoId());
        List<org.developers.model.entities.Video> videos = videoRepository.findById(request.getVideoId()).map(Collections::singletonList).orElse(Collections.emptyList());
        if (videos.isEmpty()) {
            throw new ResourceNotFoundException("Video no encontrado para el ID: " + request.getVideoId() + ".");
        } else {
            long totalViews = videos.stream().mapToLong((video) -> video.getViewCount() == null ? 0L : video.getViewCount().longValue()).sum();
            double averageDuration = videos.stream().mapToLong((video) -> DateTimeUtils.convertirDuracion(video.getDuration().toString())).average().orElse(0.0F);
            if (totalViews == 0L && averageDuration == (double)0.0F) {
                throw new ResourceNotFoundException("Estadisticas no encontradas para el ID: " + request.getVideoId() + ".");
            } else {
                log.debug("Estadísticas calculadas: Total Vistas: {}, Duración Promedio: {}", totalViews, averageDuration);
                return VideoStatisticsResponse.builder().totalViews(totalViews).averageDuration(averageDuration).build();
            }
        }
    }

    public Map<String, Long> getVideosByDurationCategories() {
        log.info("Categorizing videos by duration.");
        Map<String, Long> categories = new HashMap<>();
        this.videoRepository.findAll().forEach((video) -> {
            String category = categorizeVideoLenght(video.getDuration());
            categories.merge(category, 1L, Long::sum);
        });
        log.debug("Categorías calculadas: {}", categories);
        return categories;
    }

    public boolean isVideoAvailableById(IsVideoAvailableById isVideoAvailableById) {
        log.info("Verificando si el video con ID {} existe.", isVideoAvailableById.getVideoId());
        boolean exists = videoRepository.existsById(isVideoAvailableById.getVideoId());
        log.debug("El video con ID {} {} en la base de datos.", isVideoAvailableById.getVideoId(), exists ? "existe" : "no existe");
        return exists;
    }

    public boolean isVideoAvailableByTitle(IsVideoAvailableByTitle isVideoAvailableByTitle) {
        log.info("Verificando si existe un video con título {}.", isVideoAvailableByTitle.getTitle());
        boolean exists = !this.videoRepository.findByTitleContainingIgnoreCaseAndUserId(isVideoAvailableByTitle.getTitle(), isVideoAvailableByTitle.getUserId()).isEmpty();
        log.debug("El video con título {} {} en la base de datos.", isVideoAvailableByTitle.getTitle(), exists ? "existe" : "no existe");
        return exists;
    }

    private PaginatedResponse<VideoResponse> createPaginatedResponse(Page<org.developers.model.entities.Video> videoPage) {
        log.debug("Creando respuesta paginada con {} elementos.", videoPage.getTotalElements());
        List<org.developers.model.entities.Video> var10000 = videoPage.getContent();
        List<VideoResponse> content = var10000.stream().map(videoMapper::toVideoResponse).toList();
        return PaginatedResponse.builder()
                .content(content)
                .currentPage(videoPage.getNumber())
                .totalPages(videoPage.getTotalPages())
                .totalElements(videoPage.getTotalElements())
                .hasNext(videoPage.hasNext())
                .hasPrevious(videoPage.hasPrevious())
                .build();
    }

    private String categorizeVideoLenght(Duration duration) {
        if (duration.toMinutes() <= 5) {
            return "Short";
        } else if (duration.toMinutes() <= 20) {
            return "Medium";
        } else {
            return "Long";
        }
    }

    // Updated YouTube methods to use YouTubeService
    public YouTubeSearchResult searchYouTubeVideos(String query, int maxResults, String regionCode) {
        log.info("Buscando videos de YouTube: {}", query);
        return youTubeService.searchVideos(query, maxResults, regionCode);
    }

    public Optional<YouTubeVideoDetails> getYouTubeVideoDetails(String videoId) {
        log.info("Obteniendo detalles del video de YouTube: {}", videoId);
        return youTubeService.getVideoDetails(videoId);
    }

    public List<YouTubeVideoDetails> getYouTubeTrendingVideos(String regionCode, String categoryId) {
        log.info("Obteniendo videos trending de YouTube para región: {}", regionCode);
        return youTubeService.getTrendingVideos(regionCode, categoryId, 50);
    }

    public List<YouTubeCategory> getYouTubeVideoCategories(String regionCode) {
        log.info("Obteniendo categorías de videos de YouTube para región: {}", regionCode);
        return youTubeService.getVideoCategories(regionCode);
    }

    public List<YouTubePlaylist> getYouTubePlaylists(String query, int maxResults) {
        log.info("Buscando playlists de YouTube: {}", query);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public List<YouTubeVideoDetails> getYouTubePlaylistItems(String playlistId, int maxResults) {
        log.info("Obteniendo videos de playlist de YouTube: {}", playlistId);
        return youTubeService.getPlaylistVideos(playlistId, maxResults);
    }

    public List<YouTubeVideoDetails> getYouTubeChannelVideos(String channelId, int maxResults) {
        log.info("Obteniendo videos del canal de YouTube: {}", channelId);
        return youTubeService.getChannelVideos(channelId, maxResults);
    }

    public List<YouTubeComment> getYouTubeVideoComments(String videoId, int maxResults) {
        log.info("Obteniendo comentarios del video de YouTube: {}", videoId);
        return youTubeService.getVideoComments(videoId, maxResults);
    }

    public Optional<YouTubeVideoStatistics> getYouTubeVideoStatistics(String videoId) {
        log.info("Obteniendo estadísticas del video de YouTube: {}", videoId);
        return youTubeService.getVideoStatistics(videoId);
    }

    public Optional<YouTubeChannelDetails> getYouTubeChannelDetails(String channelId) {
        log.info("Obteniendo detalles del canal de YouTube: {}", channelId);
        return youTubeService.getChannelDetails(channelId);
    }

    public List<String> getYouTubeSearchSuggestions(String query) {
        log.info("Obteniendo sugerencias de búsqueda de YouTube: {}", query);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public List<YouTubeVideoDetails> getYouTubeRelatedVideos(String videoId, int maxResults) {
        log.info("Obteniendo videos relacionados de YouTube: {}", videoId);
        return youTubeService.getRelatedVideos(videoId, maxResults);
    }

    public List<YouTubeCaption> getYouTubeVideoCaptions(String videoId) {
        log.info("Obteniendo subtítulos del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public YouTubeThumbnails getYouTubeVideoThumbnails(String videoId) {
        log.info("Obteniendo miniaturas del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new YouTubeThumbnails();
    }

    public List<String> getYouTubeVideoQualityOptions(String videoId) {
        log.info("Obteniendo opciones de calidad del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public List<YouTubeSubtitle> getYouTubeVideoSubtitles(String videoId) {
        log.info("Obteniendo subtítulos del video de YouTube.: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public List<YouTubeChapter> getYouTubeVideoChapters(String videoId) {
        log.info("Obteniendo capítulos del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new ArrayList<>();
    }

    public YouTubeVideoAnalytics getYouTubeVideoAnalytics(String videoId) {
        log.info("Obteniendo analytics del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new YouTubeVideoAnalytics();
    }

    public List<YouTubeVideoDetails> getYouTubeVideoRecommendations(String videoId, int maxResults) {
        log.info("Obteniendo recomendaciones del video de YouTube: {}", videoId);
        return youTubeService.getRelatedVideos(videoId, maxResults);
    }

    public List<YouTubeVideoDetails> getYouTubeSimilarVideos(String videoId, int maxResults) {
        log.info("Obteniendo videos similares de YouTube: {}", videoId);
        return youTubeService.getRelatedVideos(videoId, maxResults);
    }

    public String getYouTubeVideoTranscript(String videoId) {
        log.info("Obteniendo transcripción del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return "";
    }

    public YouTubeVideoMetadata getYouTubeVideoMetadata(String videoId) {
        log.info("Obteniendo metadata del video de YouTube: {}", videoId);
        // This would need to be implemented in YouTubeService
        return new YouTubeVideoMetadata();
    }
}
