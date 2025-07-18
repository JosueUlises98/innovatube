package org.developers.common.exception.exceptions;

public class AddFailedException extends VideoServiceException{
    public AddFailedException(String message) {
        super(message);
    }

    public AddFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
