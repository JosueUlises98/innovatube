package org.developers.api.request.Favorite;

import lombok.Data;
import org.developers.model.entities.User;

@Data
public class GetVideoFavoritesByUsers {
    private User user;
}
