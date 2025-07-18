package org.developers.common.exception.exceptions;

public class UserFavoriteVideosException extends FavoriteServiceException{
    public UserFavoriteVideosException(String message) {
        super(message);
    }

    public UserFavoriteVideosException(String message, Throwable cause) {
        super(message, cause);
    }
}
