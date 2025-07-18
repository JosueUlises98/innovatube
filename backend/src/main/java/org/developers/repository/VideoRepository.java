package org.developers.repository;

import org.developers.model.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    Optional<Video> findByYoutubeVideoIdAndUserId(String youtubeVideoId,Long userId);

    List<Video> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId);

    Page<Video> findByUserIdOrderByAddedAtDesc(Long userId, Pageable pageable);

    Page<Video> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId, Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.userId = :userId AND v.videoId NOT IN (SELECT f.videoId FROM Favorite f WHERE f.userId = :userId)")
    Page<Video> findNotFavoritedByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.userId = :userId AND v.viewCount > :minViews")
    List<Video> findPopularVideosByUser(@Param("userId") Long userId, @Param("minViews") Integer minViews);

    @Query("SELECT v FROM Video v WHERE v.viewCount > :minViews")
    List<Video> findPopularVideosGlobal(@Param("minViews") Integer minViews);

    @Query("SELECT v FROM Video v WHERE v.userId = :userId AND v.addedAt BETWEEN :start AND :end")
    List<Video> findByUserIdAndAddedAtBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT v FROM Video v WHERE v.userId = :userId AND v.duration <= :maxDuration")
    List<Video> findByUserIdAndMaxDuration(@Param("userId") Long userId, @Param("maxDuration") Duration maxDuration);

    boolean existsByYoutubeVideoIdAndUserId(String youtubeVideoId, Long userId);
}
