package org.developers.api.request.Video;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetLastestVideosRequest {
    @NotNull(message = "El campo 'page' es obligatorio")
    @Positive(message = "El campo 'page' debe ser un número positivo")
    private Integer page;

    @NotNull(message = "El campo 'size' es obligatorio")
    @Positive(message = "El campo 'size' debe ser un número positivo")
    private Integer size;
}
