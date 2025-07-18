package org.developers.api.request.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetUsersRegisteredInPeriod {
    @NotNull(message = "Start date cannot be null")
    private String startDate;
    @NotNull(message = "End date cannot be null")
    private String endDate;
}
