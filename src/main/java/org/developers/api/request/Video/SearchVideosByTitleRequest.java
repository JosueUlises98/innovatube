package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchVideosByTitleRequest {
    @NotBlank( message = "El campo 'title' es obligatorio")
    private String title;
}
