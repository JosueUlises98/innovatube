package org.developers.common.exception.exceptions;

public class VideoServiceException extends RuntimeException {
    public VideoServiceException(String message) {
        super(message);
    }
    public VideoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
