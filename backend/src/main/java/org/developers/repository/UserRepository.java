package org.developers.repository;

import org.developers.api.response.User.UserActivityMetrics;
import org.developers.model.entities.User;
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
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByLastLoginAfter(LocalDateTime date);

    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u FROM User u WHERE u.isActive = false AND u.lastLogin < :date")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u LEFT JOIN Favorite f ON u.userid = f.userId GROUP BY u.userid, u.name, u.lastname, u.username, u.password, u.email, u.createdAt, u.lastLogin, u.isActive ORDER BY COUNT(f) DESC")
    List<User> findMostActiveUsers(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userid = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);

    @Query("SELECT NEW org.developers.api.response.User.UserActivityMetrics(u.lastLogin, CAST((SELECT COUNT(f2.favoriteId) FROM Favorite f2 WHERE f2.userId = u.userid) AS integer), CASE WHEN u.lastLogin > :thirtyDaysAgo THEN TRUE ELSE FALSE END) FROM User u WHERE u.userid = :userId")
    UserActivityMetrics findUserMetrics(@Param("userId") Long userId, @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}
