package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateVideoRequest {
    @NotBlank( message = "El campo 'youtubeId' es obligatorio")
    private String youtubeId;
}
