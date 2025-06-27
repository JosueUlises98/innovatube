package org.developers.api.request.Favorite;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GetUserFavoritesPaginated {
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Page cannot be null")
    @Positive(message = "Page must be a positive number")
    private Integer page;

    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be a positive number")
    private Integer size;
}
