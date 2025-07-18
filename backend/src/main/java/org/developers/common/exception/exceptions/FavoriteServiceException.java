package org.developers.common.exception.exceptions;

public class FavoriteServiceException extends RuntimeException{

    public FavoriteServiceException(String message) {
        super(message);
    }

    public FavoriteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
