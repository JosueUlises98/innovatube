package org.developers.common.exception.exceptions;

public class FavoritedVideosNotExistsException extends FavoriteServiceException {
    public FavoritedVideosNotExistsException(String message) {
        super(message);
    }
    public FavoritedVideosNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
