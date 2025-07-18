package org.developers.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.developers.model.dto.FavoriteVideo;
import org.developers.model.entities.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT NEW org.developers.model.dto.FavoriteVideo(f.userId, f.videoId, f.addedAt, v.title, v.youtubeVideoId, v.description, v.thumbnailUrl, v.duration, v.viewCount, v.likes) FROM Video v JOIN Favorite f ON v.videoId = f.videoId WHERE f.userId = :userId ORDER BY f.addedAt DESC")
    List<FavoriteVideo> findByUserId(@Param("userId") Long userId);

    @Query("SELECT NEW org.developers.model.dto.FavoriteVideo(f.userId, f.videoId, f.addedAt, v.title, v.youtubeVideoId, v.description, v.thumbnailUrl, v.duration, v.viewCount, v.likes) FROM Video v JOIN Favorite f ON v.videoId = f.videoId WHERE f.userId = :userId AND f.videoId = :videoId")
    Optional<FavoriteVideo> findByUserIdAndVideoId(@Param("userId") Long userId, @Param("videoId") Long videoId);

    @Query("SELECT NEW org.developers.model.dto.FavoriteVideo(f.userId, f.videoId, f.addedAt, v.title, v.youtubeVideoId, v.description, v.thumbnailUrl, v.duration, v.viewCount, v.likes) FROM Video v JOIN Favorite f ON v.videoId = f.videoId WHERE f.userId = :userId ORDER BY f.addedAt DESC")
    Page<FavoriteVideo> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.userId = :userId AND f.videoId = :videoId")
    boolean existsByUserIdAndVideoId(@Param("userId") Long userId, @Param("videoId") Long videoId);

    @Modifying(
            clearAutomatically = true
    )
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.userId = :userId AND f.videoId = :videoId")
    void deleteByUserIdAndVideoId(@Param("userId") Long userId, @Param("videoId") Long videoId);

    @Query("SELECT NEW org.developers.model.dto.FavoriteVideo(f.userId, f.videoId, f.addedAt, v.title, v.youtubeVideoId, v.description, v.thumbnailUrl, v.duration, v.viewCount, v.likes) FROM Video v JOIN Favorite f ON v.videoId = f.videoId WHERE f.userId = :userId AND f.addedAt >= :dateTime AND v.likes > 1000 AND v.viewCount > 1000 ORDER BY f.addedAt DESC")
    List<FavoriteVideo> findTrendingFavorites(@Param("userId") @NotNull(
            message = "User ID cannot be null"
    ) @Positive(
            message = "User ID must be a positive number"
    ) Long userId, @Param("dateTime") @NotNull LocalDateTime dateTime);

    @Query("SELECT v.youtubeVideoId FROM Video v JOIN Favorite f ON v.videoId = f.videoId WHERE f.userId = :userId")
    List<String> findYoutubeVideoIdsByUserId(@Param("userId") Long userId);
}
