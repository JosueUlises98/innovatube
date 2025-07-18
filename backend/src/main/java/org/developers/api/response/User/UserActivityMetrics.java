package org.developers.api.response.User;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserActivityMetrics {
    private LocalDateTime lastLogin;
    private Integer favoriteCount;
    private Boolean isActive;
}
