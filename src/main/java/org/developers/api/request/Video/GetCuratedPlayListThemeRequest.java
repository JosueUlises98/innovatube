package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GetCuratedPlayListThemeRequest {
    @NotBlank(message = "El campo 'theme' es obligatorio")
    private String theme;

    @NotNull(message = "El campo 'limit' no puede ser nulo")
    @Positive(message = "El campo 'limit' debe ser un número positivo")
    private Integer limit;
}
