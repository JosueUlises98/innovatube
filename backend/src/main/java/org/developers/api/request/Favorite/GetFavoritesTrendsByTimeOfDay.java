package org.developers.api.request.Favorite;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetFavoritesTrendsByTimeOfDay {
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;
}
