package org.developers.model.entities;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    Long videoId;
    @Column(name = "youtube_video_id", unique = true, nullable = false, length = 20)
    String youtubeVideoId;
    @Column(nullable = false)
    String title;
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(name = "thumbnail_url")
    String thumbnailUrl;
    @Column(name = "duration")
    String duration;
    @Column(name = "added_at")
    LocalDateTime addedAt;
    @Column(name = "view_count")
    Integer viewCount;
    // Relación One-to-Many con Favorites
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    List<Favorite> favorites;

}
