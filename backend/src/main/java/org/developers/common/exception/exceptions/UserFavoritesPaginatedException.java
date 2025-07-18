package org.developers.common.exception.exceptions;

public class UserFavoritesPaginatedException extends FavoriteServiceException{
    public UserFavoritesPaginatedException(String message) {
        super(message);
    }

    public UserFavoritesPaginatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
