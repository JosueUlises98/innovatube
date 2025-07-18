package org.developers.api.request.Video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SearchVideosWithPagination {
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotNull(message = "Page number cannot be null")
    @Positive(message = "Page number must be a positive number")
    private Integer page;
    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be a positive number")
    private Integer size;
}
