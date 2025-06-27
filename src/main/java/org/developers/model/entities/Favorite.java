package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites", indexes = {
        @Index(name = "idx_user_video", columnList = "user_id,video_id"),
        @Index(name = "idx_added_at", columnList = "added_at")
})
@Data
@Builder
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;
    // Relación Many-to-One con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;
    // Relación Many-to-One con Video
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Long videoId;
    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
}
