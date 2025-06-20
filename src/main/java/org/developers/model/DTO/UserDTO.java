package org.developers.model.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {
    private Long userid;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String profilePicture;
    private List<FavoriteDTO> favorites;
}
