package org.developers.common.exception.exceptions;

public class RemoveFailedException extends VideoServiceException{
    public RemoveFailedException(String message) {
        super(message);
    }

    public RemoveFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
