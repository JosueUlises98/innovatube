package org.developers.common.exception.exceptions;

public class YouTubeSyncException extends YouTubeException{
    public YouTubeSyncException(String message) {
        super(message);
    }

    public YouTubeSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
