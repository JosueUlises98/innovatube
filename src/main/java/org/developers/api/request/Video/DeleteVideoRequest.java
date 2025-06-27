package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DeleteVideoRequest {
    @NotBlank( message = "El campo 'videoId' es obligatorio")
    @Positive(message = "El campo de ser positivo")
    private Long videoId;
}
