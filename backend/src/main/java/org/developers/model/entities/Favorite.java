package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "favorites",
        indexes = {@Index(
                name = "idx_user_id",
                columnList = "user_id"
        ), @Index(
                name = "idx_video_id",
                columnList = "video_id"
        ), @Index(
                name = "idx_added_at",
                columnList = "added_at"
        )}
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "favorite_id"
    )
    private Long favoriteId;
    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;
    @Column(
            name = "video_id",
            nullable = false
    )
    private Long videoId;
    @Column(
            name = "added_at",
            nullable = false
    )
    private LocalDateTime addedAt;
}
