package org.developers.common.exception.exceptions;

public class FavoritedVideoExistsException extends FavoriteServiceException {
    public FavoritedVideoExistsException(String message) {
        super(message);
    }
    public FavoritedVideoExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
