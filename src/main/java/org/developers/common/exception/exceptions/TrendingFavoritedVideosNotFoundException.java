package org.developers.common.exception.exceptions;

public class TrendingFavoritedVideosNotFoundException extends FavoriteServiceException {
    public TrendingFavoritedVideosNotFoundException(String message) {
        super(message);
    }
    public TrendingFavoritedVideosNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
