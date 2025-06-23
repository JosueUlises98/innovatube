package org.developers.api.request.User;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String profilePicture;
}
