package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "videos", indexes = {
        @Index(name = "idx_youtube_video_id", columnList = "youtube_video_id"),
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_added_at", columnList = "added_at"),
        @Index(name = "idx_view_count", columnList = "view_count"),
        @Index(name = "idx_likes", columnList = "likes")
})
@Builder
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;
    @Column(name = "youtube_video_id", unique = true, nullable = false, length = 20)
    private String youtubeVideoId;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "duration")
    private String duration;
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    @Column(name = "view_count")
    private BigInteger viewCount;
    @Column(name = "likes")
    private BigInteger likes;
    // Relación One-to-Many con Favorites
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    // Getter para la colección
    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
