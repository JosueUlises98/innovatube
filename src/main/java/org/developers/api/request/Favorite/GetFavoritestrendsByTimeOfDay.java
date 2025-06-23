package org.developers.api.request.Favorite;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetFavoritestrendsByTimeOfDay {
    private LocalDateTime date;
}
