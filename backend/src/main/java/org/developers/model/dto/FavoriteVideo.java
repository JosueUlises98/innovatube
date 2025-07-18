package org.developers.model.dto;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;

public record FavoriteVideo(Long userId, Long videoId, LocalDateTime addedAt, String videoTitle, String youtubeVideoId, String videoDescription, String thumbnailUrl, Duration duration, BigInteger viewCount, BigInteger likes) {
}
