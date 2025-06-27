package org.developers.api.request.Video;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetRecommendedVideosRequest {
    @NotBlank(message = "El campo 'userId' es obligatorio")
    private Long userId;
    @NotNull(message = "El campo 'limit' no puede ser nulo")
    @Positive(message = "El campo 'limit' debe ser un número positivo")
    @Max(value = 1000, message = "El campo 'limit' no puede ser mayor a 1000")
    private Integer limit;
}
