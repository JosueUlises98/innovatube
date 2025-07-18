package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchVideos {
    @NotBlank(message = "Search cannot be empty")
    private String search;
    @NotBlank(message = "Region code cannot be empty")
    private String regionCode;
    @NotBlank(message = "Language code cannot be empty")
    private String languageCode;
}
