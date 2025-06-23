package org.developers.common.exception.exceptions;

public class CouldNotUpdateVideoException extends VideoServiceException {
    public CouldNotUpdateVideoException(String message) {
        super(message);
    }
    public CouldNotUpdateVideoException(String message, Throwable cause) {
        super(message, cause);
    }
}
