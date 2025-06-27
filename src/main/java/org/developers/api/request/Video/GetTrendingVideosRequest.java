package org.developers.api.request.Video;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetTrendingVideosRequest {
    @NotNull( message = "El campo 'minimumViews' es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @Max(value = 1000, message = "El valor no puede ser mayor a 1000")
    private Integer minimumViews;
}
