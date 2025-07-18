package org.developers.common.exception.exceptions;

public class InvalidPaginationException extends VideoServiceException{
    public InvalidPaginationException(String message) {
        super(message);
    }

    public InvalidPaginationException(String message, Throwable cause) {
        super(message, cause);
    }
}
