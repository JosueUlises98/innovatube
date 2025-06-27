package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SearchVideosWithPaginationRequest {
    @NotBlank(message = "El campo 'title' es obligatorio")
    private String title;

    @Positive(message = "El campo 'page' debe ser un número positivo")
    private Integer page;

    @Positive(message = "El campo 'size' debe ser un número positivo")
    private Integer size;
}
