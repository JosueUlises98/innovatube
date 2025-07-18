package org.developers.common.exception.exceptions;

public class InvalidCriteriaException extends VideoServiceException {
    public InvalidCriteriaException(String message) {
        super(message);
    }

    public InvalidCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
