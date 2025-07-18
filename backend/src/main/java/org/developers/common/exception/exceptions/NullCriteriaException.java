package org.developers.common.exception.exceptions;

public class NullCriteriaException extends VideoServiceException{
    public NullCriteriaException(String message) {
        super(message);
    }

    public NullCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
