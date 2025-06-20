package org.developers.repository;

import org.developers.model.entities.Favorite;
import org.developers.model.entities.User;
import org.developers.model.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Búsquedas básicas
    List<Favorite> findByUserId(User user);
    List<Favorite> findByVideoId(Video video);

    // Verificación de favoritos
    boolean existsByUserIdAndVideoId(User user, Video video);

    // Búsquedas con paginación
    Page<Favorite> findByUserId(User user, Pageable pageable);

    // Consultas personalizadas
    @Query("SELECT f FROM Favorite f WHERE f.userId = :user " +
            "ORDER BY f.addedAt DESC")
    List<Favorite> findRecentFavorites(@Param("user") User user,
                                       Pageable pageable);

    // Estadísticas
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.addedAt >= :since")
    Long countFavoritesAddedSince(@Param("since") LocalDateTime since);

    @Query("SELECT f.videoId, COUNT(f) as count FROM Favorite f " +
            "GROUP BY f.videoId ORDER BY count DESC")
    List<Object[]> findMostFavoritedVideos(Pageable pageable);

    // Eliminar favoritos
    void deleteByUserIdAndVideoId(User user, Video video);

    // Búsqueda por período
    List<Favorite> findByAddedAtBetween(LocalDateTime start,
                                        LocalDateTime end);

}
