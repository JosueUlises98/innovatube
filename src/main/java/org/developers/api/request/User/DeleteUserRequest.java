package org.developers.api.request.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserRequest {
    private String username;
    private String email;
}
