package org.developers.repository;

import org.developers.api.response.Favorite.FavoriteVideoStatistics;
import org.developers.model.entities.Favorite;
import org.developers.model.entities.User;
import org.developers.model.entities.Video;
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

    // Consultas básicas
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndVideoId(Long userId,Long videoId);

    // Obtener youtubevideoid de videos favoritos de un usuario
    @Query("SELECT v.youtubeVideoId FROM Video v " +
            "JOIN Favorite f ON f.videoId = v.videoId " +
            "WHERE f.userId = :userId")
    List<String> findYoutubeVideoIdsByUserId(@Param("userId") Long userId);

    List<Favorite> findByVideoId(Long videoId);
    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    // Verificación de existencia
    boolean existsByUserIdAndVideoId(Long userId, Long videoId);

    // Eliminación
    @Modifying
    @Transactional
    void deleteByUserIdAndVideoId(Long userId, Long videoId);

    // Videos recientes
    @Query("SELECT f FROM Favorite f " +
            "WHERE f.addedAt >= :since " +
            "ORDER BY f.addedAt DESC")
    List<Favorite> findRecentlyFavorited(
            @Param("since") LocalDateTime since,
            Pageable pageable
    );

    // Videos en tendencia
    @Query("SELECT f FROM Favorite f " +
            "WHERE f.addedAt >= :since " +
            "GROUP BY f.videoId " +
            "ORDER BY COUNT(f) DESC")
    List<Favorite> findTrendingVideos(
            @Param("since") LocalDateTime since,
            Pageable pageable
    );

    // Estadísticas
    @Query("SELECT new org.developers.api.response.Favorite.FavoriteVideoStatistics(" +
            "COUNT(f), " +
            "MIN(f.addedAt), " +
            "MAX(f.addedAt)) " +
            "FROM Favorite f " +
            "WHERE f.videoId = :videoId")
    FavoriteVideoStatistics calculateStatistics(@Param("videoId") Long videoId);

    // Más favoritos por período
    @Query("SELECT f FROM Favorite f " +
            "WHERE f.addedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY f.videoId " +
            "ORDER BY COUNT(f) DESC")
    List<Favorite> findMostFavoritedInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // Videos similares
    @Query("SELECT f2 FROM Favorite f1 " +
            "JOIN Favorite f2 ON f1.userId = f2.userId " +
            "WHERE f1.videoId = :videoId " +
            "AND f2.videoId != :videoId " +
            "GROUP BY f2.videoId " +
            "ORDER BY COUNT(f2) DESC")
    List<Favorite> findSimilarVideos(
            @Param("videoId") Long videoId,
            Pageable pageable
    );

    // Tendencias por hora del día
    @Query("SELECT FUNCTION('HOUR', f.addedAt) as hora, " +
            "COUNT(f) as cantidad " +
            "FROM Favorite f " +
            "WHERE DATE(f.addedAt) = DATE(:date) " +
            "GROUP BY FUNCTION('HOUR', f.addedAt) " +
            "ORDER BY hora")
    List<Object[]> analyzeTrendsByTimeOfDay(@Param("date") LocalDateTime date);

    // Usuarios con gustos similares
    @Query("SELECT f2 FROM Favorite f1 " +
            "JOIN Favorite f2 ON f1.videoId = f2.videoId " +
            "WHERE f1.userId = :userId " +
            "AND f2.userId != :userId " +
            "GROUP BY f2.userId " +
            "HAVING COUNT(DISTINCT f1.videoId) >= :minimumCommon " +
            "ORDER BY COUNT(DISTINCT f1.videoId) DESC")
    List<Favorite> findUsersWithSimilarTastes(
            @Param("userId") Long userId,
            @Param("minimumCommon") Long minimumCommon,
            Pageable pageable
    );
    //Obtener los videos mas recientes favoritos por userId
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId ORDER BY f.createdAt DESC")
    List<Favorite> findRecentFavoritesByUserId(@Param("userId") Long userId);

    //Trending Videos
    @Query("SELECT DISTINCT f FROM Favorite f " +
            "JOIN Video v ON f.videoId = v.videoId " +
            "WHERE f.userId = :userId " +
            "AND f.addedAt >= :oneHourAgo " +
            "AND v.likes > 1000 " +
            "AND v.viewCount > 1000 " +
            "ORDER BY v.viewCount DESC")
    List<Favorite> findTrendingFavorites(
            @Param("userId") Long userId,
            @Param("oneHourAgo") LocalDateTime oneHourAgo
    );

}
