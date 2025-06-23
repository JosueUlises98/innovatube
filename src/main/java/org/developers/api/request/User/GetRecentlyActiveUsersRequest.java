package org.developers.api.request.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetRecentlyActiveUsersRequest {
    private LocalDateTime sinceDate;
}
