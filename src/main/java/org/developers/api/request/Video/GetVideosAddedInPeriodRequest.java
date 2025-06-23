package org.developers.api.request.Video;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetVideosAddedInPeriodRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
