package org.developers.repository;

import org.developers.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Búsquedas básicas
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Búsquedas compuestas
    List<User> findByLastLoginAfter(LocalDateTime date);
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Consultas personalizadas
    @Query("SELECT u FROM User u WHERE u.lastLogin < :date")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u LEFT JOIN u.favorites f GROUP BY u " +
            "ORDER BY COUNT(f) DESC")
    List<User> findMostActiveUsers();

    // Consulta para actualizar último login
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.userid = :userId")
    void updateLastLogin(@Param("userId") Long userId,
                         @Param("lastLogin") LocalDateTime lastLogin);

}
