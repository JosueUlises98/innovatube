package org.developers.api.request.Favorite;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMostFavoritedVideosOfPeriod {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer limit;
}
