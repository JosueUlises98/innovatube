package org.developers.api.response.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDetails {
    private java.lang.Long userId;
    private java.lang.String username;
    private java.lang.String email;
}
