package org.developers.api.request.Video;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetCuratedPlayListTheme {
    @NotBlank(message = "Theme should be not blank")
    private String theme;
    @NotNull(message = "Limit cannot be null")
    private Integer limit;
}
