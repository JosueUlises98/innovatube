package org.developers.common.exception.exceptions;

public class CouldNotCreatedVideoException extends VideoServiceException {
    public CouldNotCreatedVideoException(String message) {
        super(message);
    }
    public CouldNotCreatedVideoException(String message, Throwable cause) {
        super(message, cause);
    }
}
