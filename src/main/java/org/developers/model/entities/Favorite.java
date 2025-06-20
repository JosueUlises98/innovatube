package org.developers.model.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    Long favoriteId;
    // Relación Many-to-One con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User userId;
    // Relación Many-to-One con Video
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    Video videoId;
    @Column(name = "added_at", nullable = false)
    LocalDateTime addedAt;
}
