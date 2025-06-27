package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GetShortFormVideosRequest {
    @NotBlank( message = "El campo 'minDuration' es obligatorio")
    @Pattern(regexp = "^([0-9]{1,2}):([0-5][0-9]):([0-5][0-9])$", message = "El campo 'maxDuration' debe tener el formato HH:mm:ss")
    String maxDuration;
}
