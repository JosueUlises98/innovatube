package org.developers.model.entities;

import jakarta.persistence.*;
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
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;
    @Column(name = "youtube_video_id", unique = true, nullable = false, length = 20)
    @Getter
    private String youtubeVideoId;
    @Column(nullable = false)
    @Setter
    @Getter
    private String title;
    @Column(columnDefinition = "TEXT")
    @Setter
    @Getter
    private String description;
    @Column(name = "thumbnail_url")
    @Setter
    @Getter
    private String thumbnailUrl;
    @Column(name = "duration")
    @Setter
    @Getter
    private String duration;
    @Column(name = "added_at")
    @Setter
    @Getter
    private LocalDateTime addedAt;
    @Column(name = "view_count")
    @Setter
    @Getter
    private BigInteger viewCount;
    @Column(name = "likes")
    @Setter
    @Getter
    private BigInteger likes;
    // Relación One-to-Many con Favorites
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    // Getter para la colección
    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
