package org.developers.common.exception.exceptions;

public class VideoAlreadyExistsException extends VideoServiceException{
    public VideoAlreadyExistsException(String message) {
        super(message);
    }

    public VideoAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
