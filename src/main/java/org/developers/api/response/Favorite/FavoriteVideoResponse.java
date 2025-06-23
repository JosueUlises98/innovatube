package org.developers.api.response.Favorite;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteVideoResponse {
    private String title;
    private LocalDateTime publishDate;
}
