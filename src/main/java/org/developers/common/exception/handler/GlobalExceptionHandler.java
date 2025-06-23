package org.developers.common.exception.handler;

import org.developers.common.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "USER_NOT_FOUND");
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVideoNotFoundException(VideoNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "VIDEO_NOT_FOUND");
    }

    @ExceptionHandler({CouldNotCreatedVideoException.class, CouldNotUpdateVideoException.class, CouldNotDeleteVideoException.class})
    public ResponseEntity<Map<String, Object>> handleVideoOperationException(VideoServiceException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "VIDEO_OPERATION_ERROR");
    }

    @ExceptionHandler({FavoritedVideoExistsException.class, FavoritedVideosNotExistsException.class})
    public ResponseEntity<Map<String, Object>> handleFavoriteVideoException(FavoriteServiceException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "FAVORITE_VIDEO_ERROR");
    }

    @ExceptionHandler({TrendingFavoritedVideosNotFoundException.class, RecentlyFavoritedVideosNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleFavoriteVideoNotFoundException(FavoriteServiceException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "FAVORITE_VIDEO_NOT_FOUND");
    }

    @ExceptionHandler({YouTubeException.class, YouTubeSyncException.class})
    public ResponseEntity<Map<String, Object>> handleYouTubeException(YouTubeException ex) {
        return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), "YOUTUBE_SERVICE_ERROR");
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<Map<String, Object>> handleUserServiceException(UserServiceException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "USER_SERVICE_ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Se ha producido un error interno", "INTERNAL_SERVER_ERROR");
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("errorCode", errorCode);

        return new ResponseEntity<>(response, status);
    }

}
