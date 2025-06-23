package org.developers.api.request.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeactivateInactiveUsersRequest {
    private LocalDateTime thresholdDate;
}
