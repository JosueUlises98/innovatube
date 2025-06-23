package org.developers.common.exception.exceptions;

public class RecentlyFavoritedVideosNotFoundException extends FavoriteServiceException {
    public RecentlyFavoritedVideosNotFoundException(String message) {
        super(message);
    }
    public RecentlyFavoritedVideosNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
