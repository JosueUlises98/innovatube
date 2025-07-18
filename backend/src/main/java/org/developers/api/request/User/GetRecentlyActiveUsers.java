package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetRecentlyActiveUsers {
    @NotNull(message = "Since date cannot be null")
    private LocalDateTime sinceDate;
}
