package org.developers.common.exception.exceptions;

public class CouldNotDeleteVideoException extends VideoServiceException{
    public CouldNotDeleteVideoException(String message) {
        super(message);
    }

    public CouldNotDeleteVideoException(String message, Throwable cause) {
        super(message, cause);
    }
}
