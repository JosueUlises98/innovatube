package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.developers.common.utils.converter.DurationConverter;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "videos",
        indexes = {@Index(
                name = "idx_youtube_video_id",
                columnList = "youtube_video_id"
        ), @Index(
                name = "idx_added_at",
                columnList = "added_at"
        ), @Index(
                name = "idx_view_count",
                columnList = "view_count"
        )}
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "video_id"
    )
    private Long videoId;
    @Column(
            name = "youtube_video_id",
            unique = true,
            nullable = false,
            length = 20
    )
    private String youtubeVideoId;
    @Column(
            nullable = false
    )
    private String title;
    @Column(
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name = "thumbnail_url"
    )
    private String thumbnailUrl;
    @Convert(
            converter = DurationConverter.class
    )
    @Column(
            name = "duration",
            columnDefinition = "VARCHAR(100)"
    )
    private Duration duration;
    @Column(
            name = "added_at"
    )
    private LocalDateTime addedAt;
    @Column(
            name = "view_count",
            columnDefinition = "int8"
    )
    private BigInteger viewCount;
    @Column(
            name = "likes",
            columnDefinition = "int8"
    )
    private BigInteger likes;
    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;
}
