package org.developers.model.DTO;


import java.time.LocalDateTime;

public class FavoriteDTO {
    private Long favoriteId;
    private UserDTO user;
    private VideoDTO video;
    private LocalDateTime addedAt;
}
