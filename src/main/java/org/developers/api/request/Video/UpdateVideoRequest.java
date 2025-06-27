package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateVideoRequest {
    @NotNull(message = "El campo 'videoId' es obligatorio")
    @Positive(message = "El campo 'videoId' debe ser un número positivo")
    private Long videoId;

    @NotBlank(message = "El campo 'title' es obligatorio")
    private String title;

    @NotBlank(message = "El campo 'description' es obligatorio")
    private String description;

    @NotNull(message = "El campo 'publishDate' es obligatorio")
    private LocalDateTime publishDate;

    @NotBlank(message = "El campo 'thumbnailUrl' es obligatorio")
    private String thumbnailUrl;

    @NotNull(message = "El campo 'duration' es obligatorio")
    private Duration duration;

    @NotNull(message = "El campo 'views' es obligatorio")
    @Positive(message = "El campo 'views' debe ser un número positivo")
    private BigInteger views;

    @NotNull(message = "El campo 'likes' es obligatorio")
    @Positive(message = "El campo 'likes' debe ser un número positivo")
    private BigInteger likes;
}
