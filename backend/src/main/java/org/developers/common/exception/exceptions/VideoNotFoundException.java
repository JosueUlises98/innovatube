package org.developers.common.exception.exceptions;

public class VideoNotFoundException extends VideoServiceException{
    public VideoNotFoundException(String message) {
        super(message);
    }

    public VideoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
