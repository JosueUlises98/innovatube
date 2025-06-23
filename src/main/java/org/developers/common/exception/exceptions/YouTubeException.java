package org.developers.common.exception.exceptions;

public class YouTubeException extends RuntimeException {
    public YouTubeException(String message) {
        super(message);
    }
    public YouTubeException(String message, Throwable cause) {
        super(message, cause);
    }
}
