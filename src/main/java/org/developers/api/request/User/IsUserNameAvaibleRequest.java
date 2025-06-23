package org.developers.api.request.User;

import lombok.Data;

@Data
public class IsUserNameAvaibleRequest {
    private String username;
    private Long userId;
}
