package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RecordUserActivityRequest {
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;
}
