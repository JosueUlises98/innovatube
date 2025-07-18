package org.developers.common.exception.exceptions;

public class CategorizationException extends VideoServiceException{
    public CategorizationException(String message) {
        super(message);
    }

    public CategorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
