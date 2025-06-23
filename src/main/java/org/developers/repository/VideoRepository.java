package org.developers.repository;

import org.developers.model.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    // Búsquedas básicas
    Optional<Video> findByYoutubeVideoId(String youtubeVideoId);
    List<Video> findByTitleContainingIgnoreCase(String title);

    // Búsquedas con paginación
    Page<Video> findAllByOrderByAddedAtDesc(Pageable pageable);
    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Consultas personalizadas
    @Query("SELECT v FROM Video v WHERE v.viewCount > :minViews")
    List<Video> findPopularVideos(@Param("minViews") Integer minViews);

    @Query("SELECT v FROM Video v LEFT JOIN v.favorites f " +
            "GROUP BY v.videoId, v.youtubeVideoId, v.title, v.description, " +
            "v.thumbnailUrl, v.duration, v.addedAt, v.viewCount " +
            "ORDER BY COUNT(f) DESC")
    List<Video> findMostFavoritedVideos(Pageable pageable);

    // Búsqueda por período
    List<Video> findByAddedAtBetween(LocalDateTime start, LocalDateTime end);

    // Búsqueda por duración
    @Query("SELECT v FROM Video v WHERE v.duration <= :maxDuration")
    List<Video> findByMaxDuration(@Param("maxDuration") String maxDuration);

    // Estadísticas
    @Query("SELECT COUNT(v) FROM Video v WHERE v.addedAt >= :since")
    Long countVideosAddedSince(@Param("since") LocalDateTime since);

}
